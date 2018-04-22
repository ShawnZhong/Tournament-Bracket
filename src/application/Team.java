package application;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.text.DecimalFormat;

enum Status {LOSE, WIN, NO_SCORE, ROUND_NOT_FINISHED}

public class Team extends Button implements Comparable<Team> {
    private static final DecimalFormat formatter = new DecimalFormat("0.#");
    //    private boolean completeRound;
    private Status status = Status.NO_SCORE;
    private String name;
    private Double score;


    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
        setVisible(true);
        setText(name);
    }

    public void setScore(Double score) {
        this.score = score;
        if (score == null) {
            setStatus(Status.NO_SCORE);
            return;
        }
        setText(name + ": " + formatter.format(score));
        setStatus(Status.ROUND_NOT_FINISHED);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;

        switch (status) {
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
                break;
            case ROUND_NOT_FINISHED:
                break;
        }
    }

    public void reset() {
        setStatus(Status.NO_SCORE);
        setVisible(false);
    }

    @Override
    public String toString() { return name; }

    public int compareTo(Team other) { return score.compareTo(other.score); }
}
