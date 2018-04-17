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

    private List<Team> teamList = new ArrayList<>();

    @FXML
    private void loadTeamInfo(ActionEvent event) {
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Choose Team Info File");
            fc.setInitialDirectory(new File("."));
            List<String> lines = Files.readAllLines(fc.showOpenDialog(new Stage()).toPath());
            int size = lines.size();

            for (int i = 0; i < size / 2; i++) {
                teamList.add(new Team(lines.get(i)));
                teamList.add(new Team(lines.get(size - 1 - i)));
            }

            for (int i = 0; i < size; i++) {
                Team team = new Team(teamList.get(i).getName());
                team.setLayoutX(50 + ((i < size / 2) ? 0 : 750));
                team.setLayoutY(30 + 60 * (i % (size / 2)));
                team.setOnMouseClicked(e -> {
                    ((Team) e.getTarget()).getText();
                });
                pane.getChildren().add(team);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }
}
