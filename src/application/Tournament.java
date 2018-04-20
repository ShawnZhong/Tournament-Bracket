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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    private Pane pane16;
    @FXML
    private Pane pane8;
    @FXML
    private Pane pane4;
    @FXML
    private Pane pane2;
    @FXML
    private Pane pane1;
    private Pane pane;
    private int teamSize;

    /**
     * This method initializes the GUI by creating team objects
     * and matching teams together.
     *
     * @param filePath the path of the teamList file.
     */
    public void initialize(String filePath) {
        List<String> lines = loadFile(filePath);
        teamSize = lines.size();
        initializePane();
        initializeTeam(lines);
    }

    private void initializePane() {
        pane1.setVisible(false);
        pane2.setVisible(false);
        pane4.setVisible(false);
        pane8.setVisible(false);
        pane16.setVisible(false);
        switch (teamSize) {
            case (16):
                pane = pane16;
                break;
            case (8):
                pane = pane8;
                break;
            case (4):
                pane = pane4;
                break;
            case (2):
                pane = pane2;
                break;
            case (1):
                pane = pane1;
                break;
            default:
                pane = pane16;
                new Alert(Alert.AlertType.WARNING, "Team size not supported").showAndWait();
                break;
        }
        pane.setVisible(true);
    }

    private void initializeTeam(List<String> lines) {
        for (Node node : pane.getChildren()) {
            Team team = (Team) node;
            team.setVisible(false);
            team.setDisable(false);
            team.setCompleteRound(false);
        }

        int teamSize = lines.size();

        //calculate the total rounds of the competition
        int totalRound = 31 - Integer.numberOfLeadingZeros(teamSize);
        for (int i = 0; i < teamSize; i++) {
            Team team = getTeam(teamSize - 1 + i);
            team.setName(lines.get(shuffle(totalRound, i) - 1));
            team.setVisible(true);
        }
    }

    /**
     * This method loads file by the path, throws IOException if file not found.
     *
     * @param filepath the path of the file to be read.
     * @return a list of strings that contains the list of the teams.
     */
    private List<String> loadFile(String filepath) {
        try {
            return Files.readAllLines(Paths.get(filepath));
        } catch (Exception e) {
            if (!filepath.equals("")) // Used for demo
                new Alert(Alert.AlertType.WARNING, "File not found. Use demo data instead").showAndWait();
            ChoiceDialog<Integer> dialog = new ChoiceDialog<>(teamSize, 1, 2, 4, 8, 16);
            dialog.setTitle("Choose team size");
            dialog.setContentText("Choose your team size:");
            Optional<Integer> result = dialog.showAndWait();
            result.ifPresent(integer -> teamSize = integer);
            return IntStream.range(1, teamSize + 1).mapToObj(i -> "Team " + String.format("%02d", i)).collect(Collectors.toList());
        }
    }


    /**
     * This method matches which two teams to compete with each other.
     *
     * @param n
     * @param k
     * @return a list of strings that contains the list of the teams.
     */
    private int shuffle(int n, int k) {
        return n == 0 ? 1 : k % 2 == 1 ? (1 << n) + 1 - shuffle(n - 1, k / 2) : shuffle(n - 1, k / 2);
    }


    @FXML
    private void handleInput(ActionEvent event) {
        Team team = (Team) event.getSource();

        if (team.equals(getTeam(0))) {
            new Alert(Alert.AlertType.WARNING, team.getName() + " wins!!!").showAndWait();
            return;
        }

        if (!team.isCompleteRound()) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Input Score");
            dialog.setContentText("Score for " + team.getName() + ": ");
            while (true) {
                try {
                    dialog.showAndWait().ifPresent(s -> {
                        Double score = Double.valueOf(s);
                        if (score < 0) throw new InputMismatchException();
                        team.setScore(score);
                        compareScore(team);
                    });
                    break;
                } catch (Exception e) {
                    new Alert(Alert.AlertType.WARNING, "Invalid input. Please try again.").showAndWait();
                }
            }
        }
    }

    private void compareScore(Team team1) {
        int index = pane.getChildren().indexOf(team1);
        Team team2 = getTeam((index % 2 == 0) ? index - 1 : index + 1);
        if (team2 != null && team2.getScore() != null) {
            if (team1.compareTo(team2) == 0) {
                new Alert(Alert.AlertType.WARNING, team1.getName() + " and " + team2.getName() + " tie!"
                        + "\r\nStart another game! ").showAndWait();
                team1.setScore(null);
                team2.setScore(null);
                return;
            }

            Team parent = getTeam((index - 1) / 2);
            parent.setName(team1.compareTo(team2) > 0 ? team1.getName() : team2.getName());
            parent.setVisible(true);
            team1.setCompleteRound(true);
            team2.setCompleteRound(true);
            if (parent.equals(getTeam(0)))
                new Alert(Alert.AlertType.WARNING, parent.getName() + " wins!!!").showAndWait();
        }
    }

    private Team getTeam(int index) { return (Team) pane.getChildren().get(index); }

    @FXML
    private void handleDemo(ActionEvent event) {
        initialize("");
    }

    @FXML
    private void handleLoad(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Please choose team name file");
        fc.setInitialDirectory(new File("."));
        initialize(fc.showOpenDialog(new Stage()).getPath());
    }

    @FXML
    private void handleExit(ActionEvent event) { System.exit(0); }
}
