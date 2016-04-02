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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
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
public class ResizeController implements Initializable {

    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private RadioButton percRadioButton;
    @FXML
    private RadioButton valRadioButton;
    @FXML
    private TextField percTextField;
    @FXML
    private TextField widthTextField;
    @FXML
    private TextField heightTextField;
    Image previewImage;
    Mat imageMat;
    int width, height;
    float perc;
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
    private void okButtonAction(ActionEvent event) {
        if (!percRadioButton.isDisabled() && !valRadioButton.isDisabled()) {

        } else {
            if (!percRadioButton.isDisabled()) {
               try{
                   perc = Float.valueOf(percTextField.getText());
                  width=imageMat.width();
                  height=imageMat.height();
                  perc/=100;
                  width*=perc;
                  height*=perc;
                   imageMat=adjust.changeSize(imageMat, width, height, 96);
               }
                catch(NumberFormatException ex){}
               catch(CvException e){}           
            }
            if (!valRadioButton.isDisabled()) {
               try{
                   width = Integer.valueOf(widthTextField.getText());
                   height = Integer.valueOf(heightTextField.getText());
                   imageMat=adjust.changeSize(imageMat, width, width, 96);
               }
                 catch(NumberFormatException ex){}
                 catch(CvException e){} 
            }
            mainWin.updateImage(imageMat);
        }

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

    @FXML
    private void percRadioButtonAction(ActionEvent event) {
        if (percRadioButton.isSelected()) {
//disable image presets options
            valRadioButton.setDisable(true);
            widthTextField.setDisable(true);
            heightTextField.setDisable(true);
            //enable percentage input options
            percRadioButton.setDisable(false);
            percTextField.setDisable(false);
        }
        if (!percRadioButton.isSelected()) {
//enable image presets options
            valRadioButton.setDisable(false);
            widthTextField.setDisable(false);
            heightTextField.setDisable(false);
            //disable percentage input options
            percRadioButton.setDisable(false);
            percTextField.setDisable(false);
        }
    }

    @FXML
    private void valRadioButtonAction(ActionEvent event) {
        if (valRadioButton.isSelected()) {
//enable image presets options
            valRadioButton.setDisable(false);
            widthTextField.setDisable(false);
            heightTextField.setDisable(false);
            //disable percentage input options
            percRadioButton.setDisable(true);
            percTextField.setDisable(true);
        }
        if (!valRadioButton.isSelected()) {
//disable image presets options
            valRadioButton.setDisable(false);
            widthTextField.setDisable(false);
            heightTextField.setDisable(false);
            //enable percentage input options
            percRadioButton.setDisable(false);
            percTextField.setDisable(false);
        }
    }

}
