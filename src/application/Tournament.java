package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
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
        //render();
        //render(round);
    }

    private void initialize(List<String> lines) {
        int teamSize = lines.size();
        int totalRound = (int)(Math.log(teamSize) / Math.log(2));
        pane.setBackground(new Background(new BackgroundImage(new Image(teamSize + ".jpg"), BackgroundRepeat.NO_REPEAT, 
        	BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(900,600,false,false,false,false))));
        for (int i = 0; i < totalRound+1; i++)
            data.add(new ArrayList<>());

        for (int i = 0; i < teamSize; ++i)
        	data.get(0).add(new Team(lines.get(arrange(totalRound, i)-1)));
        
        for(int i = 1; i < totalRound+1; i++) {
        	for(int j = 0; j < (int)Math.pow(2, totalRound-i); j++)
        		data.get(i).add(null);
        }
    }
    
    private int arrange(int n, int k) {
    	if(n == 0) return 1;
    	if(k%2 == 1)
			return (int)Math.pow(2,n)+1 - arrange(n-1, k/2);
		else
			return arrange(n-1, k/2);
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
//Option 1: Do not show everything util all teams' scores are entered
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
    private void exit(ActionEvent event) {
        System.exit(0);
    }
}
