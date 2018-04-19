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
    @FXML
    private Pane pane;

    private int round = 0;
    private List<List<Team>> data = new ArrayList<>();

    public void initialize(String filePath) {
        pane.getChildren().clear();
        data.clear();

        List<String> lines = loadFile(filePath);

        int teamSize = lines.size();
        int totalRound = 31 - Integer.numberOfLeadingZeros(teamSize);
        setBackground(teamSize);

        for (int i = 0; i < totalRound + 1; i++)
            data.add(new ArrayList<>());

        for (int i = 0; i < teamSize; i++)
            data.get(0).add(new Team(lines.get(arrange(totalRound, i) - 1)));

        for (int i = 1; i < totalRound + 1; i++)
            for (int j = 0; j < (1 << (totalRound - i)); j++)
                data.get(i).add(null);

        render(round);
    }

    private List<String> loadFile(String filepath) {
        try {
            return Files.readAllLines(Paths.get(filepath));
        } catch (IOException e) {
            new Alert(Alert.AlertType.WARNING, "File not found, use demo data").showAndWait();
            return IntStream.range(1, 17).mapToObj(i -> "team " + i).collect(Collectors.toList());
        }
    }

    private void setBackground(int teamSize) {
        pane.setBackground(new Background(new BackgroundImage(
                new Image(teamSize + ".jpg"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(900, 600, false, false, false, false)
        )));
    }

    private int arrange(int n, int k) {
        return n == 0 ? 1 : k % 2 == 1 ? (1 << n) + 1 - arrange(n - 1, k / 2) : arrange(n - 1, k / 2);
    }

    //Option 1: Do not show everything util all teams' scores are entered
    private void render(int round) {
        int size = data.get(round).size();
        for (int i = 0; i < size; i++) {
            int x = 450 + (((i < size / 2) ? -400 + round * 100 : 350 - round * 100));
            int y = 30 + (60) * (i % (size / 2));
            pane.getChildren().add(data.get(round).get(i).setLoc(x, y));
        }
    }

//Option 2: Compute and show everything entered
//    private void render() {
//    	pane.getChildren().clear();
//    	for(int i = 0; i < data.size(); i++) {
//    		int size = data.get(i).size();
//    		for(int j = 0; j < size; j++) {
//    			if(data.get(i).get(j) == null) continue;
//    			int x = 450 + (((j < size / 2) ? -400 + i * 100 : 350 - i * 100));
//                int y = 30 + (60) * (j % (size / 2));
//                pane.getChildren().add(data.get(i).get(j).setLoc(x, y));
//    		}
//    	}
//    }

//Option 3: Show everything in the current round
//    private void render(int round) {
//    	int size = data.get(round).size();
//	    for (int i = 0; i < size; i++) {
//	    	if(data.get(round).get(i) == null) continue;
//	        Team toAdd = data.get(round).get(i);
//	        if(pane.getChildren().contains(toAdd)) continue;
//	        int x = 450 + (((i < size / 2) ? -400 + round * 100 : 350 - round * 100));
//	        int y = 30 + (60) * (i % (size / 2));
//	        pane.getChildren().add(toAdd.setLoc(x, y));
//	    }
//    }


    @FXML
    private void handleNextRound(ActionEvent event) {
//Option 1: Do not show everything util all teams' scores are entered
        try {
            for (int i = 0; i < data.get(round).size() / 2; i++) {
                Team t1 = data.get(round).get(2 * i + 1);
                Team t2 = data.get(round).get(2 * i);
                data.get(round + 1).add(t1.compareTo(t2) > 0 ? t1.clone() : t2.clone());
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.WARNING, "Balabala").showAndWait();    //Change message here
        }
        render(++round);

//Option 2: Compute and show everything entered
//    	for(int i = 1; i < data.size(); i++) {
//    		for(int j = 0; j < data.get(i).size(); j++) {
//    			if(data.get(i).get(j) != null) continue;
//    			Team t1 = data.get(i-1).get(2*j);
//    			Team t2 = data.get(i-1).get(2*j+1);
//    			if(t1 == null || t2 == null || t1.getScore() == null || t2.getScore() == null) continue;
//    			data.get(i).set(j, t1.compareTo(t2)>0?t1.clone():t2.clone());
//    		}
//    	}
//    	render();

//Option 3: Show everything in the current round
//    	boolean complete = true;
//    	List<Team> curRound = data.get(round);
//    	List<Team> nextRound = data.get(++round);
//    	for(int i = 0; i < nextRound.size(); i++) {
//    		if(nextRound.get(i) != null) continue;
//    		Team t1 = curRound.get(2*i);
//    		Team t2 = curRound.get(2*i+1);
//    		if(t1 == null || t2 == null || t1.getScore() == null || t2.getScore() == null) {
//    			complete = false;
//    			continue;
//    		}
//    		nextRound.set(i, t1.compareTo(t2)>0?t1.clone():t2.clone());
//    	}
//    	if(!complete)
//    		render(round--);
//    	else
//    		render(round);
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
}
