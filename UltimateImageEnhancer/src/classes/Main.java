/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import classes.io.Utils;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import static javafx.scene.input.DataFormat.URL;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;

public class Main extends Application {

Stage publicStage;
Parent root=null;
URL url=null;
Mat currentImage=null;

    @Override
    public void start(Stage primaryStage) throws IOException { 
        url=getClass().getResource(Utils.MAINWINDOW);
         root=FXMLLoader.load(url); 
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

    public Stage getStage() {
        return publicStage;
    }
    public Mat getCurrentImage(){
        return currentImage;
    }
    public void setCurrentImage(Mat image){
        this.currentImage=image;
    }
}
