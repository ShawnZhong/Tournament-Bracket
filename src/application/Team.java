package application;

import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

public class Team extends Button {
    private String name;
    private Integer score;

    public Team(String name) {
        super(name);

        setOnMouseClicked(e -> {
            Team t = (Team) e.getSource();
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Input Score");
            dialog.setContentText("Score for " + t.getText() + ":");
            dialog.showAndWait().ifPresent(s -> t.setScore(Integer.parseInt(s)));
        });

        this.name = name;
    }

    public Team(String name, int x, int y) {
        this(name);
        setLayoutX(x);
        setLayoutY(y);
    }

    public String getName() {
        return name;
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
