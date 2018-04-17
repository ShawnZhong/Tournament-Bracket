package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Tournament {
    @FXML
    private Pane pane;

    private int size;

    private List<List<Team>> round = new ArrayList<>();

    @FXML
    private void loadTeamInfo(ActionEvent event) {
        pane.getChildren().clear();
        initialize(loadFile());
        render();
    }

    private void initialize(List<String> lines) {
        size = lines.size();

        for (int i = 0; i < size; i++)
            round.add(new ArrayList<>());


        List<Team> firstRound = round.get(0);

        for (int i = 0; i < size; i++) {
            firstRound.add(new Team(lines.get(i)));
            firstRound.add(new Team(lines.get(size - 1 - i)));
        }
    }

    private void render() {
        for (int i = 0; i < size; i++) {
            Team team = new Team(round.get(0).get(i).getName());
            team.setLayoutX(50 + ((i < size / 2) ? 0 : 750));
            team.setLayoutY(30 + 60 * (i % (size / 2)));
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
