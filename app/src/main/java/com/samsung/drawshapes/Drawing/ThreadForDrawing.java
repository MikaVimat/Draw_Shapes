package com.samsung.drawshapes.Drawing;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.samsung.drawshapes.Dialogs.Settings;
import com.samsung.drawshapes.Logic.Shape;
import com.samsung.drawshapes.Logic.ShapeType;
import com.samsung.drawshapes.db.DbOps;
import com.samsung.drawshapes.db.MyColor;

import java.util.ArrayList;

public class ThreadForDrawing extends Thread{
    private Canvas _canvas;
    private SurfaceHolder _surfaceHolder;
    private Paint _bgPaint  = new Paint();
    private Paint _shapePaint  = new Paint();
    private static volatile boolean isRunning = true;
    private int firstX,firstY;
    private  boolean _needToClear = true;
    private  int _x,_y;
    private static  ShapeType shapeType = ShapeType.CIRCLE;
    private boolean isTouchReleased  = false;
    public static boolean isClearedCanvas  =  false;
    private static ThreadForDrawing _thisCalssObj;
    static ArrayList<Shape> _drawnShapesArray;
    private boolean isModyfiing  = false;
    int color = Color.RED ;
    public static boolean needToDraw = true;


    public ThreadForDrawing(Context context, SurfaceHolder holder, DrawView drawView) {
        _surfaceHolder = holder;
        _bgPaint.setColor(Color.WHITE);
        _bgPaint.setStyle(Paint.Style.FILL);
        _shapePaint.setColor(Color.RED);
        _shapePaint.setStyle(Paint.Style.FILL);
        _shapePaint.setStrokeWidth(10);
        _drawnShapesArray = new ArrayList<>();
        _thisCalssObj = this;


        DbOps db = new DbOps(context.getApplicationContext());
        ArrayList<MyColor> allColors  = db.getColors();
        if(allColors.size() !=  0 ) {
            color = db.getColors().get(0).currentSelectedColor;
        }

        _shapePaint.setColor(color);

    }
    public static  void askToStopTheThread() {
        isRunning = false;
    }

    public void drawOnCanvas(){

            try {
                _shapePaint.setColor(Settings._selectedColor);
                _canvas = _surfaceHolder.lockCanvas();

                if (_canvas != null && _x < _canvas.getWidth() && _y < _canvas.getHeight()) {

                    _canvas.drawRect(0, 0, _canvas.getWidth(), _canvas.getHeight(), _bgPaint);



                    // _canvas.drawRect(firstX, firstY, _x, _y, _shapePaint);
                    if (shapeType != null && DrawView.isMouseDown && !isClearedCanvas) {
                        switch (shapeType) {
                            case RECTANGLE:
                                _canvas.drawRect(firstX, firstY, _x, _y, _shapePaint);
                                Shape shape = new Shape();
                                shape.shapeType = ShapeType.RECTANGLE;
                                shape._firstX = firstX;
                                shape._firstY = firstY;
                                shape._x = _x;
                                shape._y = _y;
                                shape.color = Settings._selectedColor;
                                if (DrawView.isFignerUp) {
                                    _drawnShapesArray.add(shape);
                                    isModyfiing = true;
                                }
                                break;
                            case CIRCLE:
                                _canvas.drawCircle(firstX, firstY, Math.abs(_x - firstX), _shapePaint);
                                Shape shapeCircle = new Shape();
                                shapeCircle.shapeType = ShapeType.CIRCLE;
                                shapeCircle._firstX = firstX;
                                shapeCircle._firstY = firstY;
                                shapeCircle._x = _x;
                                shapeCircle._y = _y;
                                shapeCircle.color = Settings._selectedColor;
                                if (DrawView.isFignerUp) {
                                    _drawnShapesArray.add(shapeCircle);
                                    isModyfiing = true;
                                }
                                break;
                            case LINE:
                                _canvas.drawLine(firstX, firstY, _x, _y, _shapePaint);
                                Shape shapeLine = new Shape();
                                shapeLine.shapeType = ShapeType.LINE;
                                shapeLine._firstX = firstX;
                                shapeLine._firstY = firstY;
                                shapeLine._x = _x;
                                shapeLine._y = _y;
                                shapeLine.color = Settings._selectedColor;

                                if (DrawView.isFignerUp) {
                                    _drawnShapesArray.add(shapeLine);
                                    isModyfiing = true;
                                }
                                break;
                        }
                    }

                    if (!isClearedCanvas && _drawnShapesArray.size() > 0) {

                        for (Shape shape : _drawnShapesArray) {
                            switch (shape.shapeType) {
                                case RECTANGLE:
                                    _shapePaint.setColor(shape.color);
                                    _canvas.drawRect(shape._firstX, shape._firstY, shape._x, shape._y, _shapePaint);
                                    break;
                                case CIRCLE:
                                    _shapePaint.setColor(shape.color);
                                    _canvas.drawCircle(shape._firstX, shape._firstY, Math.abs(shape._x - shape._firstX), _shapePaint);
                                    break;
                                case LINE:
                                    _shapePaint.setColor(shape.color);
                                    _canvas.drawLine(shape._firstX, shape._firstY, shape._x, shape._y, _shapePaint);
                                    break;

                            }
                        }
                        // isModyfiing = true;
                    }
                    //    System.out.println(firstX + " " + firstY);

                }
            } finally {
                if (_canvas != null) {
                    _surfaceHolder.unlockCanvasAndPost(_canvas);
                }
            }


        }



    @Override
    public void run() {
      //  super.run();     u7

        while (isRunning){
            while(needToDraw) {
                drawOnCanvas();
            }
        }

     }

    public static void clearCanvas(){



        isClearedCanvas = true;
        _drawnShapesArray = new ArrayList<>();
     //   _drawnShapesArray.clear();

    }
    public static  ThreadForDrawing getThreadClassObject(){
        return _thisCalssObj;
    }


    public void pressedCoordinates(int x, int y) {
      //  System.out.println("x= "+x + "y= " +y);
        _x = x;
        _y = y;
    }

    public void actionDownCoordinates(int x, int y) {
        firstX = x;
        firstY = y;
        isClearedCanvas = false;

    }

    public  static  void setShapeType(ShapeType selectedShapeType){
        shapeType = selectedShapeType;
    }
}
