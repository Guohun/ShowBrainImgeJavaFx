/*
 * This is the JavaFx code to read a matlabe template for head and illustrate it on the screen.
 */
package ScatterBrainPkg;

/**
 *
 * @author Guohun Zhu
 * 
 */
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DispBrain2D extends Application {

    private final static double MIN = 1 ;
    private final static double MAX = 100 ;
    private final static double BLUE_HUE = Color.BLUE.getHue() ;
    private final static double RED_HUE = Color.RED.getHue() ;

    @Override
    public void start(Stage primaryStage) {
        //Image colorScale = createColorScaleImage(600, 120, Orientation.HORIZONTAL);
        Image colorScale = createColorScaleImage(800, 500, Orientation.VERTICAL);
        ImageView imageView = new ImageView(colorScale);
        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private Image createColorScaleImage(int width, int height, Orientation orientation) {
        try {
            
            String matFile= "D:\\CST\\export_touch_stone\\Clinical-Data-20200220-Stage-1\\boundaries_estimated_by_nn_plus_templates\\output\\000001\\1-delta+0d0h0m\\dscan1-emscan-e00-cal_template.mat";
            
            MatFileReader matfilereader = new MatFileReader(matFile);
            Map<String, MLArray> mykeymap = matfilereader.getContent();
            MLArray words_x = (MLArray) matfilereader.getMLArray("colored_template");  //warpped_template
            //MLArray words_x = (MLArray) matfilereader.getMLArray("warpped_template");  //warpped_template

               
              com.jmatio.types.MLNumericArray  temp=
                      ((com.jmatio.types.MLNumericArray) words_x);
            //com.jmatio.types.MLDouble mychanel = ((com.jmatio.types.MLDouble) words_x);
            //double plotData[]= new double[mychanel.getN()];
               final int S = 2;
                int W=temp.getN()/3;
                int loc=height/2;               
               WritableImage image = new WritableImage(S*temp.getM() , S*temp.getN());//w h
               PixelWriter pixelWriter = image.getPixelWriter();
            
            for (int i = 0; i <temp.getM(); i++) 
                for (int j = 0; j < temp.getN()/3; j++) {
    //                int r=temp.get(i, 3*j).intValue();
  //                  int g=temp.get(i, 3*j+1).intValue();
//                    int b=temp.get(i, 3*j+2).intValue();
                    int r=temp.get(i, j).intValue();
                    int g=temp.get(i, W+j).intValue();
                    int b=temp.get(i, 2*W+j).intValue();
                    
                    //Color color = Color.rgb(r &0xff,g&0xff, b&0xff);// temp.get(i,j);  //getColorForValue(value);
                    Color color = Color.rgb( (r) &0xff,(g)&0xff, b&0xff);// temp.get(i,j);  //getColorForValue(value);
                    
                    for (int dy = 0; dy < S; dy++) {
                           for (int dx = 0; dx < S; dx++) {
                               pixelWriter.setColor(i*S+dx, loc+j*S+dy, color);
                           }
                               //pixelWriter.setArgb(x * S + dx, y * S + dy, argb);
                    }                    
                     //pixelWriter.setArgb(i, j, r);  
                }
  //              boundary_X[i] = mychanel.get(i, 0)
    //        }
      //      boundaryXY[i] = new Point2D(boundary_X[i], boundary_Y[i]);

            return image ;
        } catch (IOException ex) {
            Logger.getLogger(DispBrain2D.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
