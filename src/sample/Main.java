package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Main extends Application {
    private recorder r=new recorder();
    private byte[][] voice=new byte[10][recorder.hz*recorder.bit/8*recorder.STEREO];
    private byte[] voice2=new byte[recorder.hz*recorder.bit/8*recorder.STEREO];
    private NumberAxis x=new NumberAxis();
    private NumberAxis y=new NumberAxis();
    private XYChart.Series Series=new XYChart.Series();
    private LineChart<Number,Number> lineChart=new LineChart<Number,Number>(x,y);
    private int c=0;
    private FileOutputStream output;
    private FileInputStream input;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(lineChart, 1900, 1800));
        lineChart.getData().add(Series);
        primaryStage.show();
        try
        {
            output = new FileOutputStream("output.dat");
            input=new FileInputStream("output.dat");
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        System.out.println("開始");
        for (int i = 0; i < 1; i++) {
            voice[i] = r.getVoice();

            for (byte b : voice[i])
                System.out.println(b);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        r.stop();
        output.write(voice[0]);
        input.read(voice2);
        for(int i=0;i<voice2.length;i++)
            Series.getData().add(new XYChart.Data(i,voice2[i]));
        input.close();
        output.flush();
        output.close();
        System.out.println("終了");
        //test
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop()
    {
        r.stop();
    }
}
