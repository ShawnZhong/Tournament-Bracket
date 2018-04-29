package application;
/////////////////////////////////////////////////////////////////////////////
//Semester:         CS400 Spring 2018
//PROJECT:          cs400_p4_201804
//FILES:            Tournament.java
//                  Team.java
//                  Main.java
//
//USER:             Han Cao
//                  Suyan Qu
//                  Wanxiang Zhong
//                  Yujie Guo
//                  Yuhan Liu
//Instructor:       Deb Deppeler (deppeler@cs.wisc.edu)
//Bugs:             no known bugs
//
//2018 Mar 28, 2018  Main.java
////////////////////////////80 columns wide //////////////////////////////////
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Just a common main class;
 * Read one parameter from cmd line to read the teamlist file;
 * Example "teamList.txt"
 * Launch the GUI for the program
 */
public class Main extends Application {

    /**
     * Path for the teamlist file
     *
     * @see #start(Stage)
     */
    private static String filePath;

    /**
     * Read a command line parameter of the path of teamlist file
     * @param args String: the path of team list
     */
    public static void main(String[] args) {
    	System.out.println(1);
        filePath = args.length == 0 ? null : args[0];
        launch(args);
    }

    /**
     * Launch the JAVAFX GUI for the program
     * @param primaryStage the primary stage
     * @throws Exception when main.fxml cannot be read
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Tournament Bracket");
        Scene scene = new Scene(loader.load(), 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        Tournament tournament = loader.getController();
        tournament.initialize(filePath);
    }
}
