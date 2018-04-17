package application;

import javafx.scene.control.Button;

public class Team extends Button {
    private String name;
    private Integer score;

    public Team(String name) {
        super(name);
        this.name = name;
    }

    public Team(String name, Integer score) {
        super(name);
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
        setText(name + ": " + score);
    }

    @Override
    public String toString() {
        return "Team{" + "name='" + name + '\'' + ", score=" + score + '}';
    }
}
