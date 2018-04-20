package application;

import javafx.scene.control.Button;

public class Team extends Button implements Comparable<Team>, Cloneable {
    public boolean completeRound;
    private String name;
    private Integer score;

    public Team() {
        this("Team");
        setVisible(false);
    }

    public Team(String name) {
        super(name);

        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        setText(name);
        this.name = name;
    }

    public Team clone() {
        return new Team(this.name);
    }


    @Override
    public String toString() {
        return "Team{" + "name='" + name + '\'' + ", score=" + score + '}';
    }

    @Override
    public int compareTo(Team other) {
        return score - other.score;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
        setText(score == null ? name : name + ": " + score);
    }

    public void completeRound() {
        completeRound = true;
    }
}
