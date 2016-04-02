/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.io;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author Electronics
 */
public class  ImageCodecs {
private FileView jpegFile,jpgFile,pngFile,tiffFile;

//==============================================================================
public Image matToImage(Mat frame)
	{
		// create a temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode the frame in the buffer, according to the PNG format
		Imgcodecs.imencode(".png", frame, buffer);
		// build and return an Image created from the image encoded in the
		// buffer
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}
//==============================================================================
public String decodeExtention(String description){
    String ext="";
    if(description.equals(Utils.JPG)){
        ext="jpg";
    }
    if(description.equals(Utils.PNG)){
        ext="png";
    }
    if(description.equals(Utils.TIFF)){
        ext="tiff";
    }
    return ext;
}   
//==============================================================================
public String removeExtention(String file){
    
    String returnFile=null;
    int index=file.indexOf('.');
    
    if(index==-1){
        returnFile=file;
    }
    else{
        returnFile=file.substring(0, index);
    }
    return returnFile;
}
}
