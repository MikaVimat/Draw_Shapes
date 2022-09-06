package com.samsung.drawshapes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.samsung.drawshapes.Dialogs.Settings;
import com.samsung.drawshapes.Drawing.ThreadForDrawing;
import com.samsung.drawshapes.ListAndRecyclerViews.ListViewAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SurfaceView _surfaceView ;
    private ListView   _listView;
    private Button  _clearCanvase;
    private Button  _settingsButton;
    private int _screenWidht;
    private int _screenHeight;
    private ArrayList<String> _shapesArray;
    private ListViewAdapter  _mListViewAdapter;
    private static FirebaseAnalytics _firebaseAnalytics;
    private Activity _thisActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _surfaceView =  findViewById(R.id.surfaceView);
        _listView   =  findViewById(R.id.selectShpaeList);
        _clearCanvase   =  findViewById(R.id.clear_button);
        _settingsButton   =  findViewById(R.id.settings_button);
        setLayoutSizes();
        _shapesArray =  new ArrayList<>();
        initShapes();
        ListView listView = findViewById(R.id.selectShpaeList);
        _mListViewAdapter = new ListViewAdapter(this, _shapesArray);
        listView.setAdapter(_mListViewAdapter);
        _firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        _thisActivity = this;

        _clearCanvase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadForDrawing.getThreadClassObject().clearCanvas();
                Bundle parameters = new Bundle();
                parameters.putString("Clear_pressed", "Cleared");
                _firebaseAnalytics.logEvent("Clear_pressed", parameters);

            }
        });

        _settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle parameters = new Bundle();
                parameters.putString("Settings_pressed", "Settings");
                _firebaseAnalytics.logEvent("Settings_pressed", parameters);

                Settings dialog = new Settings(_thisActivity);
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(), "Settings");
                ThreadForDrawing.needToDraw = false;

            }
        });
    }
    private void initShapes(){
        _shapesArray.add("Circle");
        _shapesArray.add("Rectangle");
        _shapesArray.add("Line");


    }

    private void setLayoutSizes() {
        getScreenSizes();
        Log.i("softButonnHeight" , getBottombuttonsHeight()+"");
        RelativeLayout.LayoutParams  surfaceViewLayoutParams = (RelativeLayout.LayoutParams) _surfaceView.getLayoutParams();
        RelativeLayout.LayoutParams  listViewLayoutParams = (RelativeLayout.LayoutParams) _listView.getLayoutParams();
        RelativeLayout.LayoutParams  clearCanvaseLayoutParams = (RelativeLayout.LayoutParams) _clearCanvase.getLayoutParams();
        RelativeLayout.LayoutParams  settingsButtonLayoutParams = (RelativeLayout.LayoutParams) _settingsButton.getLayoutParams();
        surfaceViewLayoutParams.height = (int) (_screenHeight* 0.68f);
        listViewLayoutParams.height = (int) (_screenHeight* 0.20f) ;
        clearCanvaseLayoutParams.height = settingsButtonLayoutParams.height = (int) (_screenHeight* 0.12f) ;
        int buttons_margin = (int) (_screenWidht* 0.05f);
        clearCanvaseLayoutParams.width = settingsButtonLayoutParams.width = (int) (_screenWidht* 0.50f)- 2*buttons_margin;
        clearCanvaseLayoutParams.setMargins(buttons_margin,0,buttons_margin,0);
        settingsButtonLayoutParams.setMargins(buttons_margin,0,buttons_margin,0);


    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    private int getBottombuttonsHeight() {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }


    private   void getScreenSizes(){
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(displayMetrics);

        // Since SDK_INT == 1
        _screenWidht = displayMetrics.widthPixels;
        _screenHeight = displayMetrics.heightPixels -getBottombuttonsHeight();

        // Includes window decorations (status bar / menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                _screenWidht = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                _screenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(display)-getBottombuttonsHeight();
            } catch (Exception ignored) {
            }
        }

        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                _screenWidht = realSize.x;
                _screenHeight = realSize.y -getBottombuttonsHeight();
            } catch (Exception ignored) {
            }
        }
    }
}