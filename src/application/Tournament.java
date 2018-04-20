package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Tournament {
    @FXML
    private Pane pane;
    private int teamSize = 0;
    private int totalRound = 0;
    private List<List<Team>> data = new ArrayList<>();


    public void initialize(String filePath) {
        List<String> lines = loadFile(filePath);
        teamSize = lines.size();
        totalRound = 31 - Integer.numberOfLeadingZeros(teamSize);
        for (int i = 0; i < teamSize; i++) {
            Team team = getTeam(teamSize - 1 + i);
            team.setName(lines.get(shuffle(totalRound, i) - 1));
            team.setVisible(true);

        }
        initializePane();
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

    private void initializePane() {

        pane.setBackground(new Background(new BackgroundImage(
                new Image(teamSize + ".jpg"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(900, 600, false, false, false, false)
        )));
    }


    @FXML
    private void handleLoad(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Team Info File");
        fc.setInitialDirectory(new File("."));
        initialize(fc.showOpenDialog(new Stage()).getPath());
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void handleTeam(ActionEvent event) {
        Team t = (Team) event.getSource();
        //if (!t.completeRound) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Score");
        dialog.setContentText("Score for " + t.getText() + ":");
        // while (true) {
        try {
            dialog.showAndWait().ifPresent(s -> {
                t.setScore(Integer.parseInt(s));
                int index1 = pane.getChildren().indexOf(t);
                int index2 = (index1 % 2 == 0) ? index1 - 1 : index1 + 1;
                Team another = getTeam(index2);
                if (another.getScore() != null) { // TODO:FIXME
                    Team parent = getTeam((index1 - 1) / 2);
                    Team winner = t.compareTo(another) > 0 ? t : another;
                    parent.setName(winner.getName());
                    parent.setVisible(true);
                }


            });
            // break;
        } catch (Exception exc) { }
        //}
        //}
    }

    private Team getTeam(int index) {
        return (Team) pane.getChildren().get(index);
    }

}
