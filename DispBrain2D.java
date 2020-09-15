/*
 * This is the JavaFx code to read a matlabe template for head and illustrate it on the screen.

September 15 2020
Add heatmap into brain template.

 */
package ScatterBrainPkg;

/**
 *
 * @author Guohun Zhu
 * 
 */
import ELM.Kmeans;
import clinic.scatterLine;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import demo.Complex;
import eu.hansolo.fx.heatmap.ColorMapping;
import eu.hansolo.fx.heatmap.OpacityDistribution;
import eu.hansolo.fx.heatmap.SimpleHeatMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DispBrain2D extends Application {
    int MyH=600;
    int MyW=600;

    private final static double MIN = 1 ;
    private final static double MAX = 100 ;
    private final static double BLUE_HUE = Color.BLUE.getHue() ;
    private final static double RED_HUE = Color.RED.getHue() ;
    private StackPane Rootpane;
    private SimpleHeatMap heatMap;
public Point2D[] detectedLoc(float scale){
      Point2D [] temp=  new Point2D[] {
            new Point2D(110, 238),
            new Point2D(120, 144),
            new Point2D(207, 119),
            new Point2D(315, 348),
            new Point2D(264, 226),
            new Point2D(280, 159),
            new Point2D(240, 186),
            new Point2D(228, 170),
            new Point2D(234, 160),
            new Point2D(214, 170),
            new Point2D(200, 200),
        };
        
      return temp;
    }
    @Override
    public void start(Stage primaryStage) {
        //Image colorScale = createColorScaleImage(600, 120, Orientation.HORIZONTAL);
        MenuBar menuBar=new MenuBar();
        Menu menuGame = new Menu("File");
        MenuItem newGame = new MenuItem("Load Brain Scan File         F1");
        MenuItem exit = new MenuItem("Exit                            F2");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
            }
        });
        menuGame.getItems().addAll(newGame,new SeparatorMenuItem(),exit);
        menuBar.getMenus().addAll(menuGame);
        
        Image colorScale = createColorScaleImage(800, 500, Orientation.VERTICAL);
        ImageView imageView = new ImageView(colorScale);
        ImageViewPane viewPane=new ImageViewPane(imageView);
        
        VBox vbox=new VBox();

        vbox.setAlignment(Pos.CENTER);
//        vbox.getChildren().add(viewPane);
        Point2D[] events = detectedLoc(1);
        heatMap.addEvents(events);

        Rootpane.getChildren().addAll(viewPane, heatMap.getHeatMapImage());        
        vbox.getChildren().addAll(menuBar,Rootpane);
        VBox.setVgrow(Rootpane, Priority.ALWAYS);        
        Scene scene = new Scene(vbox, 800, 500);
        primaryStage.setScene(scene);
        
        primaryStage.setTitle("BrainScan@Zhu");
        primaryStage.show();
    }

     @Override
    public void init() {
        Rootpane = new StackPane();
        heatMap = new SimpleHeatMap(MyW, MyH, ColorMapping.LIME_YELLOW_RED, 40);
        heatMap.setOpacityDistribution(OpacityDistribution.LINEAR);
        heatMap.setHeatMapOpacity(1);        
//        Plot_Str=obtain_dots();

    }

    private Image createColorScaleImage(int width, int height, Orientation orientation) {
        try {
            
            String matFile= "D:\\CST\\export_touch_stone\\Clinical-Data-20200220-Stage-1\\boundaries_estimated_by_nn_plus_templates\\output\\000001\\1-delta+0d0h0m\\dscan1-emscan-e00-cal_template.mat";
            
            MatFileReader matfilereader = new MatFileReader(matFile);
            Map<String, MLArray> mykeymap = matfilereader.getContent();
            //System.out.println("new "+matfilereader.getMLArray("dataStruct"));
            MLArray words_x = (MLArray) matfilereader.getMLArray("colored_template");  //warpped_template
            //MLArray words_x = (MLArray) matfilereader.getMLArray("warpped_template");  //warpped_template
//            MLArray words_y = (MLArray) matfilereader.getMLArray("boundary_y");
               
              com.jmatio.types.MLNumericArray  temp=
                      ((com.jmatio.types.MLNumericArray) words_x);
            //com.jmatio.types.MLDouble mychanel = ((com.jmatio.types.MLDouble) words_x);
                 System.out.println("row=\t" + temp.getM() + "\t col=" + temp.getN() + "\t" + temp.get(0, 0));
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
