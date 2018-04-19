package application;

import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

public class Team extends Button implements Comparable<Team>, Cloneable {
    private String name;
    private Integer score;
    private boolean completeRound;

    public Team() {
        this("Team");
        setVisible(false);
    }

    public Team(String name) {
        super(name);

        this.name = name;

        setOnMouseClicked(e -> {
            if (!completeRound) {
                Team t = (Team) e.getSource();
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Input Score");
                dialog.setContentText("Score for " + t.getText() + ":");
                while (true) {
                    try {
                        dialog.showAndWait().ifPresent(s -> t.setScore(Integer.parseInt(s)));
                        Tournament.getController().handleInput();
                        break;
                    } catch (Exception exc) { }
                }
            }
        });
    }

    public Team clone() {
        return new Team(this.name);
    }

    public Team clone(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
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

    private void setScore(int score) {
        this.score = score;
        setText(name + ": " + score);
    }

    public void completeRound() {
        completeRound = true;
    }
}
