package com.example.task2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedHashMap;
import java.util.Map;


public class GameScreen extends View {

    private static final String TAG = "GameScreen";

    static int boxes;
    Paint blackFill;
    Paint redStroke;
    Paint blueStroke;
    Paint redFill;
    Paint blueFill;
    Paint scoreText;
    Paint turnText;
    MainActivity mainActivity;
    static double boxDistance;
    static double canvasHeight;
    static double canvasWidth;
    static float freeSpace;
    boolean turn1 = true;
    boolean turn2 = false;
    Path path1 = new Path();
    Path path2 = new Path();
    Path square1 = new Path();
    Path square2 = new Path();
    Map<Integer, Boolean> horLinesCheck;
    Map<Integer, Boolean> verLinesCheck;
    int score1;
    int score2;
    int boxesCompleted = 0;
    boolean gameOver = false;
    boolean pointScored = false;

    public GameScreen(Context context) {
        super(context);
        setBackgroundResource(R.drawable.paperbg);
        mainActivity = new MainActivity();
        boxes = mainActivity.getMode();
        //Log.d(TAG, "Boxes: " + mainActivity.getMode());

        blackFill = new Paint();
        blackFill.setColor(Color.BLACK);
        blackFill.setStyle(Paint.Style.FILL);
        blackFill.setAntiAlias(true);

        redStroke = new Paint();
        redStroke.setColor(Color.RED);
        redStroke.setStyle(Paint.Style.STROKE);
        redStroke.setStrokeWidth(10);
        redStroke.setAntiAlias(true);

        blueStroke = new Paint();
        blueStroke.setColor(Color.BLUE);
        blueStroke.setStyle(Paint.Style.STROKE);
        blueStroke.setStrokeWidth(10);
        blueStroke.setAntiAlias(true);

        redFill = new Paint();
        redFill.setColor(Color.RED);
        redFill.setStyle(Paint.Style.FILL);
        redFill.setAntiAlias(true);

        blueFill = new Paint();
        blueFill.setColor(Color.BLUE);
        blueFill.setStyle(Paint.Style.FILL);

        scoreText = new Paint();
        scoreText.setColor(Color.BLACK);
       // scoreText.setTextSize(80);
        scoreText.setAntiAlias(true);

        turnText = new Paint();
        turnText.setColor(Color.BLACK);
       // turnText.setTextSize(40);
        turnText.setAntiAlias(true);

        horLinesCheck = new LinkedHashMap<>();
        verLinesCheck = new LinkedHashMap<>();

        for(int t = 0; t <= boxes*(boxes+1) - 1; t++) {
            horLinesCheck.put(t, false);
            verLinesCheck.put(t, false);
        }

        score1 = 0;
        score2 = 0;


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                for(float x = (int) (boxDistance), dotX = 0; x <= boxDistance * (boxes + 1); x += boxDistance, dotX++) {
                    for(float y = (int) (canvasHeight - boxDistance * (boxes + 1)), dotY = 0; y <= canvasHeight - boxDistance; y += boxDistance, dotY++) {

                            if(x == boxDistance * (boxes + 1) && y != canvasHeight - boxDistance ) {
                                if(xPos <= (x + 30) && xPos >= (x - 30) && yPos > (y + 20) && yPos < (y + boxDistance - 20)) {
                                    if(!checkVerLineExists((int) dotX, (int) dotY)) {
                                        verticalLine(x, y);
                                        verticalLineMarker((int) dotX, (int) dotY);
                                        if(!pointScored)
                                            switchTurn();
                                    }
                                }
                            }

                            else if(y == canvasHeight - boxDistance && x != boxDistance * (boxes + 1)) {
                                if(yPos <= (y + 30) && yPos >= (y - 30) && xPos > (x + 20) && xPos < (x + boxDistance - 20)) {
                                    if(!checkHorLineExists((int) dotX, (int) dotY)) {
                                        horizontalLine(x, y);
                                        horizontalLineMarker((int) dotX, (int) dotY);
                                        if(!pointScored)
                                            switchTurn();
                                    }
                                }
                            }

                            else if(x != boxDistance * (boxes + 1) && y != canvasHeight - boxDistance){
                                if(xPos <= (x + 30) && xPos >= (x - 30) && yPos > (y + 20) && yPos < (y + boxDistance - 20)) {
                                    if(!checkVerLineExists((int) dotX, (int) dotY)) {
                                        verticalLine(x, y);
                                        verticalLineMarker((int) dotX, (int) dotY);
                                        if(!pointScored)
                                            switchTurn();
                                    }
                                }
                                else if(yPos <= (y + 30) && yPos >= (y - 30) && xPos > (x + 20) && xPos < (x + boxDistance - 20)) {
                                    if(!checkHorLineExists((int) dotX, (int) dotY)) {
                                        horizontalLine(x, y);
                                        horizontalLineMarker((int) dotX, (int) dotY);
                                        if(!pointScored)
                                            switchTurn();
                                    }
                                }
                            }

                        }
                    }
                    postInvalidate();
                    return true;

            default:
                return false;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        //Log.d(TAG, "onDraw: ch" + canvasHeight);
        //Log.d(TAG, "onDraw: cw" + canvasWidth);
        boxDistance = canvasWidth / (boxes + 2);
        freeSpace = (float)(canvasHeight - boxDistance*(boxes + 1));

        canvas.drawPath(square1, blueFill);
        canvas.drawPath(square2, redFill);
        canvas.drawPath(path1, blueStroke);
        canvas.drawPath(path2, redStroke);

        for(float x = (int) (boxDistance); x <= boxDistance * (boxes + 1); x += boxDistance) {
            for(float y = (int) (canvasHeight - boxDistance * (boxes + 1)); y <= canvasHeight - boxDistance; y += boxDistance) {
                canvas.drawCircle(x, y,10,blackFill);
            }
        }

        scoreText.setTextSize((float)(canvasWidth / 13.5));
        turnText.setTextSize(scoreText.getTextSize() / 2);

        canvas.drawText("Player 1:  " + score1, (float)(canvasWidth / 21.6), freeSpace / 2, scoreText);
        canvas.drawText("Player 2:  " + score2, (float)(canvasWidth/2 + canvasWidth / 21.6), freeSpace / 2, scoreText);

        if(turn1)
            canvas.drawText("(Your Turn)", (float)(canvasWidth / 15.42), (float)(freeSpace / 2 + canvasHeight / 22.6), turnText);
        else if(turn2)
            canvas.drawText("(Your Turn)", (float)(canvasWidth/2 + canvasWidth / 15.42), (float)(freeSpace / 2 + canvasHeight / 22.6), turnText);

        if(gameOver) {
            if(score1 > score2)
                canvas.drawText("Player 1 Wins!", (float)(canvasWidth/2 - canvasWidth/4.32), (float)(freeSpace - canvasHeight/8.132), scoreText);
            else if(score2 > score1)
                canvas.drawText("Player 2 Wins!", (float)(canvasWidth/2 - canvasWidth/4.32), (float)(freeSpace - canvasHeight/8.132), scoreText);
            else
                canvas.drawText("Draw", (float)(canvasWidth/2 - canvasWidth/10.8), (float)(freeSpace - canvasHeight/8.132), scoreText);
        }

    }

    public void verticalLine(float x, float y) {
        float startX = x;
        float startY = y;
        float endX = x;
        float endY = (float) (y + boxDistance);

        if(turn1) {
            path1.moveTo(startX, startY);
            path1.lineTo(endX, endY);
        }
        else if(turn2) {
            path2.moveTo(startX, startY);
            path2.lineTo(endX, endY);
        }
    }

    public void horizontalLine(float x, float y) {
        float startY = y;
        float endY = y;
        float startX = x;
        float endX = (float) (x + boxDistance);


        if(turn1) {
            path1.moveTo(startX, startY);
            path1.lineTo(endX, endY);
        }
        else if(turn2) {
            path2.moveTo(startX, startY);
            path2.lineTo(endX, endY);
        }

    }

    public void verticalLineMarker(int X, int Y) {
        int lineNumber = boxes*X + Y;
        verLinesCheck.put(lineNumber, true);
        //Log.d(TAG, "verticalLineMarker: " + lineNumber);
        verLineBoxer(lineNumber, X, Y);
    }

    public void horizontalLineMarker(int X, int Y) {
        int lineNumber = X + boxes*Y;
        horLinesCheck.put(lineNumber, true);
        //Log.d(TAG, "horizontalLineMarker: " + lineNumber);
        horLineBoxer(lineNumber, X, Y);
    }

    public void horLineBoxer(int lineNo, int x, int y) {
        pointScored = false;
        if(lineNo >= boxes) {
            int leftSide = boxes*x + y - 1;
            int rightSide = boxes*(x + 1) + y - 1;
            int top = x + boxes*(y - 1);
            if(verLinesCheck.get(leftSide) && verLinesCheck.get(rightSide) && horLinesCheck.get(top)) {
                drawSquare(x, y-1);
                pointScored = true;
                Log.d(TAG, "horLineBoxer: point scored true");
            }
        }
        if(lineNo < boxes*boxes) {
            int leftSide = boxes*x + y;
            int rightSide = boxes*(x + 1) + y;
            int bottom = x + boxes*(y + 1);
            if(verLinesCheck.get(leftSide) && verLinesCheck.get(rightSide) && horLinesCheck.get(bottom)) {
                drawSquare(x, y);
                pointScored = true;
                Log.d(TAG, "horLineBoxer: point scored true");
            }
        }
    }

    public void verLineBoxer(int lineNo, int x, int y) {
        pointScored = false;
        if(lineNo >= boxes) {
            int top = x + boxes*y - 1;
            int bottom = x + boxes*(y + 1) - 1;
            int leftSide = boxes*(x - 1) + y;
            if(horLinesCheck.get(top) && horLinesCheck.get(bottom) && verLinesCheck.get(leftSide)) {
                drawSquare(x-1, y);
                pointScored = true;
                Log.d(TAG, "verLineBoxer: point scored true");
            }
        }
        if(lineNo < boxes*boxes) {
            int top = x + boxes*y;
            int bottom = x + boxes*(y + 1);
            int rightSide = boxes*(x + 1) + y;
            if(horLinesCheck.get(top) && horLinesCheck.get(bottom) && verLinesCheck.get(rightSide)) {
                drawSquare(x, y);
                pointScored = true;
                Log.d(TAG, "verLineBoxer: point scored true");
            }
        }
    }

    public void drawSquare(int dotX, int dotY) {
        //Log.d(TAG, "drawSquare: reached");
        boxesCompleted ++;
        float startX = (float) (boxDistance*(dotX + 1));
        float startY = (float) (canvasHeight - boxDistance*(boxes - dotY + 1));
        if(turn1) {
            square1.moveTo(startX + 20, startY + 20);
            square1.lineTo((float)(startX + boxDistance - 20), startY + 20);
            square1.lineTo((float)(startX + boxDistance - 20), (float)(startY + boxDistance - 20));
            square1.lineTo(startX + 20, (float)(startY + boxDistance) - 20);
            square1.lineTo(startX + 20, startY + 20);
            //Log.d(TAG, "drawSquare: drawn1");
            score1 ++;
            //turn2 = true;
            //turn1 = false;
        }
        else if(turn2) {
            square2.moveTo(startX + 20, startY + 20);
            square2.lineTo((float)(startX + boxDistance - 20), startY + 20);
            square2.lineTo((float)(startX + boxDistance - 20), (float)(startY + boxDistance - 20));
            square2.lineTo(startX + 20, (float)(startY + boxDistance) - 20);
            square2.lineTo(startX + 20, startY + 20);
            //Log.d(TAG, "drawSquare: drawn2");
            score2 ++;
            //turn1 = true;
            //turn2 = false;
        }

        if(boxesCompleted == boxes*boxes)
            gameOver = true;
    }

    public boolean checkVerLineExists(int X, int Y) {
        int lineNo = boxes*X + Y;
        if(!verLinesCheck.get(lineNo)) {
            return false;
        }
        return true;
    }

    public boolean checkHorLineExists(int X, int Y) {
        int lineNo = X + boxes*Y;
        if(!horLinesCheck.get(lineNo)) {
            return false;
        }
        return true;
    }

    public void switchTurn() {
        if(turn1) {
            turn1 = false;
            turn2 = true;
            Log.d(TAG, "switchTurn: 1 to 2");
        }
        else if(turn2) {
            turn1 = true;
            turn2 = false;
            Log.d(TAG, "switchTurn: 2 to 1");
        }
    }

}
