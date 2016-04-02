/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controllers;

import classes.io.ImageCodecs;
import classes.io.Utils;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

/**
 * FXML Controller class
 *
 * @author Electronics
 */
public class CameraCaptureController implements Initializable {

    @FXML
    private Button playButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button captureButton;
    public boolean Threadrunnable;
    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    Mat source = new Mat();
    int i = 0;
    // the OpenCV object that performs the video capture
    private VideoCapture capture;
    // a flag to change the button behavior
    private boolean cameraActive;
    //a Mat container for grabbing each image
    Mat frame = new Mat();
    //
    Image displayImage;
   static Stage webcamStage;
   int frameWidth,frameHeight;
    //
    ImageCodecs codecs = new ImageCodecs();
    FileChooser fileChooser;
    @FXML
    public ImageView imageDisplay;
    DaemonThread myThread;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    @FXML
    private void playButtonAction(ActionEvent event) {
        capture=new VideoCapture(0);
        capture.open(0);
        // update the button content
        cameraActive = true;
        captureButton.setDisable(false);
        stopButton.setDisable(false);
        //start thrad     
       myThread = new DaemonThread(); //create object of threat class
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        t.start();                 //start thrad  
    }
//==============================================================================

    @FXML
    private void stopButtonAction(ActionEvent event) {
        myThread.runnable=false;
        capture.release();
        stopButton.setDisable(true);
        captureButton.setDisable(true);
    }
//==============================================================================

    @FXML
    private void captureButtonAction(ActionEvent event) {
        fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(Utils.JPG, "*.jpg"),
                    new FileChooser.ExtensionFilter(Utils.TIFF, "*.tiff"),
                    new FileChooser.ExtensionFilter(Utils.PNG, "*.png"));
        File file = fileChooser.showSaveDialog(webcamStage);
if (file != null) {
      String path=file.getAbsolutePath();  //gets directory plus file name
                String dir=codecs.removeExtention(path);                  //removes any filename extention if it exists
                String fileType=fileChooser.getSelectedExtensionFilter().getDescription(); //gets the description of the file type selected by user
                String ext=codecs.decodeExtention(fileType);              //gets extention from fileType
                dir+="."+ext;                                             //puts the correct extention to the file
               System.out.println("\nframe width:"+frame.width());
                Imgcodecs.imwrite(dir,frame);        
}
    }
//==============================================================================

    public void setPreset(int[] preset) {
        imageDisplay.setFitWidth(preset[0]);
        imageDisplay.setFitHeight(preset[1]);
    }
//==============================================================================  

    /**
     * Generic method for putting element running on a non-JavaFX thread on the
     * JavaFX thread, to properly update the UI
     *
     * @param property a {@link ObjectProperty}
     * @param value the value to set for the given {@link ObjectProperty}
     */
    private <T> void onFXThread(final ObjectProperty<T> property, final T value) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                property.set(value);
            }
        });
    }
//==============================================================================

    public void setPicture(Image image) {
        if (imageDisplay != null) {
            imageDisplay.setImage(image);
        }

    }
//==============================================================================
     class DaemonThread implements Runnable {

        protected volatile boolean runnable = false;

        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                              if (capture.isOpened()) {
                            capture.retrieve(source);
                            Core.flip(source, frame, 1);
                            onFXThread(imageDisplay.imageProperty(),codecs.matToImage(frame));
                    }
            }
        }
       
    }
     }
}
