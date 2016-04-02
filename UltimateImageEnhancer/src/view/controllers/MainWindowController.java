/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.controllers;

import actions.RedoAction;
import actions.UndoAction;
import classes.Adjustments;
import classes.Effects;
import classes.Main;
import classes.io.ImageCodecs;
import classes.io.Utils;
import java.awt.Desktop;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author Electronics
 */
public class MainWindowController implements Initializable {

    Main win = new Main();
    String imageDir[] = new String[32];      //for storing the directory for each image
    Image image[] = new Image[32];           // for storing every processed image for display
    Mat imageMat[] = new Mat[32];            // for storing every processed image
    ImageCodecs codecs = new ImageCodecs();
    Adjustments adjust=new Adjustments();
    Effects effect=new Effects();
    FileChooser fileChooser;
    //custom made internal frame for displaying image
    URL url = null;
    Parent root = null;
    BorderPane pane = null;
    int redo;
    boolean firstRedo = false, hasImage = false, hasChooser = false;
    int value, width, height, index, prevIndex;
    protected UndoAction undoAction;
    protected RedoAction redoAction;
    protected UndoManager undo = new UndoManager();
    HashMap<Object, Action> actions;
    private final String facebook=  "https://www.facebook.com/Ultimate-Image-Enhancer-1703362323253772/?skip_nax_wizard=true";
    private final String yahoo = "mailto:vusumuzitshabangu@yahoo.com";
    private final String youtube = "https://www.youtube.com/playlist?list=PLh1XHNREyQEPbcvNjmBpihNXE6bqvIzKP";
    //==========================================================================
    Stage popupStage;
    @FXML
    private Button addImageButton;
    @FXML
    private Button saveImageButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    @FXML
    private Button webcamButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button contrastButton;
    @FXML
    private Button brightnessButton;
    @FXML
    private Button sharpnessButton;
    @FXML
    private Button resizeButton;
    @FXML
    private Button flipHorizontalButton;
    @FXML
    private Button flipVerticalButton;
    @FXML
    private Button black_whiteButton;
    @FXML
    private Button blurrButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Label sizeText;
    @FXML
    private Button faceBookButton;
    @FXML
    private Button yahooButton;
    @FXML
    private Button youtubeButton;
    @FXML
    private Button linkedinButton;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        popupStage = new Stage();
    }
//==============================================================================    

    @FXML
    public void addImageButtonAction() {
        //-- Get image from JFile chooser ---------------------------------------------
        if (hasChooser == false) {            //check if JChooser object already created
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(Utils.JPG, "*.jpg"),
                    new FileChooser.ExtensionFilter(Utils.TIFF, "*.tiff"),
                    new FileChooser.ExtensionFilter(Utils.PNG, "*.png")
            );
            hasChooser = true;
        }
        File returnFile = fileChooser.showOpenDialog(win.getStage());

        if (returnFile != null) {
            if (hasImage) {                 // If there's currently an image display
                // remove it
                hasImage = false;
            }
            imageDir[index] = returnFile.getAbsolutePath(); // store directory
            System.out.print(imageDir[index]);
            imageMat[index] = Imgcodecs.imread(imageDir[index]);
            image[index] = codecs.matToImage(imageMat[index]);
            onFXThread(imageView.imageProperty(), image[index]);
            String size = "Size:" + imageMat[index].width() + "x" + imageMat[index].height();
            sizeText.setText(size);
            win.setCurrentImage(imageMat[index]);
        }
    }
//==============================================================================    

    @FXML
    public void saveImageButtonAction() {
       File file = fileChooser.showSaveDialog(null);
if (file != null) {
      String path=file.getAbsolutePath();  //gets directory plus file name
                String dir=codecs.removeExtention(path);                  //removes any filename extention if it exists
                String fileType=fileChooser.getSelectedExtensionFilter().getDescription(); //gets the description of the file type selected by user
                String ext=codecs.decodeExtention(fileType);              //gets extention from fileType
                dir+="."+ext;                                             //puts the correct extention to the file
                Imgcodecs.imwrite(dir, imageMat[index]);        
}
    }
//==============================================================================    

    @FXML
    public void undoButtonAction() {
 try{
            if(image[index-1]!=null){
                if(!firstRedo){
                    redo=index;
                    firstRedo=true;
                }
                --index;
                repaintImage();
            }
        }
        catch(ArrayIndexOutOfBoundsException ex){

        }
    }
//==============================================================================    

    @FXML
    public void redoButtonAction() {
   if(imageMat[index]!=null){
       if(index<redo && firstRedo){
            ++index;
            repaintImage();
        }
   }
        
    }
//==============================================================================    

    @FXML
    public void webcamButtonAction() {

        try {
            url = getClass().getResource(Utils.WEBCAMWINDOW);
            root = FXMLLoader.load(url);
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            popupStage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//==============================================================================    

    @FXML
    public void exitButtonAction() {
        System.exit(0);
    }
//==============================================================================    

    @FXML
    public void contrastButtonAction() {
        try {
            url = getClass().getResource(Utils.CONTRASTWINDOW);
            FXMLLoader loader = new FXMLLoader(url);
            root = loader.load();
            ContrastController con = (ContrastController) loader.getController();
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            con.initPreviewImage(imageMat[index], this);
            popupStage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//==============================================================================    

    @FXML
    public void brightnessButtonAction() {
        try {
            url = getClass().getResource(Utils.BRIGHTNESSWINDOW);
            FXMLLoader loader = new FXMLLoader(url);
            root = loader.load();
            BrightnessController con = (BrightnessController) loader.getController();
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            con.initPreviewImage(imageMat[index], this);
            popupStage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //==============================================================================    

    @FXML
    public void sharpnessButtonAction() {
        try {
            url = getClass().getResource(Utils.SHARPNESSWINDOW);
            FXMLLoader loader = new FXMLLoader(url);
            root = loader.load();
            SharpnessController con = (SharpnessController) loader.getController();
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            con.initPreviewImage(imageMat[index], this);
            popupStage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//==============================================================================    

    @FXML
    public void resizeButtonAction() {
        try {
            url = getClass().getResource(Utils.RESIZEWINDOW);
            FXMLLoader loader = new FXMLLoader(url);
            root = loader.load();
            ResizeController con = (ResizeController) loader.getController();
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            con.initPreviewImage(imageMat[index], this);
            popupStage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//==============================================================================    

    @FXML
    public void flipVerticalButtonAction() {
     if(imageMat[index] != null){
          prevIndex=index;
          ++index;
          checkIndex();
        imageMat[index] = adjust.flipImage(imageMat[prevIndex], 0);
        repaintImage();  
      }
    }
//==============================================================================    

    @FXML
    public void flipHorizontalButtonAction() {
      if(imageMat[index] != null){
          prevIndex=index;
          ++index;
          checkIndex();
        imageMat[index] = adjust.flipImage(imageMat[prevIndex], 1);
        repaintImage(); 
      }
        
    }
//==============================================================================    

    @FXML
    public void black_whiteButtonAction() {
if(imageMat[index]!=null){  //check if there's an image
            //-- first save previous image for undo and redo operation ---------------------
            prevIndex=index;
            ++index;
            checkIndex();   // check if image array is exhausted
            //--- performs a grayscale transformation onto next image ----------------------
            imageMat[index]=effect.RGB2GrayScale(imageMat[prevIndex]);
            repaintImage();
        }
    }
//==============================================================================    

    @FXML
    public void blurrButtonAction() {
        try {
            url = getClass().getResource(Utils.BLURRWINDOW);
            FXMLLoader loader = new FXMLLoader(url);
            root = loader.load();
            BlurrController con = (BlurrController) loader.getController();
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            con.initPreviewImage(imageMat[index], this);
            popupStage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//==============================================================================

    public Mat getImage() {
        return imageMat[index];
    }
//==============================================================================

    public void updateImage(Mat image) {
        if (image != null) {
            prevIndex = index;
            ++index;
            checkIndex();
            imageMat[index] = image;
            this.image[index] = codecs.matToImage(image);
            onFXThread(this.imageView.imageProperty(), this.image[index]);
            String size = "Size:" + imageMat[index].width() + "x" + imageMat[index].height();
            sizeText.setText(size);
            popupStage.close();
        }
        if (image == null) {
            popupStage.close();
        }
    }
//==============================================================================
    public void checkIndex() {
        if (index == 31) {
            index = 0;
        }
    }
//==============================================================================
    public void repaintImage(){
       image[index]=codecs.matToImage(imageMat[index]); //create Image from Mat object
       onFXThread(this.imageView.imageProperty(), this.image[index]); 
       String size = "Size:" + imageMat[index].width() + "x" + imageMat[index].height();
            sizeText.setText(size);
    }

    @FXML
    private void faceBookButtonAction(ActionEvent event) throws MalformedURLException {
        try {
            Desktop desk= Desktop.getDesktop();
            URL click =new URL(facebook);
            
            try {
                desk.browse(click.toURI());
            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void yahooButtonAction(ActionEvent event) {
        URL click = null;
        try {
            Desktop desk= Desktop.getDesktop();          
             try {
                 click = new URL(yahoo);
             } catch (MalformedURLException ex) {
                 Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
             }
            
            try {
                desk.browse(click.toURI());
            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void youtubeButtonAction(ActionEvent event) {
         URL click = null;
        try {
            Desktop desk= Desktop.getDesktop();          
             try {
                 click = new URL(youtube);
             } catch (MalformedURLException ex) {
                 Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
             }
            
            try {
                desk.browse(click.toURI());
            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void linkedinButtonAction(ActionEvent event) {
    }
}
