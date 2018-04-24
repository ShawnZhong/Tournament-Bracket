package application;
/////////////////////////////////////////////////////////////////////////////
//Semester:         CS400 Spring 2018
//PROJECT:          cs400_p4_201804
//FILES:            Tournament.java 
//                  Team.java
//                  Main.java
//
//USER:             Han Cao
//                  Suyan Qu
//                  Wanxiang Zhong
//                  Yujie Guo
//                  Yuhan Liu
//Instructor:       Deb Deppeler (deppeler@cs.wisc.edu)
//Bugs:             no known bugs
//
//2018 Mar 28, 2018  Tournament.java 
////////////////////////////80 columns wide //////////////////////////////////

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This Tournament class contains methods that loads the file of teamList and
 * matches different teams to compete with each other.
 */
public class Tournament {
    @FXML
    private Pane panes;
    private Pane pane;
    private int teamSize = 16;
    private int totalRound;
    private List<String> lines;

    /**
     * This method initializes the GUI by creating team objects
     * and matching teams altogether.
     *
     * @param filePath the path of the teamList file.
     */
    public void initialize(String filePath) {
        initializeData(filePath);
        initializePane();
        initializeTeam();
    }

    /**
     * This method loads file through its path
     *
     * @param filepath the path of the file to be read.
     */
    private void initializeData(String filepath) {
        try {
            lines = Files.readAllLines(Paths.get(filepath));
        } catch (Exception e) {
            if (filepath != null) // if not in demo mode
                showWarn("File not found. Entering demo mode...");
            ChoiceDialog<Integer> dialog = new ChoiceDialog<>(16, 0, 1, 2, 4, 8, 16);
            dialog.setTitle("Choose team size");
            dialog.setHeaderText("Demo mode");
            dialog.setContentText("Choose your team size:");
            dialog.showAndWait().ifPresent(integer -> teamSize = integer);
            lines = IntStream.range(1, teamSize + 1).mapToObj(i -> "Team " + String.format("%02d", i)).collect(Collectors.toList());
        }
        teamSize = lines.size();
        totalRound = 31 - Integer.numberOfLeadingZeros(teamSize);// calculate the total rounds of the competition
    }

     /**
     * This method displays teams to the pane. 
     *
     * @param nothing.
     * @return nothing. 
     */
    private void initializePane() {
        // if there are no teams at all
        if (teamSize == 0) {
            showWarn("No challengers, no games, and no champion.");
            totalRound = 0;
        }

        if ((teamSize & (teamSize - 1)) != 0 || teamSize > 16) { // not power of 2 or larger than 16
            showWarn("Team size " + teamSize + " not supported. Use demo data instead.");
            teamSize = 16; // set the default value to be 16
            initialize(null); // Demo mode
            return;
        }

        panes.getChildren().forEach(node -> node.setVisible(false));
        pane = (Pane) panes.getChildren().get(totalRound); 
        pane.setVisible(true); // display the pane
    }

    private void initializeTeam() {
        pane.getChildren().forEach(node -> ((Team) node).setStatus(Status.HIDDEN));

        for (int i = 0; i < teamSize; i++)
            ((Team) pane.getChildren().get(teamSize - 1 + i)).setName(lines.get(shuffle(totalRound, i) - 1));
    }

    /**
     * This method matches up two teams to compete with each other in the optimal way.
     * See https://oeis.org/A208569
     * Example:
     * 1;
     * 1,  2;
     * 1,  4, 2, 3;
     * 1,  8, 4, 5, 2,  7, 3,  6;
     * 1, 16, 8, 9, 4, 13, 5, 12, 2, 15, 7, 10, 3, 14, 6, 11;
     *
     * @param n Total number of teams.
     * @param k The index of the current position.
     * @return a list of strings that contains the list of the teams.
     */
    private int shuffle(int n, int k) {
        return n == 0 ? 1 : k % 2 == 1 ? (1 << n) + 1 - shuffle(n - 1, k / 2) : shuffle(n - 1, k / 2);
    }


    @FXML
    private void handleTeamEvent(ActionEvent event) {
        Team team = (Team) event.getSource();

        if (team.getStatus().equals(Status.WIN)) {
            showInfo(team + " is the winner.");
            return;
        }

        if (team.getStatus().equals(Status.LOSE)) {
            showInfo(team + " loses the game.");
            return;
        }

        team.setScore();
        compareScore(team);
    }


    private void compareScore(Team team1) {
        int index = pane.getChildren().indexOf(team1);
        int index2 = (index % 2 == 0) ? index - 1 : index + 1;
        Team team2 = (Team) pane.getChildren().get(index2);

        if (team2.getStatus().equals(Status.NOT_STARTED) || team2.getStatus().equals(Status.HIDDEN))
            return;

        if (team1.compareTo(team2) == 0) {
            showWarn(team1 + " and " + team2 + " tie!" + "\r\nStart another game! ");
            team1.setStatus(Status.NOT_STARTED);
            team2.setStatus(Status.NOT_STARTED);
            return;
        }

        Team winner = team1.compareTo(team2) > 0 ? team1 : team2;
        Team loser = team1.compareTo(team2) < 0 ? team1 : team2;
        winner.setStatus(Status.WIN);
        loser.setStatus(Status.LOSE);

        int parentIndex = (index - 1) / 2;
        Team parent = (Team) pane.getChildren().get(parentIndex);
        parent.setName(winner.getName());

        if (parentIndex == 1)
            showInfo(parent + " wins the game!!!");
    }

    @FXML
    private void handleLoad(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Please choose team name file");
        fc.setInitialDirectory(new File("."));
        File file = fc.showOpenDialog(new Stage());
        if (file != null)
            initialize(file.getPath());
    }

    @FXML
    private void handleExit(ActionEvent event) { System.exit(0); }

    @FXML
    private void handleDemo(ActionEvent event) { initialize(null); }

    @FXML
    private void handleReset(ActionEvent event) { initializeTeam(); }

    private void showInfo(String str) {new Alert(Alert.AlertType.INFORMATION, str).showAndWait();}

    private void showWarn(String str) {new Alert(Alert.AlertType.WARNING, str).showAndWait();}
}
