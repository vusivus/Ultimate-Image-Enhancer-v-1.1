/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controllers;

import classes.Adjustments;
import classes.Main;
import classes.io.ImageCodecs;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.opencv.core.CvException;
import org.opencv.core.Mat;

/**
 * FXML Controller class
 *
 * @author Electronics
 */
public class SharpnessController implements Initializable {

    @FXML
    private Slider slider;
    @FXML
    private ImageView preView;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
Image previewImage;
    Mat imageMat;
    int index, prevIndex;
    boolean init;
    Main con = new Main();
    Adjustments adjust = new Adjustments();
    ImageCodecs codecs = new ImageCodecs();
    MainWindowController mainWin;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void sliderAction(MouseEvent event) {
        int value =(int) slider.getValue();
        if(value>=1){
            try {
            imageMat = adjust.changeSharpness(imageMat, index);
            previewImage = codecs.matToImage(imageMat);
            setPreviewImage(previewImage);
        } catch (CvException ex) {
        }
        }    
    }

    @FXML
    private void okButtonAction(ActionEvent event) {
        mainWin.updateImage(imageMat);
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        mainWin.updateImage(null);
    }
//==============================================================================

    public void initPreviewImage(Mat image, MainWindowController controller) {
        imageMat = image;
        previewImage = codecs.matToImage(imageMat);
        mainWin = controller;
    }
//==============================================================================

    private void setPreviewImage(Image image) {
        onFXThread(preView.imageProperty(), image);
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
}