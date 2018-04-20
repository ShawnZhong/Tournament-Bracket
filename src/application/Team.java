package application;

import javafx.scene.control.Button;

public class Team extends Button implements Comparable<Team> {
    private boolean completeRound;
    private String name;
    private Integer score;

    public Team() { super(); }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
        update();
    }

    public Integer getScore() { return score; }

    public void setScore(Integer score) {
        this.score = score;
        update();
    }

    private void update() {setText(score == null ? name : name + ": " + score);}

    @Override
    public String toString() { return "Team{" + "name='" + name + '\'' + ", score=" + score + '}'; }

    @Override
    public int compareTo(Team other) { return score - other.score; }


    public boolean isCompleteRound() { return completeRound; }

    public void setCompleteRound(boolean completeRound) { this.completeRound = completeRound; }
}
