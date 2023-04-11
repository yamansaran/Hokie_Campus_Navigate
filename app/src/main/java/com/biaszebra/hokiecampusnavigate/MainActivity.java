package com.biaszebra.hokiecampusnavigate;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.jsibbold.zoomage.ZoomageView;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//TODO change table file to relative units


public class MainActivity extends AppCompatActivity {

    private int coreXDim = 1393;//TODO remove hard coding
    private int coreYDim = 1692;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ZoomageView customView = findViewById(R.id.myZoomageView);
        //coreXDim = customView.getWidth();
        //coreYDim = customView.getHeight();
    }

    public void onBtnClick(View view){
        ZoomageView customView = findViewById(R.id.myZoomageView);
        TextView pathView = findViewById(R.id.pathView);
        TestWithSampleGraph runner = new TestWithSampleGraph();
        EditText location = findViewById(R.id.location);
        String locationString = location.getText().toString();
        EditText destination = findViewById(R.id.destination);
        String destinationString = destination.getText().toString();
        InputStream is = this.getResources().openRawResource(R.raw.node_map);//NOTE MAP LOCATION

        ArrayList<String[]> table = new ArrayList<>();
        InputStream is2 = this.getResources().openRawResource(R.raw.sample_table);
        try (Scanner sc = new Scanner(is2, StandardCharsets.UTF_8.name())) {
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                String[] res = str.split("[,]", 0);
                table.add(res);
                //System.out.println(res[0]+" "+res[1]+" "+res[2] + " added to table");
            }
        }
        String testTest = runner.startRun(locationString,destinationString, is);
        //System.out.println(testTest + "~~~~~~~~~~~~~");
        testTest= testTest.replaceAll("\\s", "");
        testTest= testTest.replaceAll("\\[", "");
        testTest= testTest.replaceAll("\\]", "");
        //System.out.println(testTest + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        pathView.setText(testTest);
        //TODO
        /*
            Create string array for coordinates from testTest
            add csv file with coordinates
            surround paint in for loop to draw paths
         */

        //customView.onDrawCo( 0,0,100,100, canvas);
        //Bitmap myBitmap = BitmapFactory.decodeFile("res/drawable/simple_node_map_shrunk.png");

        Bitmap newBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_graph_cropped);
        Bitmap myBitmap = Bitmap.createScaledBitmap(newBitmap, customView.getWidth(), customView.getHeight(), true);
        //Bitmap myBitmap = ((BitmapDrawable)customView.getDrawable()).getBitmap();
        //System.out.println(myBitmap.getHeight());
        Bitmap tempBitmap = myBitmap.copy(myBitmap.getConfig(), true);
        //Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_graph_cropped);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);

        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStrokeWidth(15);

        Paint explorer = new Paint();
        explorer.setColor(Color.BLACK);
        explorer.setStrokeWidth(3);

        tempCanvas.drawLine(0,0,tempBitmap.getWidth(),tempBitmap.getHeight(),explorer);
        //tempCanvas.drawLine(1000,1000,2000,1000,p);
        //tempCanvas.drawLine(2000,1000,1500,1600,p);
        String[] res = testTest.split("[,]", 0);
        //System.out.println(res[0] + " " + res[3]);
        //ArrayList<Integer> temp = returnCor(res[0]); //TODO do this
        for(int i = 0; i < res.length;i++){
            //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+res[i]);
            if(i==(res.length-1)){

            }else{
                //System.out.println("PAINTPAINTPAINT");
                ArrayList<Integer> coorCurrent = returnCor(res[i],table);
                ArrayList<Integer> coorNext = returnCor(res[i+1],table);
                int xdim = tempBitmap.getWidth();
                int ydim = tempBitmap.getHeight();
                //System.out.println(xdim + "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + ydim);
                //System.out.println(coreXDim + "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + coreYDim);



                int xCur = (int)(coorCurrent.get(0));
                int yCur = (int)(coorCurrent.get(1));
                int xNext = (int)(coorNext.get(0));
                int yNext = (int)(coorNext.get(1));

                xCur = scalingMod(coreXDim,xdim,xCur);
                yCur = scalingMod(coreYDim,ydim,yCur);
                xNext = scalingMod(coreXDim,xdim,xNext);
                yNext = scalingMod(coreYDim,ydim,yNext);

                tempCanvas.drawLine(xCur,yCur,xNext,yNext,p);
            }
        }

        returnCor("A",table);


        customView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }

    public Double doubleDivision(Double x, Double y) {
        Double z = (double) x / y;
        return z;

    }
    public int scalingMod(int coreDim, int newDim, int scalar){
      double factor = (double) newDim/coreDim;
      double result = (double) scalar*factor;
      int resultProcessed = (int)result;
      return resultProcessed;
    }

    private ArrayList<Integer> returnCor(String source,ArrayList<String[]> table){//get coordinates of any given element
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(0);
        list.add(0);
        Integer xcor = 0;
        Integer ycor = 0;

        for(int i = 0; i < table.size(); i++) {

            if (table.get(i)[0].equals(source)) {

                xcor = Integer.valueOf(table.get(i)[1]);
                ycor = Integer.valueOf(table.get(i)[2]);

                //relative scaling
                //double xcorRatio = xcor/1393;
                float xcorRatio = (float) xcor/1393;

                //System.out.println(xcorRatio +"RATIOTATI~~~~~~~~~~~~~~~~~~~~~RATIO");
                //double ycorRatio = ycor/1692;
                float ycorRatio = (float) ycor/1692;

                Double xRatio = (double) xcorRatio;
                Double yRatio = (double) ycorRatio;

                list.set(0, xcor);
                list.set(1, (ycor));
                //System.out.println(source + " is equal to " + table.get(i)[0]);
                //System.out.println(xcor+"_"+ycor);
            }
        }
        return list;
    }
}