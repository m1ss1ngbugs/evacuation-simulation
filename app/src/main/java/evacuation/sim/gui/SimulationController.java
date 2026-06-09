package evacuation.sim.gui;

import evacuation.sim.SimSingletonConfig;
import evacuation.sim.agent.Agent;
import evacuation.sim.agent.human.Evacuee;
import evacuation.sim.core.Simulation;
import evacuation.sim.model.BaseType;
import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;

public class SimulationController {

    private static final int TILE_SIZE = 20; // size of a single Cell

    // 1. FXML injections
    @FXML private Canvas simulationCanvas;
    @FXML private Slider peopleSlider;
    @FXML private Label savedLabel;

    private Simulation simulation;
    private GraphicsContext gc;
    private AnimationTimer timer;

    // the initialization method for GUI
    @FXML
    public void initialize() {
        // the brush
        gc = simulationCanvas.getGraphicsContext2D();

        // set the slider default values
        SimSingletonConfig config = SimSingletonConfig.getInstance();
        peopleSlider.setMin(1);
        peopleSlider.setMax(100);
        peopleSlider.setValue(config.getInitialEvacueesCount());

        System.out.println("Controller is ready");
    }

    // method attached to the button
    @FXML
    public void onStartClicked() {
        System.out.println("Start button was clicked");

        // stops the old clock
        if (timer != null) {
            timer.stop();
        }

        // TODO: save this later with the other parameters in the SimSingletonConfig
        int peopleCount = (int) peopleSlider.getValue();
        System.out.println("Evacuee count: " + peopleCount);

        simulation = new Simulation();

        // matching tne size
        simulationCanvas.setWidth(simulation.getBoard().getWidth() * TILE_SIZE);
        simulationCanvas.setHeight(simulation.getBoard().getHeight() * TILE_SIZE);

        // launches the time loop
        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {

                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                float dt = (now - lastUpdate) / 1_000_000_000.0f;
                lastUpdate = now;

                // logic push
                simulation.updateTick(dt);
                render(gc);


            }
        };
        timer.start();
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
                gc.setStroke(Color.GRAY);
                gc.strokeRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // draws the agents
        gc.setFill(Color.BLUE); // evacuees - blue circles
        for (Agent agent : simulation.getAgents()) {
            if(agent instanceof Evacuee) {
                // convert logical position to pixels position
                double px = agent.getLogicalX() * TILE_SIZE;
                double py = agent.getLogicalY() * TILE_SIZE;

                // draw the agent (circle)
                gc.fillOval(px, py, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}