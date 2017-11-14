package Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static Constants.ViewConstants.*;

public class Launcher extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("launcher.fxml"));
        primaryStage.setTitle(VIEW_TITLE);
        primaryStage.setScene(new Scene(root, VIEW_WIDTH, VIEW_HEIGHT));
        primaryStage.show();
    }

}
