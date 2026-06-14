package evacuation.sim;

import evacuation.sim.gui.GuiApplication;
import javafx.application.Application;

/**
 * The main application startup class.
 * Initializes the entire system and starts the JavaFX runtime environment.
 * @author Heorhii Yartsev (293562)
 */
public class App {
    /**
     * Main method for launching the program.
     * Passes control to the JavaFX engine, which loads the graphical interface.
     * @param args command-line parameters (optional)
     */
    public static void main(String[] args) {
        // launches javaFX engine
        Application.launch(GuiApplication.class, args);
    }
}