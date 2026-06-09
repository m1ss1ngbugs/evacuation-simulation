//<<<<<<< HEAD
////package evacuation.sim.gui;
////
////import javafx.scene.layout.HBox;
////import javafx.scene.layout.Priority;
////import javafx.scene.layout.VBox;
////import evacuation.sim.SimSingletonConfig;
////import javafx.scene.control.Button;
////import javafx.scene.control.Label;
////import javafx.scene.control.Slider;
////import javafx.scene.control.TextField;
////import evacuation.sim.core.Simulation;
////
////
////public class ControlPanel extends VBox {
////    public ControlPanel(GuiApplication mainApp, Simulation simulation) {
////
////        this.setPrefWidth(400); // fixed width
////        this.setSpacing(15);
////        this.setStyle("-fx-background-color: #cd03ff2f; -fx-padding: 20px;");
////
////        HBox buttonBox = new HBox(40);
////        HBox mapInputBox = new HBox(8);
////        // VBox ratioBox = new VBox(10);
////
////        // MAP
////        Label labelMap = new Label("Map file:");
////        labelMap.setStyle("-fx-text-fill: white;");
////        TextField inputMap = new TextField(SimSingletonConfig.getInstance().getMapFilePath());
////        HBox.setHgrow(inputMap, Priority.ALWAYS);
////
////        Button browseMapButton = new Button("Choose...");
////
////        browseMapButton.setOnAction(e -> {
////            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
////            fileChooser.setTitle("Choose a file");
////            fileChooser.getExtensionFilters().add(
////                new javafx.stage.FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt")
////            );
////
////            java.io.File selectedFile = fileChooser.showOpenDialog(browseMapButton.getScene().getWindow());
////
////            if (selectedFile != null) {
////                // Jeśli użytkownik wybrał plik z okienka, wpisujemy jego pełną ścieżkę do pola tekstowego!
////                inputMap.setText(selectedFile.getAbsolutePath());
////            }
////        });
////
////        mapInputBox.getChildren().addAll(inputMap, browseMapButton);
////
////        // MAX EVACUATION TIME //TODO: Poprawki
////       /*  Label maxEvacTime = new Label("Max evacuation time:");
////        TextField inputMaxEvacTime = new TextField(String.valueOf(SimSingletonConfig.getInstance().getMaxEvacuationTime()));
////        */
////
////        // EVACUEES
////        Label evacueeCount = new Label("Number of evacuees:");
////        evacueeCount.setStyle("-fx-text-fill: white;");
////        TextField inputEvacueeCount = new TextField(String.valueOf(SimSingletonConfig.getInstance().getInitialEvacueesCount()));
////
////        //Label ratioLabel = new Label("Ratio of:");
////        //Label leaderLabel = new Label("leaders");
////        //Slider sLeader = new Slider(0, 1, SimSingletonConfig.getInstance().getLeaderRatio());
////        //Label followerLabel = new Label("followers");
////        //Slider sFollower = new Slider(0, 1, SimSingletonConfig.getInstance().getFollowerRatio());
////        //Label panickedLabel = new Label("panicked");
////       // Slider sPanicked = new Slider(0, 1, SimSingletonConfig.getInstance().getPanickedRatio());
////
////        //ratioBox.getChildren().addAll(ratioLabel, leaderLabel, sLeader, followerLabel, sFollower, panickedLabel, sPanicked);
////
////
////
////        Button startButton = new Button("Start");
////        Button pauseButton = new Button("Pause");
////        Button resetButton = new Button("Reset");
////
////        startButton.setOnAction(e -> mainApp.getTimer().start());
////        pauseButton.setOnAction(e -> mainApp.getTimer().stop());
////        resetButton.setOnAction(e -> {
////            try {
////
////                int newCount = Integer.parseInt(inputEvacueeCount.getText());
////                String newMapPath = inputMap.getText();
////
////                SimSingletonConfig.getInstance().setInitialEvacueesCount(newCount);
////                SimSingletonConfig.getInstance().setMapFilePath(newMapPath);
////
////                mainApp.getTimer().stop();
////
////                mainApp.handleReset();
////
////            } catch (NumberFormatException ex) {
////                System.out.println("ERROR: Please enter correct number of evacuees.");
////            }
////        });
////
////        buttonBox.getChildren().addAll(startButton, pauseButton, resetButton);
////
////        Label title = new Label("Evacuation Simulation");
////        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
////
////        this.getChildren().addAll(title, labelMap, mapInputBox, evacueeCount, inputEvacueeCount, /*ratioBox,*/ buttonBox);
////
////    }
////}
//=======
//package evacuation.sim.gui;
//
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.VBox;
//import evacuation.sim.SimSingletonConfig;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.Slider;
//import javafx.scene.control.TextField;
//import evacuation.sim.core.Simulation;
//
//
//public class ControlPanel extends VBox {
//    public ControlPanel(GuiApplication mainApp, Simulation simulation) {
//
//        this.setPrefWidth(400); // fixed width
//        this.setSpacing(15);
//        this.setStyle("-fx-background-color: #cd03ff2f; -fx-padding: 20px;");
//
//        HBox buttonBox = new HBox(40);
//        HBox mapInputBox = new HBox(8);
//        // VBox ratioBox = new VBox(10);
//
//        // MAP
//        Label labelMap = new Label("Map file:");
//        //labelMap.setStyle("-fx-text-fill: white;");
//        TextField inputMap = new TextField(SimSingletonConfig.getInstance().getMapFilePath());
//        HBox.setHgrow(inputMap, Priority.ALWAYS);
//
//        Button browseMapButton = new Button("Choose...");
//
//        browseMapButton.setOnAction(e -> {
//            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
//            fileChooser.setTitle("Choose a file");
//            fileChooser.getExtensionFilters().add(
//                new javafx.stage.FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt")
//            );
//
//            java.io.File selectedFile = fileChooser.showOpenDialog(browseMapButton.getScene().getWindow());
//
//            if (selectedFile != null) {
//                // Jeśli użytkownik wybrał plik z okienka, wpisujemy jego pełną ścieżkę do pola tekstowego!
//                inputMap.setText(selectedFile.getAbsolutePath());
//            }
//        });
//
//        mapInputBox.getChildren().addAll(inputMap, browseMapButton);
//
//        // MAX EVACUATION TIME //TODO: Poprawki
//        Label maxEvacTime = new Label("Max evacuation time:");
//        TextField inputMaxEvacTime = new TextField(String.valueOf(SimSingletonConfig.getInstance().getMaxEvacuationTime()));
//
//        // EVACUEES
//        Label evacueeCount = new Label("Number of evacuees:");
//        //evacueeCount.setStyle("-fx-text-fill: white;");
//        TextField inputEvacueeCount = new TextField(String.valueOf(SimSingletonConfig.getInstance().getInitialEvacueesCount()));
//
//        //Label ratioLabel = new Label("Ratio of:");
//        //Label leaderLabel = new Label("leaders");
//        //Slider sLeader = new Slider(0, 1, SimSingletonConfig.getInstance().getLeaderRatio());
//        //Label followerLabel = new Label("followers");
//        //Slider sFollower = new Slider(0, 1, SimSingletonConfig.getInstance().getFollowerRatio());
//        //Label panickedLabel = new Label("panicked");
//       // Slider sPanicked = new Slider(0, 1, SimSingletonConfig.getInstance().getPanickedRatio());
//
//        //ratioBox.getChildren().addAll(ratioLabel, leaderLabel, sLeader, followerLabel, sFollower, panickedLabel, sPanicked);
//
//
//
//        Button startButton = new Button("Start");
//        Button pauseButton = new Button("Pause");
//        Button resetButton = new Button("Reset");
//
//        startButton.setOnAction(e -> mainApp.getTimer().start());
//        pauseButton.setOnAction(e -> mainApp.getTimer().stop());
//        resetButton.setOnAction(e -> {
//            try {
//
//                int newCount = Integer.parseInt(inputEvacueeCount.getText());
//                String newMapPath = inputMap.getText();
//
//                SimSingletonConfig.getInstance().setInitialEvacueesCount(newCount);
//                SimSingletonConfig.getInstance().setMapFilePath(newMapPath);
//
//                mainApp.getTimer().stop();
//
//                mainApp.handleReset();
//
//            } catch (NumberFormatException ex) {
//                System.out.println("ERROR: Please enter correct number of evacuees.");
//            }
//        });
//
//        buttonBox.getChildren().addAll(startButton, pauseButton, resetButton);
//
//        Label title = new Label("Evacuation Simulation");
//        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
//
//        this.getChildren().addAll(title, labelMap,
//            mapInputBox, evacueeCount,
//            inputEvacueeCount, /*ratioBox,*/
//            buttonBox);
//
//    }
//}
//>>>>>>> 329588839c1cba27d4002b1c7268f11039b7c92e
