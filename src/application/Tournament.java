package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Tournament {
    @FXML
    private Pane pane;

    private int teamSize;

    private Team[][] teams;

    @FXML
    private void loadTeamInfo(ActionEvent event) {
        pane.getChildren().clear();
        initialize(loadFile());
        render();
    }

    private void initialize(List<String> lines) {
        teamSize = lines.size();
        teams = new Team[(int) (Math.log(teamSize) / Math.log(2)) + 1][];
        for (int i = 0; i < teams.length; i++) {
            teams[teams.length - i - 1] = new Team[(int) Math.pow(2, i)];
        }

        for (int i = 0; i < teams[0].length; i++) {
            if (i % 2 == 0)
                teams[0][i] = new Team(lines.get(i));
            else
                teams[0][teams[0].length - i] = new Team(lines.get(i));
        }
    }

    private void render() {
        for (int i = 0; i < teamSize; i++) {
            Team team = new Team(teams[0][i].getName());
            team.setLayoutX(50 + ((i < teamSize / 2) ? 0 : 750));
            team.setLayoutY(30 + 60 * (i % (teamSize / 2)));
            team.setOnMouseClicked(e -> {
                Team t = (Team) e.getSource();
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Input Score");
                dialog.setContentText("Score for " + t.getText() + ":");
                dialog.showAndWait().ifPresent(s -> t.setScore(Integer.parseInt(s)));
            });
            pane.getChildren().add(team);
        }
    }

    private List<String> loadFile() {
        while (true)
            try {
                FileChooser fc = new FileChooser();
                fc.setTitle("Choose Team Info File");
                fc.setInitialDirectory(new File("."));
                return Files.readAllLines(fc.showOpenDialog(new Stage()).toPath());
            } catch (IOException e) { }
    }


    @FXML
    private void nextRound() {

        // Add code for comparison

        render();
    }

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }
}
