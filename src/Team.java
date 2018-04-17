import javafx.scene.control.Button;

public class Team extends Button {
    private String name;
    private Integer score;

    public Team(String name) {
        super(name);
        this.name = name;
    }

    public Team(String name, int score) {
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Team{" + "name='" + name + '\'' + ", score=" + score + '}';
    }
}
