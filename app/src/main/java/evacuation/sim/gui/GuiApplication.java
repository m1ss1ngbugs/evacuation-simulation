package evacuation.sim.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Loading FXML view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_layout.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setTitle("Symulacja Ewakuacji");
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