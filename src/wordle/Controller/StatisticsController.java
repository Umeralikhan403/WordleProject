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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wordle.Models.GameResult;
import wordle.Models.GameStatus;
import wordle.Models.Player;
import wordle.Service.Session;
import wordle.Util.AlertUtil;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsController {

    @FXML private Label totalScoreLabel;
    @FXML private Label highScoreLabel;
    @FXML private Label averageScoreLabel;
    @FXML private Label gamesPlayedLabel;
    @FXML private Label winPercentageLabel;

    @FXML private PieChart winLossPieChart;

    @FXML private TableView<GameResult> winsTable;
    @FXML private TableColumn<GameResult, String>  wordColumn;
    @FXML private TableColumn<GameResult, Integer> attemptsColumn;
    @FXML private TableColumn<GameResult, Double>  scoreColumn;

    @FXML
    public void initialize() {
        Player player = Session.getCurrentPlayer();
        if (player == null) {
            // no user logged in: you might redirect back to login
            return;
        }

        List<GameResult> history = player.getHistory();
        double totalScore = player.getTotalScore();
        double highScore  = player.getHighScore();
        double avgScore   = history.isEmpty()
                          ? 0.0
                          : history.stream()
                                   .mapToDouble(GameResult::getScore)
                                   .average()
                                   .orElse(0.0);
        int   gamesPlayed = history.size();
        long  wins        = history.stream()
                                   .filter(r -> r.getStatus() == GameStatus.WON)
                                   .count();
        long  losses      = gamesPlayed - wins;
        double winPct     = gamesPlayed == 0
                          ? 0.0
                          : (wins * 100.0) / gamesPlayed;

        // fill labels
        totalScoreLabel.setText(String.format("%.2f", totalScore));
        highScoreLabel.setText(String.format("%.2f", highScore));
        averageScoreLabel.setText(String.format("%.2f", avgScore));
        gamesPlayedLabel.setText(String.valueOf(gamesPlayed));
        winPercentageLabel.setText(String.format("%.1f%%", winPct));

        // pie chart
        ObservableList<PieChart.Data> pieData =
            FXCollections.observableArrayList(
                new PieChart.Data("Wins", wins),
                new PieChart.Data("Losses", losses)
            );
        winLossPieChart.setData(pieData);

        // table only shows the WON games
        List<GameResult> winsList = history.stream()
            .filter(r -> r.getStatus() == GameStatus.WON)
            .collect(Collectors.toList());

        ObservableList<GameResult> tableItems =
            FXCollections.observableArrayList(winsList);

        wordColumn.setCellValueFactory(
            new PropertyValueFactory<>("word"));
        attemptsColumn.setCellValueFactory(
            new PropertyValueFactory<>("attemptsUsed"));
        scoreColumn.setCellValueFactory(
            new PropertyValueFactory<>("score"));

        winsTable.setItems(tableItems);
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
