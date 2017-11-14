package Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static Constants.ViewConstants.*;

public class Launcher extends Application {

    private static Launcher launcher;

    private Stage rootStage;

    public Launcher() {}

    public static Launcher getInstance() {
        return launcher;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        launcher = this;
        Parent root = FXMLLoader.load(getClass().getResource("launcher.fxml"));
        primaryStage.setTitle(VIEW_TITLE);
        primaryStage.setScene(new Scene(root, VIEW_WIDTH, VIEW_HEIGHT));
        rootStage = primaryStage;
        primaryStage.show();
    }

    public Stage getRootStage() {
        return rootStage;
    }

}
