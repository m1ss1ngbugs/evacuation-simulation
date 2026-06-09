//package evacuation.sim.gui;
//
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.scene.control.Label;
//import javafx.scene.layout.Priority;
//
//public class StatsPanel extends HBox {
//
//    public StatsPanel() {
//        this.setPrefHeight(180); // fixed height
//        this.setSpacing(20);
//        // this.setStyle("-fx-background-color: linear-gradient(to bottom, #dc4545, #dc4545); -fx-padding: 20px;");
//
//        VBox leftTile = new VBox();
//        HBox.setHgrow(leftTile, Priority.ALWAYS);
//        leftTile.setStyle("-fx-background-color: linear-gradient(to bottom, #6645dc, #6200ff); -fx-padding: 20px; -fx-background-radius: 12px;");
//        Label title1 = new Label("Statistics");
//        title1.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
//        leftTile.getChildren().add(title1);
//
//
//        VBox rightTile = new VBox();
//        rightTile.setPrefWidth(300);
//        rightTile.setStyle("-fx-background-color: linear-gradient(to bottom, #6645dc, #6200ff); -fx-padding: 20px; -fx-background-radius: 12px;");
//        Label title2 = new Label("Analytics");
//        title2.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
//        rightTile.getChildren().add(title2);
//
//        this.getChildren().addAll(leftTile, rightTile);
//    }
//}
