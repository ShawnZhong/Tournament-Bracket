package application;

import javafx.scene.control.Button;

public class Team extends Button implements Comparable<Team> {
    private boolean completeRound;
    private String name;
    private Double score;

    public Team() { super(); }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
        update();
    }

    public Double getScore() { return score; }

    public void setScore(Double score) {
        this.score = score;
        update();
    }

    private void update() {setText(score == null ? name : name + ": " + score);}

    @Override
    public String toString() { return "Team{" + "name='" + name + '\'' + ", score=" + score + '}'; }

    public int compareTo(Team other) { return score.compareTo(other.score); }

    public boolean isCompleteRound() { return completeRound; }

    public void setCompleteRound(boolean completeRound) { this.completeRound = completeRound; }
}
