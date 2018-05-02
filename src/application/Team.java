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

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * The status enum is used for representing the status of Team
 * <p>
 * HIDDEN: when the team is hidden
 * NO_SCORE: when the team have no score entered
 * SCORE_ENTERED: when the team have score entered
 * LOSE: when the team lose the game
 * WIN: when the team win the game
 */
enum Status {
    HIDDEN, NO_SCORE, SCORE_ENTERED, LOSE, WIN
}


/**
 * A Class used to represent the team in Tournament;
 * Contains methods that used to present a Team:
 * Include Name, score, status; and closure functions for editing those fields
 */
public class Team extends HBox implements Comparable<Team> {
    private Label label; // the label of the team
    private TextField textField; // the textField to enter the score of the team
    private Integer score; // the score of a team

    /**
     * This is the current status of the team
     *
     * @see Status
     */
    private Status status;

    /**
     * this is the method that initializes the information of a team, including
     * the label, textField, and name.
     *
     * @param name the name of the team to be initialized
     * @return nothing.
     */
    public void initialize(String name) {
        label = (Label) getChildren().get(0);
        textField = (TextField) getChildren().get(1);
        label.setText(name);
        setStatus(Status.NO_SCORE); // default status
    }

    /**
     * Get the name
     *
     * @return String name of the team
     */
    public String getName() {
        return label.getText();
    }


    public void setScore(int score) {
        this.score = score;
        setStatus(Status.SCORE_ENTERED);
    }

    /**
     * Get status of the team
     *
     * @return the status of this team
     * @see Status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * set status of the team
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
