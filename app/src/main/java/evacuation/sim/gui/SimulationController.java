package evacuation.sim.gui;

import evacuation.sim.SimSingletonConfig;
import evacuation.sim.agent.Agent;
import evacuation.sim.agent.human.Evacuee;
import evacuation.sim.core.Simulation;
import evacuation.sim.model.BaseType;
import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.File;

public class SimulationController {

    // dynamic size of the cell on the canvas
    private int tileSize = 20;

    @FXML private Canvas simulationCanvas;
    @FXML private TextField inputMap;
    @FXML private Label savedLabel;
    // initial parameters sliders and labels
    // population sliders and builders
    @FXML private Slider initialEvacueesCountSlider;
    @FXML private Label initialEvacueesCountLabel;
    // roles sliders and labels
    @FXML private Slider leaderRatioSlider;
    @FXML private Label leaderRatioLabel;
    @FXML private Slider followerRatioSlider;
    @FXML private Label followerRatioLabel;
    @FXML private Slider panickedRatioSlider;
    @FXML private Label panickedRatioLabel;
    // blocks StackOverFlowError when slider moving
    private boolean isUpdatingRatios = false;
    // physics sliders and labels
    @FXML private Slider meanBaseSpeedSlider;
    @FXML private Label meanBaseSpeedLabel;
    @FXML private Slider speedVarianceSlider;
    @FXML private Label speedVarianceLabel;
    @FXML private Slider evacueeHealthSlider;
    @FXML private Label evacueeHealthLabel;
    @FXML private Slider evacueeReactionTimeSlider;
    @FXML private Label evacueeReactionTimeLabel;
    @FXML private Slider evacueeVisionRadiusSlider;
    @FXML private Label evacueeVisionRadiusLabel;
    // psychological sliders and labels
    @FXML private Slider meanPanicThresholdSlider;
    @FXML private Label meanPanicThresholdLabel;
    @FXML private Slider panicVarianceSlider;
    @FXML private Label panicVarianceLabel;
    @FXML private Slider meanSocialFactorSlider;
    @FXML private Label meanSocialFactorLabel;
    @FXML private Slider socialVarianceSlider;
    @FXML private Label socialVarianceLabel;
    // fire sliders and labels
    @FXML private Slider initialFireHazardsCountSlider;
    @FXML private Label initialFireHazardsCountLabel;
    @FXML private Slider fireDamagePerSecondSlider;
    @FXML private Label fireDamagePerSecondLabel;
    @FXML private Slider fireSpreadIntervalSlider;
    @FXML private Label fireSpreadIntervalLabel;
    @FXML private Slider fireIncubationDelaySlider;
    @FXML private Label fireIncubationDelayLabel;
    // smoke sliders and labels
    @FXML private Slider smokeDamagePerSecondSlider;
    @FXML private Label smokeDamagePerSecondLabel;
    @FXML private Slider smokeSpreadIntervalSlider;
    @FXML private Label smokeSpreadIntervalLabel;
    @FXML private Slider smokeInitialDensitySlider;
    @FXML private Label smokeInitialDensityLabel;
    @FXML private Slider smokeFadeRatePerSecondSlider;
    @FXML private Label smokeFadeRatePerSecondLabel;
    @FXML private Slider smokeDuplicationThresholdSlider;
    @FXML private Label smokeDuplicationThresholdLabel;

    private Simulation simulation;
    private GraphicsContext gc;
    private AnimationTimer timer;

    @FXML
    public void initialize() {
        gc = simulationCanvas.getGraphicsContext2D();
        SimSingletonConfig config = SimSingletonConfig.getInstance();

        // Initial evacuee count slider and label
        setupSlider(initialEvacueesCountSlider, initialEvacueesCountLabel,
                1, 100, config.getInitialEvacueesCount(), "%.0f");
        // Initial fire hazards count slider and label
        setupSlider(initialFireHazardsCountSlider, initialFireHazardsCountLabel,
                0, 10, config.getInitialFireHazardsCount(), "%.0f");
        // Setups ratio logic for ratio sliders and labels
        setupRatioLogic();
        // Initial meanBaseSpeed slider and label
        setupSlider(meanBaseSpeedSlider, meanBaseSpeedLabel,
                0.5f, 3.0f, config.getMeanBaseSpeed(), "%.1f");
        // Initial speedVariance slider and label
        setupSlider(speedVarianceSlider, speedVarianceLabel,
                0.0f, 1.0f, config.getSpeedVariance(), "%.1f");
        // Initial evacueeHealth slider and label
        setupSlider(evacueeHealthSlider, evacueeHealthLabel,
                50, 200, config.getEvacueeHealth(), "%.0f");
        // Initial evacueeReactionTime slider and label
        setupSlider(evacueeReactionTimeSlider, evacueeReactionTimeLabel,
                0.0f, 10.0f, config.getEvacueeReactionTime(),"%.1f");
        // Initial evacueeVisionRadius slider and label
        setupSlider(evacueeVisionRadiusSlider, evacueeVisionRadiusLabel,
                1, 15, config.getEvacueeVisionRadius(), "%.0f");
        // Initial meanPanicThreshold slider and label
        setupSlider(meanPanicThresholdSlider, meanPanicThresholdLabel,
                10, 100, config.getMeanPanicThreshold(), "%.0f");
        // Initial panicVariance slider and label
        setupSlider(panicVarianceSlider, panicVarianceLabel,
                0, 30, config.getPanicVariance(), "%.0f");
        // Initial meanSocialFactor slider and label
        setupSlider(meanSocialFactorSlider, meanSocialFactorLabel,
                0, 100, config.getMeanSocialFactor()*100, "%.0f%%");
        // Initial socialVariance slider and label
        setupSlider(socialVarianceSlider, socialVarianceLabel,
                0, 40, config.getSocialVariance()*100, "%.0f%%");
        // Initial fireDamagePerSecond slider and label
        setupSlider(fireDamagePerSecondSlider, fireDamagePerSecondLabel,
                20, 100, config.getFireDamagePerSecond(), "%.0f");
        // Initial fireSpreadInterval slider and label
        setupSlider(fireSpreadIntervalSlider, fireSpreadIntervalLabel,
                0.5f, 10.0f, config.getFireSpreadInterval(), "%.1f");
        // Initial fireIncubationDelay slider and label
        setupSlider(fireIncubationDelaySlider, fireIncubationDelayLabel,
                0.0f, 10.0f, config.getFireIncubationDelay(), "%.1f");
        // Initial smokeDamagePerSecond slider and label
        setupSlider(smokeDamagePerSecondSlider, smokeDamagePerSecondLabel,
                1, 15, config.getSmokeDamagePerSecond(), "%.1f");
        // Initial smokeSpreadInterval slider and label
        setupSlider(smokeSpreadIntervalSlider, smokeSpreadIntervalLabel,
                0.0, 5.0, config.getSmokeSpreadInterval(), "%.1f");
        // Initial smokeInitialDensity slider and label
        setupSlider(smokeInitialDensitySlider, smokeInitialDensityLabel,
                20, 140, config.getSmokeInitialDensity(), "%.0f");
        // Initial smokeFadeRatePerSecond slider and label
        setupSlider(smokeFadeRatePerSecondSlider, smokeFadeRatePerSecondLabel,
                5, 25, config.getSmokeFadeRatePerSecond(), "%.0f");
        // Initial smokeDuplicationThreshold slider and label
        setupSlider(smokeDuplicationThresholdSlider, smokeDuplicationThresholdLabel,
                5, 30, config.getSmokeDuplicationThreshold(), "%.0f");

        // text fields
        inputMap.setText(config.getMapFilePath());
    }

    // file selecting
    @FXML
    public void onBrowseMapClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik mapy");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(simulationCanvas.getScene().getWindow());
        if (selectedFile != null) {
            inputMap.setText(selectedFile.getAbsolutePath());
        }
    }

    // simulation launching and scaling
    @FXML
    public void onStartClicked() {
        if (timer != null) timer.stop();

        try {
            // Updating the Singleton with data from sliders
            int newCount = (int) initialEvacueesCountSlider.getValue();
            int newFireCount = (int) initialFireHazardsCountSlider.getValue();
            float newLeaderRatio = (float) (leaderRatioSlider.getValue() / 100);
            float newFollowerRatio = (float) (followerRatioSlider.getValue() / 100);
            float newPanickedRatio = (float) (panickedRatioSlider.getValue() / 100);
            float newMeanBaseSpeed = (float) meanBaseSpeedSlider.getValue();
            float newSpeedVariance = (float) speedVarianceSlider.getValue();
            float newEvacueeHealth = (float) evacueeHealthSlider.getValue();
            float newEvacueeReactionTime = (float) evacueeReactionTimeSlider.getValue();
            int newEvacueeVisionRadius = (int) evacueeVisionRadiusSlider.getValue();
            float newMeanPanicThreshold = (float) meanPanicThresholdSlider.getValue();
            float newPanicVariance = (float) panicVarianceSlider.getValue();
            float newMeanSocialFactor = (float) (meanSocialFactorSlider.getValue() / 100);
            float newSocialVariance = (float) (socialVarianceSlider.getValue() / 100);
            float newFireDamagePerSecond = (float) fireDamagePerSecondSlider.getValue();
            float newFireSpreadInterval = (float) fireSpreadIntervalSlider.getValue();
            float newFireIncubationDelay = (float) fireIncubationDelaySlider.getValue();
            float newSmokeDamagePerSecond = (float) smokeDamagePerSecondSlider.getValue();
            float newSmokeSpreadInterval = (float) smokeSpreadIntervalSlider.getValue();
            float newSmokeInitialDensity = (float) smokeInitialDensitySlider.getValue();
            float newSmokeFadeRatePerSecond = (float) smokeFadeRatePerSecondSlider.getValue();
            float newSmokeDuplicationThreshold = (float) smokeDuplicationThresholdSlider.getValue();

            SimSingletonConfig config = SimSingletonConfig.getInstance();

            config.setInitialEvacueesCount(newCount);
            config.setInitialFireHazardsCount(newFireCount);
            config.setMapFilePath(inputMap.getText());
            config.setLeaderRatio(newLeaderRatio);
            config.setFollowerRatio(newFollowerRatio);
            config.setPanickedRatio(newPanickedRatio);
            config.setMeanBaseSpeed(newMeanBaseSpeed);
            config.setSpeedVariance(newSpeedVariance);
            config.setEvacueeHealth(newEvacueeHealth);
            config.setEvacueeReactionTime(newEvacueeReactionTime);
            config.setEvacueeVisionRadius(newEvacueeVisionRadius);
            config.setMeanPanicThreshold(newMeanPanicThreshold);
            config.setPanicVariance(newPanicVariance);
            config.setMeanSocialFactor(newMeanSocialFactor);
            config.setSocialVariance(newSocialVariance);
            config.setFireDamagePerSecond(newFireDamagePerSecond);
            config.setFireSpreadInterval(newFireSpreadInterval);
            config.setFireIncubationDelay(newFireIncubationDelay);
            config.setSmokeDamagePerSecond(newSmokeDamagePerSecond);
            config.setSmokeSpreadInterval(newSmokeSpreadInterval);
            config.setSmokeInitialDensity(newSmokeInitialDensity);
            config.setSmokeFadeRatePerSecond(newSmokeFadeRatePerSecond);
            config.setSmokeDuplicationThreshold(newSmokeDuplicationThreshold);

        } catch (Exception ex) {
            System.err.println("Błąd wczytywania danych z panelu!");
            return;
        }

        // creates new simulation with new data
        simulation = new Simulation();
        Board board = simulation.getBoard();

        // Scaling tiles to fit available space
        double availableWidth = 900.0;
        double availableHeight = 500.0;
        int dynamicWidth = board.getWidth();
        int dynamicHeight = board.getHeight();

        int calculatedTileSize = (int) Math.min(availableWidth / dynamicWidth, availableHeight / dynamicHeight);
        this.tileSize = Math.max(10, Math.min(40, calculatedTileSize));

        simulationCanvas.setWidth(dynamicWidth * tileSize);
        simulationCanvas.setHeight(dynamicHeight * tileSize);

        gc.clearRect(0, 0, 3000, 3000); // Cleaning the canvas before start

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

                simulation.updateTick(dt);
                render(gc);
            }
        };
        timer.start();
    }

    // pause and reset
    @FXML
    public void onPauseClicked() {
        if (timer != null) {
            timer.stop();
        }
    }

    @FXML
    public void onResetClicked() {
        if (timer != null) {
            timer.stop();
        }
        onStartClicked();   // calls the restart
    }

    private void render(GraphicsContext gc) {
        Board board = simulation.getBoard();
        gc.clearRect(0, 0, board.getWidth() * tileSize, board.getHeight() * tileSize);

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Cell cell = board.getCell(x, y);

                if (cell.getBaseType() == BaseType.WALL) {
                    gc.setFill(Color.BLACK);
                } else if (cell.getBaseType() == BaseType.EXIT) {
                    gc.setFill(Color.GREEN);
                } else if (cell.getBaseType() == BaseType.OBSTACLE) {
                    gc.setFill(Color.BURLYWOOD);
                } else {
                    gc.setFill(Color.LIGHTGRAY);
                }

                if (cell.getDynamicState() == DynamicState.FIRE) {
                    gc.setFill(Color.RED);
                } else if (cell.getDynamicState() == DynamicState.SMOKE) {
                    gc.setFill(Color.DARKGRAY);
                }

                gc.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }

        gc.setFill(Color.BLUE);
        for (Agent agent : simulation.getAgents()) {
            if (agent instanceof Evacuee) {
                double px = agent.getRenderX() * tileSize;
                double py = agent.getRenderY() * tileSize;
                gc.fillOval(px, py, tileSize, tileSize);
            }
        }
    }

    // universal slider configuration
    private void setupSlider(Slider slider, Label label, double min, double max, double startValue, String format) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(startValue);

        // sets starting text to the label
        label.setText(String.format(format, startValue));

        // adds label listener to slider
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            label.setText(String.format(format, newValue.doubleValue()));
        });
    }

    // startup method for evacuees ratio parameters
    private void setupRatioLogic() {
        SimSingletonConfig config = SimSingletonConfig.getInstance();

        // converts ratios to percents
        leaderRatioSlider.setValue(config.getLeaderRatio() * 100);
        followerRatioSlider.setValue(config.getFollowerRatio() * 100);
        panickedRatioSlider.setValue(config.getPanickedRatio() * 100);

        updateRatioLabels();

        // listener connecting with ratio adjusting
        leaderRatioSlider.valueProperty().addListener((obs,
                                                       oldVal, newVal) ->
                                                        adjustRatios(leaderRatioSlider, newVal.doubleValue()));
        followerRatioSlider.valueProperty().addListener((obs,
                                                         oldVal, newVal) ->
                                                        adjustRatios(followerRatioSlider, newVal.doubleValue()));
        panickedRatioSlider.valueProperty().addListener((obs,
                                                         oldVal, newVal) ->
                                                        adjustRatios(panickedRatioSlider, newVal.doubleValue()));
    }

    //  slider balance algorithm
    private void adjustRatios(Slider changedSlider, double newValue) {
        if (isUpdatingRatios) return; // Zabezpieczenie przed pętlą!
        isUpdatingRatios = true;      // Blokujemy drzwi

        Slider s1, s2;
        // actual slider pulling recognizing
        if (changedSlider == leaderRatioSlider) {
            s1 = followerRatioSlider; s2 = panickedRatioSlider;
        } else if (changedSlider == followerRatioSlider) {
            s1 = leaderRatioSlider; s2 = panickedRatioSlider;
        } else {
            s1 = leaderRatioSlider; s2 = followerRatioSlider;
        }

        double remainingSpace = 100.0 - newValue;
        double oldSum = s1.getValue() + s2.getValue();

        // safe value setter for sliders
        if (oldSum == 0) {
            s1.setValue(remainingSpace / 2.0);
            s2.setValue(remainingSpace / 2.0);
        } else {
            // scale proportionally to their current values
            s1.setValue((s1.getValue() / oldSum) * remainingSpace);
            s2.setValue((s2.getValue() / oldSum) * remainingSpace);
        }

        updateRatioLabels();
        isUpdatingRatios = false; // unlock other sliders modifying
    }

    // updates labels
    private void updateRatioLabels() {
        leaderRatioLabel.setText(String.format("%.0f%%", leaderRatioSlider.getValue()));
        followerRatioLabel.setText(String.format("%.0f%%", followerRatioSlider.getValue()));
        panickedRatioLabel.setText(String.format("%.0f%%", panickedRatioSlider.getValue()));
    }
}