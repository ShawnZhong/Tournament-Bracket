package application;

import javafx.scene.control.Button;

public class Team extends Button implements Comparable<Team>, Cloneable {
    public boolean completeRound;
    private String name;
    private Integer score;

    public Team() {
        this("Team");
        setDisable(true);
        //setVisible(true);
    }

    public Team(String name) {
        super(name);

        this.name = name;

        setOnMouseClicked(e -> {

        });
    }

    public Team clone() {
        return new Team(this.name);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setText(name);
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

    public void setScore(int score) {
        this.score = score;
        setText(name + ": " + score);
    }

    public void completeRound() {
        completeRound = true;
    }
}
