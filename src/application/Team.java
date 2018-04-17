package application;

import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

public class Team extends Button implements Comparable<Team> {
    private String name;
    private Integer score;


    public Team(String name) {
        super(name);

        this.name = name;

        setOnMouseClicked(e -> {
            Team t = (Team) e.getSource();
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Input Score");
            dialog.setContentText("Score for " + t.getText() + ":");
            while (true) {
                try {
                    dialog.showAndWait().ifPresent(s -> t.setScore(Integer.parseInt(s)));
                    break;
                } catch (Exception exc) { }
            }

        });
    }

    public Team setLoc(int x, int y) {
        setLayoutX(x);
        setLayoutY(y);
        return this;
    }

    private void setScore(int score) {
        this.score = score;
        setText(name + ": " + score);
    }

    @Override
    public String toString() {
        return "Team{" + "name='" + name + '\'' + ", score=" + score + '}';
    }

    @Override
    public int compareTo(Team other) {
        return score - other.score;
    }
}
