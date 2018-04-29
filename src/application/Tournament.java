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
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.GridPane;
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
    /**
     * This variable links to 'panes' in the fxml file
     * It is the parent of all other panes (pane16, pane8, pane4, ..., pane1)
     *
     * @see #initializePane()
     */
    @FXML
    private Pane panes;

    /**
     * This is the actual pane we are using.
     * It will store one of the pane from {pane16, ..., pane1}, depending on the team size
     *
     * @see #initializePane()
     */
    private Pane pane;

    /**
     * This GridPane is used to display champions (first, second, third)
     * It is a child of the selected pane above
     *
     * @see #initializePane()
     */
    private Pane topThreeBox;

    /**
     * This is the size of team
     * Acceptable: 0,1,2,4,8,16
     *
     * @see #initializeTeam()
     */
    private int teamSize = 16;

    /**
     * This is the number of total round, determined by the size of team
     *
     * @see #initializeData(String)
     */
    private int totalRound;

    /**
     * This is the content of file read from filePath
     * It consists of all team names
     *
     * @see #initializeData(String)
     */
    private List<String> lines;

    /**
     * This method will
     * 1. call initializeData to initialize {@code lines}
     * 2. call initializePane to initialize {@code pane} and {@code topThreeBox}
     * 3. call initializeTeam to initialize all the teams
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
     * {@code lines}, {@code teamSize}, {@code totalRound} will be initialed
     *
     * If we there is any exception, or the {@code filepath} is null
     * Then we will enter demo mode, and initialed lines with {Team 01, ..., Team 16}
     *
     * @param filepath the path of the file to be read.
     */
    private void initializeData(String filepath) {
        try {
            lines = Files.readAllLines(Paths.get(filepath));
        } catch (Exception e) {
            // if not in demo mode, then we need to prompt error
            if (filepath != null)
                showWarn("File not found. Entering demo mode...");

            // Show prompting dialog
            ChoiceDialog<Integer> dialog = new ChoiceDialog<>(16, 0, 1, 2, 4, 8, 16);
            dialog.setTitle("Choose team size");
            dialog.setHeaderText("Demo mode");
            dialog.setContentText("Choose your team size:");
            dialog.showAndWait().ifPresent(integer -> teamSize = integer);

            // initialed lines with {Team 01, ..., Team 16}
            lines = IntStream.range(1, teamSize + 1)
                    .mapToObj(i -> "Team " + String.format("%02d", i))
                    .collect(Collectors.toList());
        }
        teamSize = lines.size();

        // calculate the total rounds of the competition
        // if teamSize = 0, then totalRound = -1
        totalRound = 31 - Integer.numberOfLeadingZeros(teamSize);
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

    /**
     * This method will initialize the {@code pane} variable by choosing the right pane to display
     * It will also initialize {@code topThreeBox} with the corresponding one
     */
    private void initializePane() {
        // Set all panes to be invisible
        // this is necessary when we want to use another team size
        panes.getChildren().forEach(node -> node.setVisible(false));

        // if there are no teams at all
        if (teamSize == 0)
            showWarn("No challengers, no games, and no champion.");

        // if teamSize is not power of 2 or is 0 or larger than 16
        if ((teamSize & (teamSize - 1)) != 0 || teamSize > 16) {
            showWarn("Team size " + teamSize + " not supported. Use demo data instead.");
            teamSize = 16; // set the default value to be 16
            initialize(null); // Demo mode
            return;
        }

        // get the corresponding pane depending on totalRound, and display it
        pane = (Pane) panes.getChildren().get(totalRound + 1);
        pane.setVisible(true);

        // set topThreeBox to corresponding GridPane, and set as hidden
        topThreeBox = (GridPane) pane.getChildren().get(1);
        topThreeBox.setVisible(false);
    }



    /**
     * This method will initialize all the teams displayed on the pane
     */
    private void initializeTeam() {
        // When team size is 1, directly display the topThreeBox
        // Since the champion is determinant when there is only one team
        if (teamSize == 1) {
            ((Button) topThreeBox.getChildren().get(0)).setText(lines.get(0));
            topThreeBox.setVisible(true);
            return;
        }

        // Initialize all the teams to be hidden first
        for (int i = 0; i < teamSize * 2 - 2; i++)
            getTeam(i).setStatus(Status.HIDDEN);

        // matches each team with team to compete with using the shuffle method
        // The status will be set to default after initialize is called
        // And the team will be displayed on the pane
        for (int i = 0; i < teamSize; i++)
            getTeam(teamSize - 2 + i).initialize(lines.get(shuffle(totalRound, i) - 1));
    }

//    /**
//     * Event handler for the team buttons
//     *
//     * @param event the event invoked
//     */
//    @FXML
//    private void handleTeam(ActionEvent event) {
//        // TODO: Get team by index, and this method should be invoked handleConfirm by the confirm button
//        // Get the team clicked
//        Team team = (Team) event.getSource();
//
//        // If a given team is the winner in some round
//        if (team.getStatus().equals(Status.WIN)) {
//            showInfo(team.getName() + " is the winner.");
//            return;
//        }
//
//        // If a given team is the loser in some round
//        if (team.getStatus().equals(Status.LOSE)) {
//            showInfo(team.getName() + " loses the game.");
//            return;
//        }
//
//        // compare with another team
//        compareScore(team);
//    }

    /**
     * Handle the the action of final submit button;
     * Do two things:
     * 1. compare the score of two teams
     * 2. change the status of two teams
     */
    @FXML
    private void handleConfirm(ActionEvent event) {
        //Get the button index
        Button btn = (Button) event.getSource();
        int index = ((Group) (pane.getChildren().get(2))).getChildren().indexOf(btn);
        Team team1 = getTeam(index * 2);
        Team team2 = getTeam(index * 2 + 1);
        System.out.println(index);
        compareScore(team1, team2, index - 1);
    }

    /**
     * This method compares the scores of two teams and set their winning status.
     *
     * @param team1 the team to be compared with its paired team.
     */
    private void compareScore(Team team1, Team team2, int parentIndex) {
        // if team1 has not start playing
        if (team1.getStatus().equals(Status.HIDDEN))
            return;

        // if team2 has not start playing
        if (team2.getStatus().equals(Status.HIDDEN))
            return;

        // if two teams have the same score
        if (team1.compareTo(team2) == 0) {
            showWarn(team1.getName() + " and " + team2.getName() + " tie!" + "\r\nStart another game! ");
            team1.setStatus(Status.DEFAULT);
            team2.setStatus(Status.DEFAULT);
            return;
        }

        // decide the winner of the game
        Team winner = team1.compareTo(team2) > 0 ? team1 : team2;
        Team loser = team1.compareTo(team2) < 0 ? team1 : team2;
        winner.setStatus(Status.WIN);
        loser.setStatus(Status.LOSE);


        // Check if the whole game is finished
        if (parentIndex == -1) {
            showInfo(winner.getName() + " wins the game!!!");
            showChampion(winner, loser);
            return;
        }

        // set the name for next round
        Team parent = getTeam(parentIndex);
        parent.initialize(winner.getName());
    }

    /**
     * This method handle the champions
     * It will fill topThreeBox with correct information, and display it
     *
     * @param first  the first prize
     * @param second the second prize
     */
    private void showChampion(Team first, Team second) {
        // No need to display Champion Box if there are less than 2 teams
        if (teamSize < 2)
            return;

        // Show Champion Box, because this method will be called only all the games is finished
        topThreeBox.setVisible(true);

        // set name for first and second place
        ((Button) topThreeBox.getChildren().get(0)).setText(first.getName());
        ((Button) topThreeBox.getChildren().get(1)).setText(second.getName());

        // No need to display the third place if there are only two teams
        if (teamSize < 4)
            return;

        // Determine the third place
        Team third = IntStream.range(2, 6)
                .mapToObj(this::getTeam)
                .filter(e -> e.getStatus().equals(Status.LOSE))
                .sorted(Team::compareTo)
                .collect(Collectors.toList())
                .get(1);

        // Set name for the third place
        ((Button) topThreeBox.getChildren().get(2)).setText(third.getName());
    }


    /**
     * Event handler for the first prize, second prize & third prize button
     *
     * @param event not used
     */
    @FXML
    public void handleTopThree(ActionEvent event) {
        // get the team clicked and its index
        Team team = (Team) event.getSource();
        int index = topThreeBox.getChildren().indexOf(team);

        // display the corresponding info
        String[] place = {"first", "second", "third"};
        showInfo(team.getName() + " is " + place[index] + " place!!!");
    }

    /**
     * Event handler for the load button
     *
     * @param event not used
     */
    @FXML
    private void handleLoad(ActionEvent event) {
        // Build a file chooser
        FileChooser fc = new FileChooser();
        fc.setTitle("Please choose team name file");
        fc.setInitialDirectory(new File("."));
        File file = fc.showOpenDialog(new Stage());

        // continue if there is some file chosen
        if (file != null)
            initialize(file.getPath());
    }

    /**
     * Event handler for the exit button
     *
     * @param event not used
     */
    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }


    /**
     * Event handler for the demo button
     *
     * @param event not used
     */
    @FXML
    private void handleDemo(ActionEvent event) {
        initialize(null); // Entering demo mode
    }


    /**
     * Event handler for the reset button
     *
     * @param event not used
     */
    @FXML
    private void handleReset(ActionEvent event) {
        initializeTeam();
    }

    /**
     * A help method used to find the competitor of given team
     *
     * @param index the index of a given team
     * @return its competitor
     */
    private Team getCompetitor(int index) {
        return getTeam((index % 2 == 0) ? index + 1 : index - 1);
    }

    /**
     * A help method used to find the team by its index
     *
     * @param index the index of a given team
     * @return the team
     */
    private Team getTeam(int index) {
        return (Team) ((Pane) pane.getChildren().get(0)).getChildren().get(index);
    }

    /**
     * A helper method used to get the index of a specific team
     * <p>
     * Word as a "inverse function" of getTeam
     *
     * @param team a given team
     * @return its index
     */
    private int getTeamIndex(Team team) {
        return ((Pane) pane.getChildren().get(0)).getChildren().indexOf(team);
    }

    /**
     * A private helper used to display information
     *
     * @param str the information we want to display
     */
    private void showInfo(String str) {
        new Alert(Alert.AlertType.INFORMATION, str).showAndWait();
    }

    /**
     * A private helper used to display warning
     *
     * @param str the warning message we want to display
     */
    private void showWarn(String str) {
        new Alert(Alert.AlertType.WARNING, str).showAndWait();
    }
}
