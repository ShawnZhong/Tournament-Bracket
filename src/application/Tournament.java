package application;
/////////////////////////////////////////////////////////////////////////////
//Semester:         CS400 Spring 2018
//PROJECT:          cs400_p4_201804
//FILES:            Tournament.java 
//                  Team.java
//                  Main.java
//
//USER:             Han Cao,
//                  Suyan Qu,
//                  Wanxiang Zhong
//                  Yujie Guo
//                  Yuhan Liu
//Instructor:       Deb Deppeler (deppeler@cs.wisc.edu)
//Bugs:             no known bugs
//
//2018 Mar 28, 2018  Tournament.java 
////////////////////////////80 columns wide //////////////////////////////////

import com.sun.istack.internal.Nullable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This Tournament class contains methods that loads the file of teamList and
 * match different teams to compete with each other.
 */
public class Tournament {
    @FXML
    private Pane panes;
    private Pane pane;
    private int teamSize;
    private int totalRound;
    private List<String> lines;

    /**
     * This method initializes the GUI by creating team objects
     * and matching teams together.
     *
     * @param filePath the path of the teamList file.
     */
    void initialize(@Nullable String filePath) {
        initializeData(filePath);
        initializePane();
        initializeTeam();
    }

    /**
     * This method loads file by the path
     *
     * @param filepath the path of the file to be read.
     */
    private void initializeData(String filepath) {
        try {
            lines = Files.readAllLines(Paths.get(filepath));
        } catch (Exception e) {
            if (filepath != null) // Used for demo
                showWarn("File not found. Use demo data instead");
            ChoiceDialog<Integer> dialog = new ChoiceDialog<>(teamSize, 1, 2, 4, 8, 16);
            dialog.setTitle("Choose team size");
            dialog.setHeaderText("Demo mode");
            dialog.setContentText("Choose your team size:");
            dialog.showAndWait().ifPresent(integer -> teamSize = integer);
            lines = IntStream.range(1, teamSize + 1).mapToObj(i -> "Team " + String.format("%02d", i)).collect(Collectors.toList());
        }
        teamSize = lines.size();
        totalRound = 31 - Integer.numberOfLeadingZeros(teamSize);// calculate the total rounds of the competition
    }

    private void initializePane() {
        if (teamSize != 1 && teamSize != 2 && teamSize != 4 && teamSize != 8 && teamSize != 16) {
            showWarn("Team size " + teamSize + " not supported");
            teamSize = 16;
            initialize(null); // Demo mode
            return;
        }

        panes.getChildren().forEach(node -> node.setVisible(false));
        pane = (Pane) panes.getChildren().get(totalRound);
        pane.setVisible(true);
    }

    private void initializeTeam() {
        pane.getChildren().forEach(node -> ((Team) node).reset());

        for (int i = 0; i < teamSize; i++)
            getTeam(teamSize - 1 + i).setName(lines.get(shuffle(totalRound, i) - 1));
    }

    /**
     * This method matches which two teams to compete with each other.
     * See https://oeis.org/A208569
     * Example:
     * 1;
     * 1,  2;
     * 1,  4, 2, 3;
     * 1,  8, 4, 5, 2,  7, 3,  6;
     * 1, 16, 8, 9, 4, 13, 5, 12, 2, 15, 7, 10, 3, 14, 6, 11;
     *
     * @param n
     * @param k
     * @return a list of strings that contains the list of the teams.
     */
    private int shuffle(int n, int k) {
        return n == 0 ? 1 : k % 2 == 1 ? (1 << n) + 1 - shuffle(n - 1, k / 2) : shuffle(n - 1, k / 2);
    }


    @FXML
    private void handleTeamEvent(ActionEvent event) {
        Team team = (Team) event.getSource();

        if (team.equals(getTeam(0))) {
            showInfo(team + " wins!!!");
            return;
        }

        if (team.isCompleteRound())
            return;

        team.setScore(promptInput(team.getName()));
        compareScore(team);
    }

    private Double promptInput(String teamName) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Score");
        dialog.setContentText("Score for " + teamName + ": ");
        while (true) {
            try {
                Optional<String> str = dialog.showAndWait();
                if (!str.isPresent()) return null;
                Double score = Double.valueOf(str.get());
                if (score < 0) throw new InputMismatchException();
                return score;
            } catch (Exception e) {
                showWarn("Invalid input. Please try again.");
            }
        }
    }

    private void compareScore(Team team1) {
        int index = pane.getChildren().indexOf(team1);
        Team team2 = getTeam((index % 2 == 0) ? index - 1 : index + 1);
        if (team2 == null || team2.getScore() == null)
            return;

        if (team1.compareTo(team2) == 0) {
            showWarn(team1 + " and " + team2 + " tie!" + "\r\nStart another game! ");
            team1.setScore(null);
            team2.setScore(null);
            return;
        }

        Team parent = getTeam((index - 1) / 2);
        parent.setName(team1.compareTo(team2) > 0 ? team1.getName() : team2.getName());
        team1.setCompleteRound(true);
        team2.setCompleteRound(true);
        if (parent.equals(getTeam(0)))
            showInfo(parent + " wins!!!");

    }

    private Team getTeam(int index) { return (Team) pane.getChildren().get(index); }

    @FXML
    private void handleLoad(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Please choose team name file");
        fc.setInitialDirectory(new File("."));
        initialize(fc.showOpenDialog(new Stage()).getPath());
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
