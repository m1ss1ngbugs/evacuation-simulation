module evacuation.sim {

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    exports evacuation.sim;
    exports evacuation.sim.gui;

    opens evacuation.sim.gui to javafx.fxml;
}