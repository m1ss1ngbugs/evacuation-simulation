package evacuation.sim.gui;


import evacuation.sim.core.Simulation;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiApplication extends Application {


    /* TODO: potrzebne jest rozwiązanie: albo mapa będzie statyczna, albo musi później być możliwość rysowania
     TODO: jej za pomocą GUI */

    @Override
    public void start(Stage primaryStage) throws IOException {

//        // logic initialization before window creation
//        simulation = new Simulation();
//
//        int dynamicWidth = simulation.getBoard().getWidth();
//        int dynamicHeight = simulation.getBoard().getHeight();
//
//        // creates canvas to draw the simulation
//        Canvas canvas = new Canvas(dynamicWidth * TILE_SIZE, dynamicHeight * TILE_SIZE);
//        GraphicsContext gc = canvas.getGraphicsContext2D(); // virtual brush for canvas configuration
//
//        // draws the board
//        render(gc);
//
//        // packs canvas to layout and creates the scene
//        StackPane stackPane = new StackPane();
//        stackPane.getChildren().add(canvas);
//        Scene scene = new Scene(stackPane);
//
//        // the main window configuration
//        primaryStage.setTitle("Symulacja Ewakuacji");
//        primaryStage.setScene(scene);
//        primaryStage.setResizable(false); // blocks window resizing for ease of use
//
//        // creates a time engine
//        AnimationTimer timer = new AnimationTimer() {
//            private long lastUpdate = 0;
//
//            @Override
//            public void handle(long now) {
//                // For the first time
//                if (lastUpdate == 0) {
//                    lastUpdate = now;
//                    return;
//                }
//
//                // count time in seconds
//                float dt = (now - lastUpdate) / 1_000_000_000.0f;
//                lastUpdate = now;
//
//                // pushing simulation logic forward
//                simulation.updateTick(dt);
//
//                // draws new state on the screen
//                render(gc);
//            }
//        };
//        timer.start(); // Uruchamiamy zegar!


        // 1. Ładowarka plików FXML (szuka pliku w folderze resources)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_layout.fxml"));

        // 2. Wczytujemy całą strukturę widoku (nasz główny BorderPane)
        Parent root = loader.load();

        // 3. Tworzymy scenę
        Scene scene = new Scene(root);

        primaryStage.setTitle("Symulacja Ewakuacji");
        primaryStage.setScene(scene);


        // captures the window closing event
        primaryStage.setOnCloseRequest(event -> {
            javafx.application.Platform.exit(); // turn off JavaFX engine
            System.exit(0); // close application with 0 code
        });

        primaryStage.show(); // show the window
    }
}