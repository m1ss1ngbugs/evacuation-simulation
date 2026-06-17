package evacuation.sim.gui;

import evacuation.sim.SimSingletonConfig;
import evacuation.sim.agent.Agent;
import evacuation.sim.agent.human.Evacuee;
import evacuation.sim.core.Simulation;
import evacuation.sim.core.Statistics;
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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.File;


/**
 * JavaFX view controller responsible for handling the GUI.
 * Implements the Controller layer in the MVC design pattern.
 * Acting as a bridge between the view defined in the FXML file and the simulation's logic ({@link Simulation}).
 * Manage the main simulation time loop using {@link AnimationTimer}.
 * Retrieving and validating parameters from the user, then updating {@link SimSingletonConfig}.
 * Cyclic rendering of the board state, agents, and threats on the {@link Canvas} object.
 * Presenting statistics and generating a heatmap after the evacuation is complete.
 * @author Heorhii Yartsev (293562)
 */
public class SimulationController {

    // dynamic size of the cell on the canvas
    private int tileSize = 20;

    @FXML private Canvas simulationCanvas;
    @FXML private TextField inputMap;
    @FXML private Pane canvasContainer;
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
    @FXML private Label smokeDuplicationThreasholdLabel;
    // bottom panel statistics
    @FXML private Label timeLabel;
    @FXML private Label savedCountLabel;
    @FXML private Label fireCasualtiesLabel;
    @FXML private Label smokeCasualtiesLabel;
    @FXML private Label survivalRateLabel;
    @FXML private Label scenarioLabel;
    @FXML private Label FirstEvacuationTimeLabel;
    @FXML private Label TotalEvacuationTimeLabel;
    // time scale slider and label
    @FXML private Slider timeScaleSlider;
    @FXML private Label timeScaleLabel;

    private Simulation simulation;
    private GraphicsContext gc;
    private AnimationTimer timer;

    /**
     * Method automatically called by the JavaFX framework after injecting FXML elements.
     * Initializes the canvas graphics context and configures all sliders.
     * and labels based on default values retrieved from {@link SimSingletonConfig}.
     * Also establishes listeners for dynamic agent aspect ratio balancing.
     */
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
        setupSlider(smokeDuplicationThresholdSlider, smokeDuplicationThreasholdLabel,
                5, 30, config.getSmokeDuplicationThreshold(), "%.0f");
        // simulation time scale label and slider initialization
        setupSlider(timeScaleSlider, timeScaleLabel,
                0.1, 5.0, 1.0, "%.1fx");

        // text fields
        inputMap.setText(config.getMapFilePath());
    }

    /**
     * Handles the map selection button click event.
     * Opens a system dialog allowing the user to select a text file
     * containing the map layout, filtering only files with the .txt extension.
     */
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

    /**
     * Starts or resumes the simulation loop.
     * If this is the first run, then creates a new simulation world and initializes the {@link AnimationTimer}.
     * Calculates the frame time - delta time, taking into account the timescale slider,
     * invokes agent update logic and refreshes the graphical interface.
     */
    @FXML
    public void onStartClicked() {
        if (timer != null) timer.stop();

        if (updateConfigFromUI()) return;

        // creates new simulation with new data
        if (simulation == null) {
            setupSimulationWorld();
        }

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

                // time temp modification
                float timeMultiplier = (float) timeScaleSlider.getValue();
                dt = dt * timeMultiplier;
                // max dt limit
                if (dt > 0.1f) dt = 0.1f;

                simulation.updateTick(dt);
                render(gc);

                updateStatisticsUI();

                // simulations end check
                Statistics stats = simulation.getStats();
                int initialCount = SimSingletonConfig.getInstance().getInitialEvacueesCount();
                int currentResolved = stats.getSavedCount() + stats.getCasualtiesFire() + stats.getCasualtiesSmoke();

                if (currentResolved >= initialCount) {
                    this.stop(); // stops timer
                    System.out.println("Ewakuacja zakończona! Wszyscy agenci opuścili planszę lub zginęli.");

                    onShowHeatmapClicked();
                }
            }
        };
        timer.start();
    }

    /**
     * Pauses the main simulation loop.
     * Stops the {@link AnimationTimer} object, which freezes the state of the agents and board,
     * but allows the simulation to be resumed later without losing progress.
     */
    @FXML
    public void onPauseClicked() {
        if (timer != null) {
            timer.stop();
        }
    }

    /**
     * Resets the entire simulation to its initial state.
     * Stops the clock, loads the latest settings from the control panel (sliders)
     * and creates a new, clean simulation environment.
     */
    @FXML
    public void onResetClicked() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        
        if (updateConfigFromUI()) return;

        setupSimulationWorld();
    }

    /**
     * Displays a heatmap on the canvas after the simulation is complete.
     * This method gets the generated color matrix from the {@link Statistics} object and applies it
     * to a grid of tiles, visualizing traffic density in specific areas of the building.
     */
    @FXML
    public void onShowHeatmapClicked() {
        // stops the simulation (in case we click this button during simulation going on)
        if (timer != null) {
            timer.stop();
        }

        if (simulation == null || simulation.getStats() == null) {
            return; // no data for heatmap generation
        }

        // gets the color matrix from the Statistics
        Color[][] heatmapColors = simulation.getStats().generateHeatmap(simulation.getBoard());
        int height = heatmapColors.length;
        int width = heatmapColors[0].length;

        // cleans up the canvas before putting there a heatmap
        gc.clearRect(0, 0, simulationCanvas.getWidth(), simulationCanvas.getHeight());

        // draws heatmap cells
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color cellColor = heatmapColors[y][x];
                if (cellColor != null) {
                    gc.setFill(cellColor);
                    gc.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                }
            }
        }

        System.out.println("Wygenerowano i wyświetlono Heatmapę!");
    }

    /**
     * Initializes a new instance of the simulation engine and prepares the canvas.
     * Gets the dimensions of the loaded map and dynamically recalculates the size of a single tile
     * ({@code tileSize}) to optimally fit the entire board to the available window size.
     */
    private void setupSimulationWorld() {
        simulation = new Simulation();
        Board board = simulation.getBoard();

        double availableWidth = canvasContainer.getWidth();
        double availableHeight = canvasContainer.getHeight();
        
        if (availableWidth == 0) availableWidth = 900.0;
        if (availableHeight == 0) availableHeight = 500.0;

        int dynamicWidth = board.getWidth();
        int dynamicHeight = board.getHeight();

        int calculatedTileSize = (int) Math.min(availableWidth / dynamicWidth, availableHeight / dynamicHeight);
        this.tileSize = Math.max(10, Math.min(40, calculatedTileSize));

        simulationCanvas.setWidth(dynamicWidth * tileSize);
        simulationCanvas.setHeight(dynamicHeight * tileSize);

        gc.clearRect(0, 0, 3000, 3000);

        render(gc);

        clearStatisticsUI();
    }

    /**
     * Main drawing function (Render Loop).
     * Clears the canvas, then iterates through all the board tiles and active agents,
     * assigning them appropriate colors and drawing them on the {@link Canvas} object.
     * @param gc graphics context (GraphicsContext2D) assigned to the main canvas (the brush).
     */
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

    /**
     * Periodically updates the bottom information panel in the graphical interface.
     * Downloads the {@link Statistics} object from the engine and refreshes the text labels (Labels)
     * with data including time, number of agents saved, and number of victims.
     */
    private void updateStatisticsUI() {
        if (simulation == null || simulation.getStats() == null) return;

        Statistics stats = simulation.getStats();

        timeLabel.setText(String.format("%.1f s", simulation.getCurrentTime()));
        savedCountLabel.setText("" + stats.getSavedCount());
        fireCasualtiesLabel.setText("" + stats.getCasualtiesFire());
        smokeCasualtiesLabel.setText("" + stats.getCasualtiesSmoke());
        survivalRateLabel.setText(String.format("%.1f%%", stats.calculateSurvivalRate() * 100));
        scenarioLabel.setText(stats.determineFinalScenario());
        FirstEvacuationTimeLabel.setText(String.format("%.1f s", stats.getFirstEvacuationTime()));
        TotalEvacuationTimeLabel.setText(String.format("%.1f s", stats.getTotalEvacuationTime()));
    }

    /**
     * Resets all text values in the lower statistics panel,
     * preparing the interface for a clean, new simulation run.
     */
    private void clearStatisticsUI() {
    if (timeLabel != null) timeLabel.setText("0.0 s");
    if (savedCountLabel != null) savedCountLabel.setText("0");
    if (fireCasualtiesLabel != null) fireCasualtiesLabel.setText("0");
    if (smokeCasualtiesLabel != null) smokeCasualtiesLabel.setText("0");
    if (survivalRateLabel != null) survivalRateLabel.setText("0.0%");
    if (scenarioLabel != null) scenarioLabel.setText("Unavailable");
    if (FirstEvacuationTimeLabel != null) FirstEvacuationTimeLabel.setText("0.0 s");
    if (TotalEvacuationTimeLabel != null) TotalEvacuationTimeLabel.setText("0.0 s");
}

    /**
     * A general-purpose helper method for initializing sliders (Sliders).
     * Sets the range of acceptable values, the starting position, and attaches a listener
     * that automatically refreshes the assigned text label with each slider.
     *
     * @param slider the slider object to configure.
     * @param label the text label displaying the current slider value.
     * @param min the minimum value on the scale.
     * @param max the maximum value on the scale.
     * @param startValue the initial value of the slider retrieved from the configuration.
     * @param format a formatting string (e.g., "%.1f") specifying the appearance of the number.
     */
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

    /**
     * Initializes the interconnection mechanism for the sliders defining the percentage distribution of the agents'
     * psychological roles (Leader, Follower, Panicker).
     */
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

    /**
     * Implements an algorithm for automatically balancing agent role ratios.
     * Ensures that, regardless of user movements, the sum of the values of the three main
     * sliders will always be exactly 100%. Blocks recursive event loops
     * using the {@code isUpdatingRatios} flag.
     *
     * @param changedSlider the slider that has currently been moved by the user.
     * @param newValue the new value set on the moving slider.
     */
    private void adjustRatios(Slider changedSlider, double newValue) {
        if (isUpdatingRatios) return; 
        isUpdatingRatios = true;      

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

    /**
     * A short helper method that formats role slider values to percentages.
     */
    private void updateRatioLabels() {
        leaderRatioLabel.setText(String.format("%.0f%%", leaderRatioSlider.getValue()));
        followerRatioLabel.setText(String.format("%.0f%%", followerRatioSlider.getValue()));
        panickedRatioLabel.setText(String.format("%.0f%%", panickedRatioSlider.getValue()));
    }

    /**
     * Collects all values from interface controls (sliders, text fields) and saves
     * them to the global template {@link SimSingletonConfig}.
     * Acts as a validation layer before each simulation start or reset.
     *
     * @return {@code false} if the configuration was saved successfully;
     * {@code true} if an exception occurred (displays an error message - Alert)
     */
    private boolean updateConfigFromUI() {
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
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Configuration error");
            alert.setHeaderText("Simulation cannot be started.");
            alert.setContentText("Check whether the data in the side panel is correct.");
            alert.showAndWait();
            return true;
        }
        return false;
    }
}