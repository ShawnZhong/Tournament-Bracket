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

enum Status {HIDDEN, DEFAULT, IN_PROGRESS, LOSE, WIN}

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
            if (!notChanged && textField.getText().trim().length() != 0) {
                try {
                    score = Integer.valueOf(textField.getText());
                    status = Status.IN_PROGRESS;
                } catch (Exception e) {
                    new Alert(Alert.AlertType.INFORMATION, "Invalid Input").showAndWait();
                    textField.clear();
                }
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
        setStatus(Status.DEFAULT);
    }

    /**
     * Status of the team
     *  HIDDEN: Hide the team from canvas
     *  DEFAULT: NO score but name
     *  IN_PROGRESS:
     *  WIN: HighList as a Winner
     *  Lose: Set the status to a Loser
     * @return the status of this team
     *
     * @see Status
     */
    public Status getStatus() {
        return status;
    }

    /**
     *  HIDDEN: Hide the team from canvas
     *  DEFAULT: NO score but name
     *  IN_PROGRESS:
     *  WIN: HighList as a Winner
     *  Lose: Set the status to a Loser
     * @param status the status of a given team
     *
     * @see Status
     */
    public void setStatus(Status status) {
        switch (this.status = status) {
            case HIDDEN:
                setVisible(false);
                break;
            case DEFAULT:
                getStyleClass().removeAll("winner", "loser");
                setVisible(true);
                break;
            case IN_PROGRESS:
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
