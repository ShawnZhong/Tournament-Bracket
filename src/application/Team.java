package application;

import com.sun.istack.internal.Nullable;
import javafx.scene.control.Button;

import java.text.DecimalFormat;

public class Team extends Button implements Comparable<Team> {
    private static final DecimalFormat formatter = new DecimalFormat("0.#");

    private boolean completeRound;
    private String name;
    private Double score;

    public Team() { super(); }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
        setVisible(true);
        update();
    }

    public Double getScore() { return score; }

    public void setScore(@Nullable Double score) {
        this.score = score;
        update();
    }

    private void update() {setText(score == null ? name : name + ": " + formatter.format(score));}

    public void reset() {
        setScore(null);
        setVisible(false);
        setDisable(false);
        setCompleteRound(false);
    }

    @Override
    public String toString() { return name; }

    public int compareTo(Team other) { return score.compareTo(other.score); }

    public boolean isCompleteRound() { return completeRound; }

    public void setCompleteRound(boolean completeRound) { this.completeRound = completeRound; }
}
