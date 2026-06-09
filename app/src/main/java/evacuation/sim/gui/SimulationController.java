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
    @FXML private Slider initialEvacueesCountSlider;
    @FXML private Label initialEvacueesCountLabel;
    @FXML private Slider initialFireHazardsCountSlider;
    @FXML private Label initialFireHazardsCountLabel;
    // Roles sliders and labels
    @FXML private Slider leaderRatioSlider;
    @FXML private Label leaderRatioLabel;
    @FXML private Slider followerRatioSlider;
    @FXML private Label followerRatioLabel;
    @FXML private Slider panickedRatioSlider;
    @FXML private Label panickedRatioLabel;
    // blocks StackOverFlowError when slider moving
    private boolean isUpdatingRatios = false;
    @FXML private Slider meanBaseSpeedSlider;
    @FXML private Label meanBaseSpeedLabel;

    private Simulation simulation;
    private GraphicsContext gc;
    private AnimationTimer timer;

    @FXML
    public void initialize() {
        gc = simulationCanvas.getGraphicsContext2D();
        SimSingletonConfig config = SimSingletonConfig.getInstance();

        // Initial evacuee count slider
        setupSlider(initialEvacueesCountSlider, initialEvacueesCountLabel,
                1, 100, config.getInitialEvacueesCount(), "%.0f");
        // Initial fire hazards count slider
        setupSlider(initialFireHazardsCountSlider, initialFireHazardsCountLabel,
                0, 10, config.getInitialFireHazardsCount(), "%.0f");
        // Setups ratio logic for ratio sliders
        setupRatioLogic();
        // Initial meanBaseSpeed slider
        setupSlider(meanBaseSpeedSlider, meanBaseSpeedLabel,
                0.5, 3, config.getMeanBaseSpeed(), "%.1f");

        // Pola tekstowe
        inputMap.setText(config.getMapFilePath());
    }

    // --- WYBIERANIE PLIKU ---
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

    // --- URUCHAMIANIE I SKALOWANIE ---
    @FXML
    public void onStartClicked() {
        if (timer != null) timer.stop();

        try {
            // Aktualizacja Singletona danymi z okienka przed startem
            int newCount = (int) initialEvacueesCountSlider.getValue();
            int newFireCount = (int) initialFireHazardsCountSlider.getValue();
            int newLeaderRatio = (int) (leaderRatioSlider.getValue() / 100);
            int newFollowerRatio = (int) (followerRatioSlider.getValue() / 100);
            int newPanickedRatio = (int) (panickedRatioSlider.getValue() / 100);
            int newMeanBaseSpeed = (int) meanBaseSpeedSlider.getValue();

            SimSingletonConfig.getInstance().setInitialEvacueesCount(newCount);
            SimSingletonConfig.getInstance().setInitialFireHazardsCount(newFireCount); // ZAPISUJEMY POŻARY
            SimSingletonConfig.getInstance().setMapFilePath(inputMap.getText());
            SimSingletonConfig.getInstance().setLeaderRatio(newLeaderRatio);
            SimSingletonConfig.getInstance().setFollowerRatio(newFollowerRatio);
            SimSingletonConfig.getInstance().setPanickedRatio(newPanickedRatio);
            SimSingletonConfig.getInstance().setMeanBaseSpeed(newMeanBaseSpeed);
        } catch (Exception ex) {
            System.err.println("Błąd wczytywania danych z panelu!");
            return;
        }

        // Tworzymy nową symulację z nowymi danymi
        simulation = new Simulation();
        Board board = simulation.getBoard();

        // Skalowanie kafelków do dostępnego miejsca
        double availableWidth = 900.0;
        double availableHeight = 500.0;
        int dynamicWidth = board.getWidth();
        int dynamicHeight = board.getHeight();

        int calculatedTileSize = (int) Math.min(availableWidth / dynamicWidth, availableHeight / dynamicHeight);
        this.tileSize = Math.max(10, Math.min(40, calculatedTileSize));

        simulationCanvas.setWidth(dynamicWidth * tileSize);
        simulationCanvas.setHeight(dynamicHeight * tileSize);

        // Oczyszczenie płótna przed startem
        gc.clearRect(0, 0, 3000, 3000);

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

    // --- LOGIKA KOLEGI: PAUZA I RESET ---
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
        // Wywołujemy Start, który ponownie czyta dane z okienka i tworzy symulację na nowo
        onStartClicked();
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

    // Uniwersalny konfigurator suwaków
    private void setupSlider(Slider slider, Label label, double min, double max, double startValue, String format) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(startValue);

        // Ustawienie tekstu startowego
        label.setText(String.format(format, startValue));

        // Dodanie nasłuchiwacza zmian na żywo
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            label.setText(String.format(format, newValue.doubleValue()));
        });
    }

    // Metoda startowa do wywołania w initialize()
    private void setupRatioLogic() {
        SimSingletonConfig config = SimSingletonConfig.getInstance();

        // Mnożymy * 100, bo w configu masz ułamki (np. 0.15), a na ekranie chcemy 15%
        leaderRatioSlider.setValue(config.getLeaderRatio() * 100);
        followerRatioSlider.setValue(config.getFollowerRatio() * 100);
        panickedRatioSlider.setValue(config.getPanickedRatio() * 100);

        updateRatioLabels();

        // Podpinamy listenery. Kiedy ruszasz jednym, wywołuje się nasza metoda matematyczna
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

    // Algorytm balansu suwaków
    private void adjustRatios(Slider changedSlider, double newValue) {
        if (isUpdatingRatios) return; // Zabezpieczenie przed pętlą!
        isUpdatingRatios = true;      // Blokujemy drzwi

        Slider s1, s2;
        // Rozpoznajemy, który suwak użytkownik właśnie ciągnie
        if (changedSlider == leaderRatioSlider) {
            s1 = followerRatioSlider; s2 = panickedRatioSlider;
        } else if (changedSlider == followerRatioSlider) {
            s1 = leaderRatioSlider; s2 = panickedRatioSlider;
        } else {
            s1 = leaderRatioSlider; s2 = followerRatioSlider;
        }

        double remainingSpace = 100.0 - newValue;
        double oldSum = s1.getValue() + s2.getValue();

        // Bezpieczny podział reszty
        if (oldSum == 0) {
            // Jeśli oba były na zerze, dzielimy po równo
            s1.setValue(remainingSpace / 2.0);
            s2.setValue(remainingSpace / 2.0);
        } else {
            // Skalujemy proporcjonalnie do ich dotychczasowych wartości
            s1.setValue((s1.getValue() / oldSum) * remainingSpace);
            s2.setValue((s2.getValue() / oldSum) * remainingSpace);
        }

        updateRatioLabels();
        isUpdatingRatios = false; // Odblokowujemy drzwi
    }

    // Aktualizuje teksty obok suwaków (dodając znak procenta)
    private void updateRatioLabels() {
        leaderRatioLabel.setText(String.format("%.0f%%", leaderRatioSlider.getValue()));
        followerRatioLabel.setText(String.format("%.0f%%", followerRatioSlider.getValue()));
        panickedRatioLabel.setText(String.format("%.0f%%", panickedRatioSlider.getValue()));
    }
}