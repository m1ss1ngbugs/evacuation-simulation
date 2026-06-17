package evacuation.sim.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main GUI class based on the JavaFX framework.
 * Responsible for initializing the windowing environment, displaying browsers
 * from an FXML file, and setting application window settings.
 * @author Heorhii Yartsev (293562)
 */
public class GuiApplication extends Application {

    /**
     * The main JavaFX lifecycle method, called automatically when the application starts.
     * Loads the interface structure from the {@code /main_layout.fxml} file, creates the stage,
     * forces full-screen mode (maximized).
     *
     * @param primaryStage the main application window (Stage) provided by the JavaFX platform
     * @throws IOException if a read error occurs or the FXML view file is not found
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Loading FXML view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_layout.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setTitle("Evacuation Simulation");
        primaryStage.setScene(scene);

        // run in full screen
        primaryStage.setMaximized(true);

        primaryStage.setOnCloseRequest(event -> {
            javafx.application.Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }
}