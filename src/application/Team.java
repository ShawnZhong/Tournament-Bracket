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
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Optional;

enum Status {HIDDEN, DEFAULT, IN_PROGRESS, LOSE, WIN}

/**
 * A Class used to represent the team in Tournament;
 */
public class Team extends Button implements Comparable<Team> {
    private static final DecimalFormat formatter = new DecimalFormat("0.#");

    private String name; // The name of the team
    private Double score; // The score of the team
    private Status status; // Win or Lose?

    /**
     * Default constructor of Team, set name to default
     * Normally will not be called
     */
    public Team() { this("Team 00"); }

    /**
     * Constructor
     * @param name the name of team
     */
    public Team(String name) {setName(name);}

    /**
     * Get the name
     * @return String name of the team
     */
    public String getName() { return name; }

    /**
     * set the name
     * @param name String, the name of the team
     */
    public void setName(String name) {
        this.name = name;
        setStatus(Status.DEFAULT);
    }

    /**
     * Set the score for the team
     * Negative number of other unNumber characters will not be accept
     */
    public void setScore() {
        // GUI part
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Score");
        dialog.setContentText("Input Score");
        dialog.setContentText("Score for " + name + ": ");
        while (true) try {
            Optional<String> str = dialog.showAndWait();
            if (!str.isPresent())
                return;
            Double score = Double.valueOf(str.get());
            if (score < 0)
                throw new InputMismatchException();
            this.score = score;
            setStatus(Status.IN_PROGRESS);
            return;
        } catch (Exception e) {
            new Alert(Alert.AlertType.WARNING, "Invalid input. Please try again.").showAndWait();
        }
    }

    /**
     * Status of the team
     *  HIDDEN: Hide the team from canvas
     *  DEFAULT: NO score but name
     *  IN_PROGRESS:
     *  WIN: HighList as a Winner
     *  Lose: Set the status to a Loser
     * @return
     */
    public Status getStatus() { return status; }

    /**
     *  HIDDEN: Hide the team from canvas
     *  DEFAULT: NO score but name
     *  IN_PROGRESS:
     *  WIN: HighList as a Winner
     *  Lose: Set the status to a Loser
     * @param status
     */
    public void setStatus(Status status) {
        switch (this.status = status) {
            case HIDDEN:
                getStyleClass().removeAll("winner", "loser");
                this.score = null;
                setText(name);
                setVisible(false);
                break;
            case DEFAULT:
                this.score = null;
                setText(name);
                setVisible(true);
                break;
            case IN_PROGRESS:
                setText(name + ": " + formatter.format(score));
                break;
            case WIN:
                getStyleClass().add("winner");
                break;
            case LOSE:
                getStyleClass().add("loser");
                break;
        }
    }

    /**
     * Print name
     * @return the name
     */
    @Override
    public String toString() { return name; }

    /**
     * Used to compare with other team in terms of scores
     * @param other A TEAM
     * @return Boolean:
     * True: Win
     * False: Lost
     */
    public int compareTo(Team other) { return score.compareTo(other.score); }
}
