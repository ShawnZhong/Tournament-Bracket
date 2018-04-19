package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    private static Tournament self = null;
    @FXML
    private Pane pane;
    private int teamSize = 0;
    private int totalRound = 0;
    private List<List<Team>> data = new ArrayList<>();

    public Tournament() {
        self = this;
    }

    public static Tournament getController() {
        return self;
    }

    public void initialize(String filePath) {
        List<String> lines = loadFile(filePath);
        teamSize = lines.size();
        totalRound = 31 - Integer.numberOfLeadingZeros(teamSize);
        initializeData(lines);
        initializePane();
        render();
    }

    private List<String> loadFile(String filepath) {
        try {
            return Files.readAllLines(Paths.get(filepath));
        } catch (IOException e) {
            new Alert(Alert.AlertType.WARNING, "File not found, use demo data instead").showAndWait();
            return IntStream.range(1, 17).mapToObj(i -> "team " + i).collect(Collectors.toList());
        }
    }

    private void initializeData(List<String> lines) {
        data.clear();
        for (int i = 0; i < totalRound + 1; i++)
            data.add(new ArrayList<>());

        for (int i = 0; i < teamSize; i++)
            data.get(0).add(new Team(lines.get(shuffle(totalRound, i) - 1)));

        for (int i = 1; i < totalRound + 1; i++)
            for (int j = 0; j < (1 << (totalRound - i)); j++)
                data.get(i).add(null);
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


    private void render() {
        int index = 0;
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).size(); j++) {
                Team team = data.get(i).get(j);
                index++;
                if (team == null) continue;

                Team old = (Team) pane.getChildren().get(index - 1);

                team.setLayoutX(old.getLayoutX());
                team.setLayoutY(old.getLayoutY());

                pane.getChildren().set(index - 1, team);
                System.out.println((index - 1) + team.toString());
            }
        }
    }

    public void handleInput() {
        for (int i = 1; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).size(); j++) {
                if (data.get(i).get(j) != null) continue;
                Team t1 = data.get(i - 1).get(2 * j);
                Team t2 = data.get(i - 1).get(2 * j + 1);
                if (t1 == null || t2 == null || t1.getScore() == null || t2.getScore() == null ||
                        t1.getScore().equals(t2.getScore())) continue;
                data.get(i).set(j, t1.compareTo(t2) > 0 ? t1.clone() : t2.clone());
                t1.completeRound();
                t2.completeRound();
            }
        }
        render();
    }

    @FXML
    private void handleLoad(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Team Info File");
        fc.setInitialDirectory(new File("."));
        try {
        	initialize(fc.showOpenDialog(new Stage()).getPath());
        }catch(Exception e) {}
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }
}
