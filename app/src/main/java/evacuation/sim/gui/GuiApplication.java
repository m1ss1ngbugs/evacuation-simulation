package evacuation.sim.gui;

import evacuation.sim.agent.Agent;
import evacuation.sim.core.Simulation;
import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;
import evacuation.sim.model.BaseType;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GuiApplication extends Application {

    private Simulation simulation;
    private AnimationTimer timer;

    private Canvas canvas;
    private GraphicsContext gc;
    

    private static int TILE_SIZE = 20; // size of a single Cell
    /* TODO: potrzebne jest rozwiązanie: albo mapa będzie statyczna, albo musi później być możliwość rysowania
     TODO: jej za pomocą GUI */

    @Override
    public void start(Stage primaryStage) {

        // logic initialization before window creation
        simulation = new Simulation();

        // 2. POBIERANIE DYNAMICZNYCH WYMIARÓW Z LOGIKI
        int dynamicWidth = simulation.getBoard().getWidth();
        int dynamicHeight = simulation.getBoard().getHeight();

        double availableWidth = 900.0;
        double availableHeight = 500.0;

        double tileWidth = availableWidth / dynamicWidth;
        double tileHeight = availableHeight / dynamicHeight;

        int calculatedTileSize = (int) Math.min(tileHeight, tileWidth);

        TILE_SIZE = Math.max(10, Math.min(40, calculatedTileSize));

        // creates canvas to draw the simulation
        this.canvas = new Canvas(dynamicWidth * TILE_SIZE, dynamicHeight * TILE_SIZE);
        this.gc = this.canvas.getGraphicsContext2D(); // virtual brush for canvas configuration

        // draws the board
        render(gc);

        // packs canvas to layout and creates the scene
        //TODO: Usunac to, zrobic osobne pliki do tego
        // StackPane stackPane = new StackPane();
        // stackPane.getChildren().add(canvas);
        // Scene scene = new Scene(stackPane);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #6c0ab7, #4214ad);");

        ControlPanel controlPanel = new ControlPanel(this, simulation);
        mainLayout.setLeft(controlPanel);

        VBox rightLayout = new VBox(20);
        rightLayout.setStyle("-fx-padding: 20px;");

        StackPane mapBox = new StackPane(canvas);
        VBox.setVgrow(mapBox, Priority.ALWAYS);
        mapBox.setStyle("-fx-background-radius: 12px; -fx-padding: 10px; -fx-background-color: #cd03ff2f");

        StatsPanel statsPanel = new StatsPanel();

        rightLayout.getChildren().addAll(mapBox, statsPanel);

        mainLayout.setCenter(rightLayout);

        Scene scene = new Scene(mainLayout);

        scene.getStylesheets().add("data:text/css, .label { -fx-text-fill: white; }");

        // the main window configuration
        primaryStage.setTitle("Symulacja Ewakuacji");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // start in full screen
        primaryStage.setResizable(false); // blocks window resizing for ease of use

        // creates a time engine
        this.timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // For the first time
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                // count time in seconds
                float dt = (now - lastUpdate) / 1_000_000_000.0f;
                lastUpdate = now;

                // pushing simulation logic forward
                simulation.updateTick(dt);

                // draws new state on the screen
                render(gc);
            }
        };

        // timer.start(); // Uruchamiamy zegar!

        // captures the window closing event
        primaryStage.setOnCloseRequest(event -> {
            javafx.application.Platform.exit(); // turn off JavaFX engine
            System.exit(0); // close application with 0 code
        });

        primaryStage.show(); // show the window
    }

    private void drawTestScreen(GraphicsContext gc) {
        // blurs the entire background in gray
        gc.setFill(Color.LIGHTGRAY); // chooses a paint color
        gc.fillRect(0, 0, 10 * TILE_SIZE, 10 * TILE_SIZE);

        // draws test red square
        gc.setFill(Color.RED);
        gc.fillRect(5 * TILE_SIZE, 5 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public void handleReset() {
    this.simulation.restartSimulation();

    int newW = simulation.getBoard().getWidth();
    int newH = simulation.getBoard().getHeight();

    double availableWidth = 900.0;
    double availableHeight = 500.0;
    int calculatedTileSize = (int) Math.min(availableWidth / newW, availableHeight / newH);
    TILE_SIZE = Math.max(10, Math.min(40, calculatedTileSize));

    this.gc.clearRect(0, 0, 3000, 3000);

    this.canvas.setWidth(newW * TILE_SIZE);
    this.canvas.setHeight(newH * TILE_SIZE);

    render(this.gc);
}

    private void render(GraphicsContext gc) {
        Board board = simulation.getBoard(); // take the board out of the simulation

        // clears the entire canvas
        gc.clearRect(0, 0, board.getWidth() * TILE_SIZE, board.getHeight() * TILE_SIZE);

        // drawing the board (cells)
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Cell cell = board.getCell(x, y);

                // chooses the color
                if (cell.getBaseType() == BaseType.WALL) {
                    gc.setFill(Color.BLACK);
                } else if (cell.getBaseType() == BaseType.EXIT) {
                    gc.setFill(Color.GREEN);
                } else if (cell.getBaseType() == BaseType.OBSTACLE) {
                    gc.setFill(Color.BURLYWOOD);
                } else {
                    gc.setFill(Color.LIGHTGRAY); // Domyślna podłoga
                }

                // changes the color if there is hazard on the cell
                if (cell.getDynamicState() == DynamicState.FIRE) {
                    gc.setFill(Color.RED);
                } else if (cell.getDynamicState() == DynamicState.SMOKE) {
                    gc.setFill(Color.DARKGRAY);
                }

                // draw rectangle for this cell
                gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                // chooses the color for the frame of the cell and draws it
                // gc.setStroke(Color.GRAY);
                // gc.strokeRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // draws the agents
        gc.setFill(Color.BLUE); // evacuees - blue circles
        for (Agent agent : simulation.getAgents()) {
            if(agent instanceof evacuation.sim.agent.human.Evacuee) {
                // convert logical position to pixels position
                double px = agent.getRenderX() * TILE_SIZE;
                double py = agent.getRenderY() * TILE_SIZE;

                // draw the agent (circle)
                gc.fillOval(px, py, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public AnimationTimer getTimer() {
        return this.timer;
    }

    public Simulation getSimulation() {
        return this.simulation;
    }
}