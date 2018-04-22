package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Optional;

enum Status {HIDDEN, NO_SCORE, LOSE, WIN, ROUND_NOT_FINISHED}

public class Team extends Button implements Comparable<Team> {
    private static final DecimalFormat formatter = new DecimalFormat("0.#");

    private Status status;
    private String name;
    private Double score;

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
        setStatus(Status.NO_SCORE);
    }

    public Status getStatus() { return status; }

    public void setStatus(Status status) {
        switch (this.status = status) {
            case WIN:
                setUnderline(true);
                break;
            case LOSE:
                setTextFill(Color.DARKGRAY);
                break;
            case NO_SCORE:
                this.score = null;
                setUnderline(false);
                setTextFill(Color.BLACK);
                setText(name);
                setVisible(true);
                break;
            case HIDDEN:
                this.score = null;
                setUnderline(false);
                setTextFill(Color.BLACK);
                setText(name);
                setVisible(false);
                break;
            case ROUND_NOT_FINISHED:
                setText(name + ": " + formatter.format(score));
                break;
        }
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

            setStatus(Status.ROUND_NOT_FINISHED);
            return;
        } catch (Exception e) {
            new Alert(Alert.AlertType.WARNING, "Invalid input. Please try again.").showAndWait();
        }
    }

    @Override
    public String toString() { return name; }

    public int compareTo(Team other) { return score.compareTo(other.score); }
}
