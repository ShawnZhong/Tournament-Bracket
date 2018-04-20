package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Tournament {
    private Pane pane;
    @FXML
    private Pane pane16;
    @FXML
    private Pane pane8;
    @FXML
    private Pane pane4;
    @FXML
    private Pane pane2;
    @FXML
    private Pane pane1;

    public void initialize(String filePath) {
        List<String> lines = loadFile(filePath);
        int teamSize = lines.size();

        switch (teamSize) {
            case (16):
                pane = pane16;
                break;
            case (8):
                pane = pane8;
                break;
            case (4):
                pane = pane4;
                break;
            case (2):
                pane = pane2;
                break;
            case (1):
                pane = pane1;
                break;
            default:
                pane = pane16;
                teamSize = 16;
                new Alert(Alert.AlertType.WARNING, "Team size not supported").showAndWait();
                break;
        }
        pane.setVisible(true);

        for (Node node : pane.getChildren()) {
            Team team = (Team) node;
            team.setVisible(false);
            team.setDisable(false);
            team.setCompleteRound(false);
        }

        int totalRound = 31 - Integer.numberOfLeadingZeros(teamSize);
        for (int i = 0; i < teamSize; i++) {
            Team team = getTeam(teamSize - 1 + i);
            team.setName(lines.get(shuffle(totalRound, i) - 1));
            team.setVisible(true);
        }
    }

    private List<String> loadFile(String filepath) {
        try {
            return Files.readAllLines(Paths.get(filepath));
        } catch (IOException e) {
            new Alert(Alert.AlertType.WARNING, "File not found, use demo data instead").showAndWait();
            return IntStream.range(1, 17).mapToObj(i -> "team " + i).collect(Collectors.toList());
        }
    }


    private int shuffle(int n, int k) {
        return n == 0 ? 1 : k % 2 == 1 ? (1 << n) + 1 - shuffle(n - 1, k / 2) : shuffle(n - 1, k / 2);
    }


    @FXML
    public void handleTeam(ActionEvent event) {
        Team team = (Team) event.getSource();

        if (team.equals(getTeam(0))) {
            new Alert(Alert.AlertType.WARNING, team.getName() + " wins!!!").showAndWait();
            return;
        }

        if (!team.isCompleteRound()) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Input Score");
            dialog.setContentText("Score for " + team.getName() + ": ");
            while (true) {
                try {
                    dialog.showAndWait().ifPresent(s -> {
                        double score = Double.parseDouble(s);
                        if (score < 0) throw new InputMismatchException();
                        team.setScore(score);
                        compareScore(team);
                    });
                    break;
                } catch (Exception e) {
                    new Alert(Alert.AlertType.WARNING, "Invalid input, please try again").showAndWait();
                }
            }
        }
    }

    private void compareScore(Team team1) {
        int index1 = pane.getChildren().indexOf(team1);
        Team team2 = getTeam((index1 % 2 == 0) ? index1 - 1 : index1 + 1);
        if (team2 != null && team2.getScore() != null) {
            Team parent = getTeam((index1 - 1) / 2);

            if (team1.compareTo(team2) == 0) {
                new Alert(Alert.AlertType.WARNING, team1.getName() + " and " + team2.getName() + " tie!"
                        + "\r\nStart another game! ").showAndWait();
                team1.setScore(null);
                team2.setScore(null);
                return;
            }

            parent.setName(team1.compareTo(team2) > 0 ? team1.getName() : team2.getName());
            parent.setVisible(true);
            team1.setCompleteRound(true);
            team2.setCompleteRound(true);
            if (parent.equals(getTeam(0)))
                new Alert(Alert.AlertType.WARNING, parent.getName() + " wins!!!").showAndWait();
        }
    }

    private Team getTeam(int index) { return (Team) pane.getChildren().get(index); }

    @FXML
    private void handleLoad(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Team Info File");
        fc.setInitialDirectory(new File("."));
        initialize(fc.showOpenDialog(new Stage()).getPath());
    }

    @FXML
    private void handleExit(ActionEvent event) { System.exit(0); }
}
