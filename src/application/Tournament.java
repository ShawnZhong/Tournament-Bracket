package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    private int round = 0;
    private List<List<Team>> data = new ArrayList<>();

    @FXML
    private void loadTeamInfo(ActionEvent event) {
        pane.getChildren().clear();
        initialize(loadFile());
        render(round);
    }

    private void initialize(List<String> lines) {
        int teamSize = lines.size();

        for (int i = 0; i < Math.log(teamSize) / Math.log(2) + 1; i++)
            data.add(new ArrayList<>());

        for (int i = 0; i < teamSize / 2; i++) {
            data.get(0).add(new Team(lines.get(i)));
            data.get(0).add(new Team(lines.get(teamSize - 1 - i)));
        }
    }

    private void render(int round) {
        int size = data.get(round).size();
        for (int i = 0; i < size; i++) {
            int x = 50 + ((i < size / 2) ? 0 : 750);
            int y = 30 + 60 * (i % (size / 2));
            pane.getChildren().add(data.get(round).get(i).setLoc(x, y));
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
        try {
            for (int i = 0; i < data.get(round).size() / 2; i++) {
                Team t1 = data.get(round).get(2 * i + 1);
                Team t2 = data.get(round).get(2 * i);
                data.get(++round).add(t1.compareTo(t2) > 0 ? t1 : t2);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Balabala");
            alert.showAndWait();
        }
        render(round);
    }

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }
}
