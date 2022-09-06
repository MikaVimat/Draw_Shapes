package com.samsung.drawshapes.Dialogs;

import static android.content.Context.WINDOW_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samsung.drawshapes.Drawing.ThreadForDrawing;
import com.samsung.drawshapes.ListAndRecyclerViews.RecyclerViewAdapter;
import com.samsung.drawshapes.R;
import com.samsung.drawshapes.db.DbHelper;
import com.samsung.drawshapes.db.DbOps;

import eltos.simpledialogfragment.SimpleDialog;
import eltos.simpledialogfragment.color.SimpleColorDialog;
import eltos.simpledialogfragment.color.SimpleColorWheelDialog;
import yuku.ambilwarna.AmbilWarnaDialog;


public class Settings extends DialogFragment{


    private Button _addNewColorButton;
    private TextView _colorCodeText;
    private Button _useColorButton;
    private Button _deleteButton;
    private Button _cancelButton;
    private RecyclerView _recyclerListOfColors;
    private EditText _colorNameEditTxt;
    private Activity _calledFromActivity;
    private int _screenWidht;
    private int _screenHeight;
    private  DbOps dbOps;
    private RecyclerViewAdapter _adapter;
    public static int _selectedColor = Color.RED;
    private static int _selectedTempColor;
    private static int _idToDelete;
    private static  boolean isColorSelected = false;

    public Settings(Activity activity) {
        super();
        _calledFromActivity = activity;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View myView;
        myView = inflater.inflate(R.layout.settings_dialog, null);
        _addNewColorButton = myView.findViewById(R.id.add_new_color_button);
        _colorNameEditTxt = myView.findViewById(R.id.enterColorNameEdit);
       // _colorCodeText = myView.findViewById(R.id.color_code_txt);
        _useColorButton = myView.findViewById(R.id.useColorButton);
        _deleteButton = myView.findViewById(R.id.delete_button);
        _cancelButton = myView.findViewById(R.id.cancel_button);
        _recyclerListOfColors = myView.findViewById(R.id.recycler_colors_list);

         dbOps = new DbOps(_calledFromActivity);

         if(dbOps.getColors().size() == 0){
             dbOps.insert("Red",_selectedColor);
         }

         _useColorButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 _selectedColor = _selectedTempColor;
                 ThreadForDrawing.needToDraw = true;
                 // Check if no view has focus:
                 hideKeyboardFrom(getContext(),_colorNameEditTxt);
                 dismiss();
             }
         });
        _deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("idtodelete" +_idToDelete);
                if(isColorSelected) {
                    isColorSelected = false;
                    dbOps.delete(dbOps.getColorIdByValue(_selectedTempColor));
                    _adapter = new RecyclerViewAdapter(dbOps.getColors(), _calledFromActivity);
                    _recyclerListOfColors.setHasFixedSize(true);
                    _recyclerListOfColors.setLayoutManager(new LinearLayoutManager(_calledFromActivity));
                    _recyclerListOfColors.setAdapter(_adapter);
                }
                else {
                    Toast.makeText(getContext(),"Select color to delete",Toast.LENGTH_SHORT).show();
                }

            }
        });
         _cancelButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 ThreadForDrawing.needToDraw = true;
                 dismiss();
             }
         });



        _addNewColorButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

             hideKeyboardFrom(getContext(),_colorNameEditTxt);
                Log.i("dialogHints", "Close called");
                // GamePlay.selectFillButton();
/*              SimpleColorWheelDialog.build()
                        .title("pickme")
                        .msg("hihi")
                        .show(getActivity(),"tag");

                SimpleColorDialog.build()
                        .title("pickme")
                        .msg("hihi")
                        .show(getActivity(),"tag");*

 */
                openColorPickerDialogue();
                //dismiss();
                // DrawThread.retDraWThread().onRestartGame();


            }
        });
        builder.setView(myView);
        AlertDialog dialog = builder.create();


        return dialog;
    }


        public void setSizes () {
            int dialogW = (int) (_screenWidht* 0.90f);
            int dialogH = (int) (_screenHeight * 0.60f);
            int buttonsMargine;
           getDialog().getWindow().setLayout(dialogW, dialogH);

            RelativeLayout.LayoutParams addNewColorButtonLayoutParams = (RelativeLayout.LayoutParams) _addNewColorButton.getLayoutParams();
            RelativeLayout.LayoutParams colorNameEditTxtLayoutParams = (RelativeLayout.LayoutParams) _colorNameEditTxt.getLayoutParams();
            //RelativeLayout.LayoutParams colorCodeTextLayoutParams = (RelativeLayout.LayoutParams) _colorCodeText.getLayoutParams();
            RelativeLayout.LayoutParams recyclerListOfColorsLayoutParams = (RelativeLayout.LayoutParams) _recyclerListOfColors.getLayoutParams();
            RelativeLayout.LayoutParams useColorButtonLayoutParams = (RelativeLayout.LayoutParams) _useColorButton.getLayoutParams();
            RelativeLayout.LayoutParams deleteButtonLayoutParams = (RelativeLayout.LayoutParams) _deleteButton.getLayoutParams();
            RelativeLayout.LayoutParams cancelButtonLayoutParams = (RelativeLayout.LayoutParams) _cancelButton.getLayoutParams();
            addNewColorButtonLayoutParams.height = (int) (dialogH*0.12f);
            colorNameEditTxtLayoutParams.height = (int) (dialogH*0.12f);
            addNewColorButtonLayoutParams.width  =  colorNameEditTxtLayoutParams.width = (int) (dialogW *0.50f);
          //  colorCodeTextLayoutParams.height = (int) (dialogH*0.20f);
            recyclerListOfColorsLayoutParams.height = (int) (dialogH*0.68f);
             useColorButtonLayoutParams.height = (int) (dialogH*0.10f);
             deleteButtonLayoutParams.height = (int) (dialogH*0.10f);
             cancelButtonLayoutParams.height = (int) (dialogH*0.10f);
             buttonsMargine = (int) (dialogW*0.02f);
            useColorButtonLayoutParams.width = deleteButtonLayoutParams.width = cancelButtonLayoutParams.width = (int) (dialogW*0.30f);
            useColorButtonLayoutParams.setMarginEnd(buttonsMargine);
            deleteButtonLayoutParams.setMarginEnd(buttonsMargine);

        }
    public void openColorPickerDialogue() {

        // the AmbilWarnaDialog callback needs 3 parameters
        // one is the context, second is default color,
        final AmbilWarnaDialog colorPickerDialogue = new AmbilWarnaDialog(_calledFromActivity, 0,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // leave this function body as
                        // blank, as the dialog
                        // automatically closes when
                        // clicked on cancel button
                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        System.out.println("coloris" +color);
                        // change the mDefaultColor to
                        // change the GFG text color as
                        // it is returned when the OK
                        // button is clicked from the
                        // color picker dialog
                      //  mDefaultColor = color;

                        // now change the picked color
                        // preview box to mDefaultColor
                        if(_colorNameEditTxt.getText().toString().equals("")){
                            Toast.makeText(getContext(),getString(R.string.enterName),Toast.LENGTH_SHORT).show();
                            return;
                        }
                      //  _selectedColor.setBackgroundColor(color);
                        dbOps.insert(_colorNameEditTxt.getText().toString(),color);
                        _adapter = new RecyclerViewAdapter(dbOps.getColors(),_calledFromActivity);
                        _recyclerListOfColors.setHasFixedSize(true);
                        _recyclerListOfColors.setLayoutManager(new LinearLayoutManager(_calledFromActivity));
                        _recyclerListOfColors.setAdapter(_adapter);


                    }
                });
        colorPickerDialogue.show();
    }


        private void getScreenSizes () {
            WindowManager wm = (WindowManager) _calledFromActivity.getSystemService(WINDOW_SERVICE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            Display display = wm.getDefaultDisplay();
            display.getMetrics(displayMetrics);

            // Since SDK_INT == 1
            _screenWidht = displayMetrics.widthPixels;
            _screenHeight = displayMetrics.heightPixels;

            // Includes window decorations (status bar / menu bar)
            if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
                try {
                    _screenWidht = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                    _screenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
                } catch (Exception ignored) {
                }
            }

            if (Build.VERSION.SDK_INT >= 17) {
                try {
                    Point realSize = new Point();
                    Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                    _screenWidht = realSize.x;
                    _screenHeight = realSize.y;
                } catch (Exception ignored) {
                }
            }
        }

    @Override
    public void onStart() {
        super.onStart();
        getScreenSizes();
        setSizes();


        _adapter = new RecyclerViewAdapter(dbOps.getColors(),_calledFromActivity);
        _recyclerListOfColors.setHasFixedSize(true);
        _recyclerListOfColors.setLayoutManager(new LinearLayoutManager(_calledFromActivity));
        _recyclerListOfColors.setAdapter(_adapter);
    }


    public static void setSelectedColor(int color){
        _selectedTempColor = color;
        isColorSelected = true;
    }

    public static void SetIdToDelete(int idTodelete){

        _idToDelete = idTodelete;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

