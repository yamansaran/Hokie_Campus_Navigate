package com.biaszebra.hokiecampusnavigate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.Toast;



import com.jsibbold.zoomage.ZoomageView;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;


//TODO change tableFile to relative units
//TODO add labeled map mode
//TODO add group edge interpolation
//TODO add language module

public class MainActivity extends AppCompatActivity {

    boolean accessibility = false;
    boolean hurry = false;
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
                Toast.makeText(this, "Options menu not implemented yet!", Toast.LENGTH_SHORT).show();
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
                hurryMode();
                if(hurry) {
                    Toast.makeText(this, "\"In A Hurry\" Mode enabled", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "\"In A Hurry\" Mode disabled", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void accessibilityMode() throws IOException {
       accessibility = !accessibility;
    }
    public void hurryMode(){
        hurry = !hurry;
    }

    private final int coreXDim = 2876;//TODO remove hard coding
    private final int coreYDim = 3437;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("");
        setContentView(R.layout.activity_main);
        Resources res = getResources();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, res.getStringArray(R.array.completion));
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.value1);
        textView.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, res.getStringArray(R.array.completion));
        AutoCompleteTextView textView2 = (AutoCompleteTextView) findViewById(R.id.value2);
        textView2.setAdapter(adapter);
    }

    public void onBtnClick(View view) {
        ZoomageView customView = findViewById(R.id.myZoomageView);
        TestWithSampleGraph runner = new TestWithSampleGraph();//create path generator

        AutoCompleteTextView locationText =findViewById(R.id.value1);
        AutoCompleteTextView destinationText =findViewById(R.id.value2);

        String locationString = locationText.getText().toString();
        locationString = locationString.replaceAll("\\s", "");
        String destinationString = destinationText.getText().toString();
        destinationString = destinationString.replaceAll("\\s", "");

        boolean loc = !doesExist(locationString);
        boolean dest = !doesExist(destinationString);

        if(loc||dest){

            Toast.makeText(this, "That location doesn not exist!", Toast.LENGTH_SHORT).show();
        }else{

        InputStream is;
        InputStream is2;
        if (accessibility) {
            is = this.getResources().openRawResource(R.raw.node_map_acc);//NOTE MAP LOCATION
        } else {
            if(hurry) {
                is = this.getResources().openRawResource(R.raw.node_map_quick);
            }else{
                is = this.getResources().openRawResource(R.raw.node_map);
            }
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
            }
        }
        String testTest = runner.startRun(locationString, destinationString, is);//find path
        testTest = testTest.replaceAll("\\s", "");//filter output path
        testTest = testTest.replaceAll("\\[", "");
        testTest = testTest.replaceAll("\\]", "");


        Bitmap newBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vt_campus_map_enlarged);//replacing image with copy image that will be painted on
        Bitmap myBitmap = Bitmap.createScaledBitmap(newBitmap, customView.getWidth(), customView.getHeight(), true);//I do not know why this is here, but it wont work without  copy of a copy
        Bitmap tempBitmap = myBitmap.copy(myBitmap.getConfig(), true);//copy as previous bitmaps are immutable
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);

        Paint p = new Paint();//route paint, edit here
        p.setColor(Color.RED);
        p.setStrokeWidth(6);

        Paint explorer = new Paint();//TODO remove explorer line
        explorer.setColor(Color.BLACK);
        explorer.setStrokeWidth(3);

        System.out.println("DEBUG"+testTest);
        String[] res = testTest.split("[,]", 0);//array of steps along path

        for (int i = 0; i < res.length; i++) {//loop to paint lines along map
            if (i == (res.length - 1)) {

            } else {
                ArrayList<Integer> coorCurrent = returnCor(res[i], table);
                System.out.println("DEBUG"+coorCurrent);
                ArrayList<Integer> coorNext = returnCor(res[i + 1], table);
                int xdim = tempBitmap.getWidth();
                int ydim = tempBitmap.getHeight();

                int xCur = (int) (coorCurrent.get(0));//paint start and end point
                System.out.println("DEBUG"+xCur);
                int yCur = (int) (coorCurrent.get(1));
                int xNext = (int) (coorNext.get(0));
                int yNext = (int) (coorNext.get(1));

                xCur = scalingMod(coreXDim, xdim, xCur);//scaled to make up for different screen resolutions
                yCur = scalingMod(coreYDim, ydim, yCur);
                xNext = scalingMod(coreXDim, xdim, xNext);
                yNext = scalingMod(coreYDim, ydim, yNext);

                System.out.println("From "+ xCur +" " + yCur + " To "+xNext + " "+yNext);
                tempCanvas.drawLine(xCur, yCur, xNext, yNext, p);
                tempCanvas.drawCircle((float)xNext,(float)yNext,(float)3,p);
            }
        }

        returnCor("A", table);
        customView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }
        locationText.getText().clear();
        destinationText.getText().clear();
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

                float xcorRatio = (float) xcor/2876;//TODO remove hard coded values

                float ycorRatio = (float) ycor/3437;

                Double xRatio = (double) xcorRatio;
                Double yRatio = (double) ycorRatio;

                list.set(0, xcor);
                list.set(1, (ycor));

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