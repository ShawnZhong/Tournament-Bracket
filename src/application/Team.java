package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Optional;

enum Status {DEFAULT, NOT_STARTED, IN_PROGRESS, LOSE, WIN}

public class Team extends Button implements Comparable<Team> {
    private static final DecimalFormat formatter = new DecimalFormat("0.#");

    private String name;
    private Double score;
    private Status status;

    public Team() { this("Team 00"); }

    public Team(String name) {setName(name);}

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
        setStatus(Status.NOT_STARTED);
    }

    public void setScore() {
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

    public Status getStatus() { return status; }


    public void setStatus(Status status) {
        switch (this.status = status) {
            case DEFAULT:
                getStyleClass().removeAll("winner", "loser");

                this.score = null;
                setText(name);
                setVisible(false);
                break;
            case NOT_STARTED:
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


    @Override
    public String toString() { return name; }

    public int compareTo(Team other) { return score.compareTo(other.score); }
}
