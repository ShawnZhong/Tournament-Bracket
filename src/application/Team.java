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
//2018 Mar 28, 2018  Team.java
////////////////////////////80 columns wide //////////////////////////////////

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

enum Status {HIDDEN, NO_SCORE, SCORE_ENTERED, LOSE, WIN}


/**
 * A Class used to represent the team in Tournament;
 * Contains methods that used to present a Team:
 * Include Name, score, status; and closure functions for editing those fields
 */
public class Team extends HBox implements Comparable<Team> {
    private Label label;
    private TextField textField;
    private Integer score;

    /**
     * This is the current status of the team
     *
     * @see Status
     */
    private Status status;

    public void initialize(String name) {
        label = (Label) getChildren().get(0);
        textField = (TextField) getChildren().get(1);
        textField.focusedProperty().addListener((arg0, arg1, notChanged) -> {
            if (notChanged)
                return;

            if (textField.getText().trim().length() == 0) {
                setStatus(Status.NO_SCORE);
                return;
            }

            try {
                if ((score = Integer.valueOf(textField.getText())) < 0)
                    throw new Exception("Score should not be negative");

                setStatus(Status.SCORE_ENTERED);
            } catch (Exception e) {
                new Alert(Alert.AlertType.INFORMATION, "Invalid Input\n" + e.getMessage()).showAndWait();
                setStatus(Status.NO_SCORE);
            }
        });

        setName(name);
    }

    /**
     * Get the name
     *
     * @return String name of the team
     */
    public String getName() {
        return label.getText();
    }

    public void setName(String name) {
        label.setText(name);
        setStatus(Status.NO_SCORE);
    }

    /**
     * Status of the team
     * HIDDEN: Hide the team from canvas
     * NO_SCORE: NO score but name
     * SCORE_ENTERED:
     * WIN: HighList as a Winner
     * Lose: Set the status to a Loser
     *
     * @return the status of this team
     * @see Status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * HIDDEN: Hide the team from canvas
     * NO_SCORE: NO score but name
     * SCORE_ENTERED:
     * WIN: HighList as a Winner
     * Lose: Set the status to a Loser
     *
     * @param status the status of a given team
     * @see Status
     */
    public void setStatus(Status status) {
        switch (this.status = status) {
            case HIDDEN:
                setVisible(false);
                break;
            case NO_SCORE:
                getStyleClass().removeAll("winner", "loser");
                setVisible(true);
                score = null;
                textField.setEditable(true);
                textField.clear();
                break;
            case SCORE_ENTERED:
                break;
            case WIN:
                getStyleClass().add("winner");
                textField.setEditable(false);
                break;
            case LOSE:
                getStyleClass().add("loser");
                textField.setEditable(false);
                break;
        }
    }

    /**
     * Used to compare with other team in terms of scores
     *
     * @param other A TEAM
     * @return Boolean:
     * True: Win
     * False: Lost
     */
    public int compareTo(Team other) {
        return score.compareTo(other.score);
    }
}
