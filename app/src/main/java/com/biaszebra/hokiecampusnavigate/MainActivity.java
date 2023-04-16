package com.biaszebra.hokiecampusnavigate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.jsibbold.zoomage.ZoomageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;


//TODO change table file to relative units


public class MainActivity extends AppCompatActivity {

    boolean accessibility = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:
                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:

                try {
                    accessibilityMode();
                    if(accessibility) {
                        Toast.makeText(this, "Accessibility Mode enabled", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Accessibility Mode disabled", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            case R.id.item3:
                Toast.makeText(this, "Item 3 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    public void accessibilityMode() throws IOException {
       accessibility = !accessibility;

    }

    private int coreXDim = 2876;//TODO remove hard coding
    private int coreYDim = 3437;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ZoomageView customView = findViewById(R.id.myZoomageView);
        //coreXDim = customView.getWidth();
        //coreYDim = customView.getHeight();
    }

    public void onBtnClick(View view) {
        //System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaa"+accessibility);
        ZoomageView customView = findViewById(R.id.myZoomageView);
        TextView pathView = findViewById(R.id.pathView);
        TestWithSampleGraph runner = new TestWithSampleGraph();//create path generator
        EditText location = findViewById(R.id.location);
        String locationString = location.getText().toString();
        EditText destination = findViewById(R.id.destination);
        String destinationString = destination.getText().toString();


        boolean loc = !doesExist(locationString);
        boolean dest = !doesExist(destinationString);
        System.out.println(locationString + loc+destinationString+dest+"============================");
        if(loc||dest){
            System.out.println("ending");
            Toast.makeText(this, "That location doesn not exist!", Toast.LENGTH_SHORT).show();
        }else{
        System.out.println("proceeding");
        InputStream is;
        InputStream is2;
        if (accessibility) {
            is = this.getResources().openRawResource(R.raw.node_map_acc);//NOTE MAP LOCATION
        } else {
            is = this.getResources().openRawResource(R.raw.node_map);
        }
        ArrayList<String[]> table = new ArrayList<>();//table to hold point information

        if (accessibility) {
            is2 = this.getResources().openRawResource(R.raw.sample_table_acc);//point coordinate data file
        } else {
            is2 = this.getResources().openRawResource(R.raw.sample_table);
        }


        try (Scanner sc = new Scanner(is2, StandardCharsets.UTF_8.name())) {
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                String[] res = str.split("[,]", 0);
                table.add(res);
                //System.out.println(res[0]+" "+res[1]+" "+res[2] + " added to table");
            }
        }
        String testTest = runner.startRun(locationString, destinationString, is);//find path
        //System.out.println(testTest + "~~~~~~~~~~~~~");
        testTest = testTest.replaceAll("\\s", "");//filter output path
        testTest = testTest.replaceAll("\\[", "");
        testTest = testTest.replaceAll("\\]", "");
        //System.out.println(testTest + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        pathView.setText(testTest);//show path in pathView


        //customView.onDrawCo( 0,0,100,100, canvas);
        //Bitmap myBitmap = BitmapFactory.decodeFile("res/drawable/simple_node_map_shrunk.png");

        Bitmap newBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vt_campus_map_enlarged);//replacing image with copy image that will be painted on
        Bitmap myBitmap = Bitmap.createScaledBitmap(newBitmap, customView.getWidth(), customView.getHeight(), true);//I do not know why this is here, but it wont work without  copy of a copy
        //Bitmap myBitmap = ((BitmapDrawable)customView.getDrawable()).getBitmap();
        //System.out.println(myBitmap.getHeight());
        Bitmap tempBitmap = myBitmap.copy(myBitmap.getConfig(), true);//copy as previous bitmaps are immutable
        //Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_graph_cropped);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);

        Paint p = new Paint();//route paint, edit here
        p.setColor(Color.RED);
        p.setStrokeWidth(8);

        Paint explorer = new Paint();//TODO remove explorer line
        explorer.setColor(Color.BLACK);
        explorer.setStrokeWidth(3);

        //tempCanvas.drawLine(0,0,tempBitmap.getWidth(),tempBitmap.getHeight(),explorer);//Explorer line to span image
        //tempCanvas.drawLine(1000,1000,2000,1000,p);
        //tempCanvas.drawLine(2000,1000,1500,1600,p);
        String[] res = testTest.split("[,]", 0);//array of steps along path
        //System.out.println(res[0] + " " + res[3]);
        //ArrayList<Integer> temp = returnCor(res[0]);
        for (int i = 0; i < res.length; i++) {//loop to paint lines along map
            //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+res[i]);
            if (i == (res.length - 1)) {

            } else {
                //System.out.println("PAINTPAINTPAINT");
                ArrayList<Integer> coorCurrent = returnCor(res[i], table);
                ArrayList<Integer> coorNext = returnCor(res[i + 1], table);
                int xdim = tempBitmap.getWidth();
                int ydim = tempBitmap.getHeight();
                //System.out.println(xdim + "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + ydim);
                //System.out.println(coreXDim + "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + coreYDim);


                int xCur = (int) (coorCurrent.get(0));//paint start and end point
                int yCur = (int) (coorCurrent.get(1));
                int xNext = (int) (coorNext.get(0));
                int yNext = (int) (coorNext.get(1));

                xCur = scalingMod(coreXDim, xdim, xCur);//scaled to make up for different screen resolutions
                yCur = scalingMod(coreYDim, ydim, yCur);
                xNext = scalingMod(coreXDim, xdim, xNext);
                yNext = scalingMod(coreYDim, ydim, yNext);

                tempCanvas.drawLine(xCur, yCur, xNext, yNext, p);
            }
        }

        returnCor("A", table);


        customView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }
    }

    public Double doubleDivision(Double x, Double y) {//real division
        Double z = (double) x / y;
        return z;

    }
    public int scalingMod(int coreDim, int newDim, int scalar){//resolution scaling
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
                float xcorRatio = (float) xcor/2876;

                //System.out.println(xcorRatio +"RATIOTATI~~~~~~~~~~~~~~~~~~~~~RATIO");
                //double ycorRatio = ycor/1692;
                float ycorRatio = (float) ycor/3437;

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
    public boolean doesExist(String string){
        InputStream is = this.getResources().openRawResource(R.raw.sample_table);
        boolean bool = false;

        try (Scanner sc = new Scanner(is, StandardCharsets.UTF_8.name())) {
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                String[] res = str.split("[,]", 0);
                if(res[0].equals(string)){
                    bool = true;
                }

            }
        }


        return bool;
    }
}