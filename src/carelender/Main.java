package carelender;

import carelender.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller.initialize();
        //Parent root = FXMLLoader.load(getClass().getResource("view/userinterface.fxml"));

        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("userinterface.fxml"));
        /*Parent root = FXMLLoader.load(new File("src/userinterface.fxml").toURI().toURL());
        if (root == null) {
        	System.out.println("Not from src");
            root = FXMLLoader.load(new File("userinterface.fxml").toURI().toURL());
        } else {
        	System.out.println("From src");
        }*/
        primaryStage.setTitle("CareLender");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
        /*GridPane root = new GridPane();
        Canvas canvas = new Canvas();
        TextField textField = new TextField();
        root.getChildren().add(canvas);
        root.getChildren().add(textField);

        primaryStage.setTitle("CareLender");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}
