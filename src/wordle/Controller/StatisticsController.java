package wordle.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wordle.Models.GameResult;
import wordle.Models.GameStatus;
import wordle.Models.GameType;
import wordle.Models.Player;
import wordle.Service.Session;
import wordle.Util.AlertUtil;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsController {

    @FXML private TabPane gameTypeTabs;
    @FXML private Tab wordleTab;
    @FXML private Tab nerdleTab;
    
    // Wordle Tab
    @FXML private Label totalScoreWordleLabel;
    @FXML private Label highScoreWordleLabel;
    @FXML private Label averageScoreWordleLabel;
    @FXML private Label gamesPlayedWordleLabel;
    @FXML private Label winPercentageWordleLabel;
    @FXML private PieChart winLossPieChartWordle;
    @FXML private TableView<GameResult> winsTableWordle;
    @FXML private TableColumn<GameResult, String> wordColumnWordle;
    @FXML private TableColumn<GameResult, Integer> attemptsColumnWordle;
    @FXML private TableColumn<GameResult, Double> scoreColumnWordle;
    
    // Nerdle Tab
    @FXML private Label totalScoreNerdleLabel;
    @FXML private Label highScoreNerdleLabel;
    @FXML private Label averageScoreNerdleLabel;
    @FXML private Label gamesPlayedNerdleLabel;
    @FXML private Label winPercentageNerdleLabel;
    @FXML private PieChart winLossPieChartNerdle;
    @FXML private TableView<GameResult> winsTableNerdle;
    @FXML private TableColumn<GameResult, String> equationColumnNerdle;
    @FXML private TableColumn<GameResult, Integer> attemptsColumnNerdle;
    @FXML private TableColumn<GameResult, Double> scoreColumnNerdle;

    @FXML
    public void initialize() {
        Player player = Session.getCurrentPlayer();
        if (player == null) {
            // no user logged in: you might redirect back to login
            return;
        }

        List<GameResult> allHistory = player.getHistory();
        
        // Separate histories by game type
        List<GameResult> wordleHistory = allHistory.stream()
            .filter(result -> result.getGameType() == null || result.getGameType() == GameType.WORDLE)
            .collect(Collectors.toList());
            
        List<GameResult> nerdleHistory = allHistory.stream()
            .filter(result -> result.getGameType() == GameType.NERDLE)
            .collect(Collectors.toList());
            
        // Initialize both tabs
        initializeWordle(player, wordleHistory);
        initializeNerdle(player, nerdleHistory);
    }
    
    private void initializeWordle(Player player, List<GameResult> history) {
        // Calculate statistics
        double totalScore = history.stream()
                .mapToDouble(GameResult::getScore)
                .sum();
                
        double highScore = history.isEmpty() ? 0.0 : 
                history.stream()
                .mapToDouble(GameResult::getScore)
                .max().orElse(0.0);
                
        double avgScore = history.isEmpty() ? 0.0 :
                history.stream()
                .mapToDouble(GameResult::getScore)
                .average().orElse(0.0);
                
        int gamesPlayed = history.size();
        
        long wins = history.stream()
                .filter(r -> r.getStatus() == GameStatus.WON)
                .count();
                
        long losses = gamesPlayed - wins;
        
        double winPct = gamesPlayed == 0 ? 0.0 : (wins * 100.0) / gamesPlayed;

        // Fill labels
        totalScoreWordleLabel.setText(String.format("%.2f", totalScore));
        highScoreWordleLabel.setText(String.format("%.2f", highScore));
        averageScoreWordleLabel.setText(String.format("%.2f", avgScore));
        gamesPlayedWordleLabel.setText(String.valueOf(gamesPlayed));
        winPercentageWordleLabel.setText(String.format("%.1f%%", winPct));

        // Pie chart
        ObservableList<PieChart.Data> pieData =
            FXCollections.observableArrayList(
                new PieChart.Data("Wins", wins),
                new PieChart.Data("Losses", losses)
            );
        winLossPieChartWordle.setData(pieData);

        // Table only shows the WON games
        List<GameResult> winsList = history.stream()
            .filter(r -> r.getStatus() == GameStatus.WON)
            .collect(Collectors.toList());

        ObservableList<GameResult> tableItems =
            FXCollections.observableArrayList(winsList);

        wordColumnWordle.setCellValueFactory(new PropertyValueFactory<>("word"));
        attemptsColumnWordle.setCellValueFactory(new PropertyValueFactory<>("attemptsUsed"));
        scoreColumnWordle.setCellValueFactory(new PropertyValueFactory<>("score"));

        winsTableWordle.setItems(tableItems);
    }
    
    private void initializeNerdle(Player player, List<GameResult> history) {
        // Calculate statistics
        double totalScore = history.stream()
                .mapToDouble(GameResult::getScore)
                .sum();
                
        double highScore = history.isEmpty() ? 0.0 : 
                history.stream()
                .mapToDouble(GameResult::getScore)
                .max().orElse(0.0);
                
        double avgScore = history.isEmpty() ? 0.0 :
                history.stream()
                .mapToDouble(GameResult::getScore)
                .average().orElse(0.0);
                
        int gamesPlayed = history.size();
        
        long wins = history.stream()
                .filter(r -> r.getStatus() == GameStatus.WON)
                .count();
                
        long losses = gamesPlayed - wins;
        
        double winPct = gamesPlayed == 0 ? 0.0 : (wins * 100.0) / gamesPlayed;

        // Fill labels  
        totalScoreNerdleLabel.setText(String.format("%.2f", totalScore));
        highScoreNerdleLabel.setText(String.format("%.2f", highScore));
        averageScoreNerdleLabel.setText(String.format("%.2f", avgScore));
        gamesPlayedNerdleLabel.setText(String.valueOf(gamesPlayed));
        winPercentageNerdleLabel.setText(String.format("%.1f%%", winPct));

        // Pie chart
        ObservableList<PieChart.Data> pieData =
            FXCollections.observableArrayList(
                new PieChart.Data("Wins", wins),
                new PieChart.Data("Losses", losses)
            );
        winLossPieChartNerdle.setData(pieData);

        // Table only shows the WON games
        List<GameResult> winsList = history.stream()
            .filter(r -> r.getStatus() == GameStatus.WON)
            .collect(Collectors.toList());

        ObservableList<GameResult> tableItems =
            FXCollections.observableArrayList(winsList);

        equationColumnNerdle.setCellValueFactory(new PropertyValueFactory<>("word"));
        attemptsColumnNerdle.setCellValueFactory(new PropertyValueFactory<>("attemptsUsed"));
        scoreColumnNerdle.setCellValueFactory(new PropertyValueFactory<>("score"));

        winsTableNerdle.setItems(tableItems);
    }
    
    @FXML
    private void handleBack(ActionEvent evt) {
        try {
            Parent menuRoot = FXMLLoader.load(
                getClass().getResource("/wordle/View/MenuView.fxml")
            );
            Stage stage = (Stage)((Node) evt.getSource()).getScene().getWindow();
            stage.setScene(new Scene(menuRoot));
            stage.setTitle("Wordle — Choose Game");
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.warn("Navigation Error", "Could not return to the menu.");
        }
    }
}