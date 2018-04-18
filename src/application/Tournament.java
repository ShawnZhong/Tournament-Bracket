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
        int totalRound = (int)(Math.log(teamSize) / Math.log(2));

        for (int i = 0; i < totalRound+1; i++)
            data.add(new ArrayList<>());

        for (int i = 0; i < teamSize; ++i)
        	data.get(0).add(new Team(lines.get(arrange(totalRound, i)-1)));
    }
    
    private int arrange(int n, int k) {
    	if(n == 0) return 1;
    	if(k%2 == 1)
			return (int)Math.pow(2,n)+1 - arrange(n-1, k/2);
		else
			return arrange(n-1, k/2);
    }

    private void render(int round) {
        int size = data.get(round).size();
        for (int i = 0; i < size; i++) {
            int x = 450 + (((i < size / 2) ? -400 + round * 100 : 350 - round * 100));
            int y = 30 + (60) * (i % (size / 2));
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
                data.get(round + 1).add(t1.compareTo(t2) > 0 ? t1.clone() : t2.clone());
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Balabala");	//Change message here
            alert.showAndWait();
        }
        render(++round);
    }

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }
}
