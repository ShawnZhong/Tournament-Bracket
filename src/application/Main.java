package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static String filePath;

    public static void main(String[] args) {
        filePath = args[0];
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Tournament Bracket");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("main.fxml")), 1000, 600));
        primaryStage.show();

        Tournament.getController().initialize(filePath);
    }


}
