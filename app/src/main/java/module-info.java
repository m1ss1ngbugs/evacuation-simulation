module evacuation.sim {

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    exports evacuation.sim;
    exports evacuation.sim.gui;
    exports evacuation.sim.core;
    exports evacuation.sim.event;
    exports evacuation.sim.agent;
    exports evacuation.sim.agent.human;
    exports evacuation.sim.agent.hazard;
    exports evacuation.sim.model;

    opens evacuation.sim.gui to javafx.fxml;
}





