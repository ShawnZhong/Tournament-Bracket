package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static String filePath;

    public static void main(String[] args) {
        filePath = args.length == 0 ? "" : args[0];
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));

        primaryStage.setTitle("Tournament Bracket");
        primaryStage.setScene(new Scene(loader.load(), 1000, 600));
        primaryStage.show();

        Tournament tournament = loader.getController();
        tournament.initialize(filePath);
    }
}
