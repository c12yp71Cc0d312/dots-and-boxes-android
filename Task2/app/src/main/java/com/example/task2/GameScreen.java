package com.example.task2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class GameScreen extends View {

    private static final String TAG = "GameScreen";

    static int boxes;
    static int players;
    Paint blackFill;
    Paint redStroke;
    Paint blueStroke;
    Paint magentaStroke;
    Paint dkGrayStroke;
    Paint redFill;
    Paint blueFill;
    Paint magentaFill;
    Paint dkGrayFill;
    Paint scoreText;
    Paint turnText;
    MainActivity mainActivity;
    static double boxDistance;
    static double canvasHeight;
    static double canvasWidth;
    static float freeSpace;
    boolean turn1 = true;
    boolean turn2 = false;
    boolean turn3 = false;
    boolean turn4 = false;
    Path path1 = new Path();
    Path path2 = new Path();
    Path path3 = new Path();
    Path path4 = new Path();
    Path square1 = new Path();
    Path square2 = new Path();
    Path square3 = new Path();
    Path square4 = new Path();
    Map<Integer, Boolean> horLinesCheck;
    Map<Integer, Boolean> verLinesCheck;
    int score1, score2, score3, score4;
    int boxesCompleted = 0;
    boolean gameOver = false;
    boolean pointScored = false;
    ArrayList<Path> pathHistory;
    Bitmap undo;

    public GameScreen(Context context, MainActivity mainActivity) {
        super(context);
        setBackgroundResource(R.drawable.paperbg);
        this.mainActivity = mainActivity;
        boxes = mainActivity.getMode();
        players = mainActivity.getPlayers();
        pathHistory = new ArrayList<>();

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

        magentaStroke = new Paint();
        magentaStroke.setColor(Color.MAGENTA);
        magentaStroke.setStyle(Paint.Style.STROKE);
        magentaStroke.setStrokeWidth(10);
        magentaStroke.setAntiAlias(true);

        dkGrayStroke = new Paint();
        dkGrayStroke.setColor(Color.DKGRAY);
        dkGrayStroke.setStyle(Paint.Style.STROKE);
        dkGrayStroke.setStrokeWidth(10);
        dkGrayStroke.setAntiAlias(true);

        redFill = new Paint();
        redFill.setColor(Color.RED);
        redFill.setStyle(Paint.Style.FILL);
        redFill.setAntiAlias(true);
        redFill.setAlpha(64);

        blueFill = new Paint();
        blueFill.setColor(Color.BLUE);
        blueFill.setStyle(Paint.Style.FILL);
        blueFill.setAntiAlias(true);
        blueFill.setAlpha(64);

        magentaFill = new Paint();
        magentaFill.setColor(Color.MAGENTA);
        magentaFill.setStyle(Paint.Style.FILL);
        magentaFill.setAntiAlias(true);
        magentaFill.setAlpha(64);

        dkGrayFill = new Paint();
        dkGrayFill.setColor(Color.DKGRAY);
        dkGrayFill.setStyle(Paint.Style.FILL);
        dkGrayFill.setAntiAlias(true);
        dkGrayFill.setAlpha(64);

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
        score3 = 0;
        score4 = 0;

        undo = BitmapFactory.decodeResource(getResources(),R.drawable.undo1);
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

                    if(xPos >= canvasWidth - 120 && yPos <= 71) {
                        pathHistory.remove(pathHistory.size()-1);
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
        boxDistance = canvasWidth / (boxes + 2);
        freeSpace = (float)(canvasHeight - boxDistance*(boxes + 1));

        canvas.drawBitmap(undo, (float)(canvasWidth-150), 20, null);
        drawLinesAndBoxes(canvas);
        drawDots(canvas);
        drawScores(canvas);

    }

    public void verticalLine(float x, float y) {
        float startX = x;
        float startY = y;
        float endX = x;
        float endY = (float) (y + boxDistance);

        if(turn1) {
            path1.moveTo(startX, startY);
            path1.lineTo(endX, endY);
            pathHistory.add(path1);
        }
        else if(turn2) {
            path2.moveTo(startX, startY);
            path2.lineTo(endX, endY);
            pathHistory.add(path2);
        }
        else if(turn3) {
            path3.moveTo(startX, startY);
            path3.lineTo(endX, endY);
            pathHistory.add(path3);
        }
        else if(turn4) {
            path4.moveTo(startX, startY);
            path4.lineTo(endX, endY);
            pathHistory.add(path4);
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
            pathHistory.add(path1);
        }
        else if(turn2) {
            path2.moveTo(startX, startY);
            path2.lineTo(endX, endY);
            pathHistory.add(path2);
        }
        else if(turn3) {
            path3.moveTo(startX, startY);
            path3.lineTo(endX, endY);
            pathHistory.add(path3);
        }
        else if(turn4) {
            path4.moveTo(startX, startY);
            path4.lineTo(endX, endY);
            pathHistory.add(path4);
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
            square1.moveTo(startX, startY);
            square1.lineTo((float)(startX + boxDistance), startY);
            square1.lineTo((float)(startX + boxDistance), (float)(startY + boxDistance));
            square1.lineTo(startX, (float)(startY + boxDistance));
            square1.lineTo(startX, startY);
            //Log.d(TAG, "drawSquare: drawn1");
            score1 ++;
            //turn2 = true;
            //turn1 = false;
            pathHistory.add(square1);
        }
        else if(turn2) {
            square2.moveTo(startX, startY);
            square2.lineTo((float)(startX + boxDistance), startY);
            square2.lineTo((float)(startX + boxDistance), (float)(startY + boxDistance));
            square2.lineTo(startX, (float)(startY + boxDistance));
            square2.lineTo(startX, startY);
            //Log.d(TAG, "drawSquare: drawn2");
            score2 ++;
            //turn1 = true;
            //turn2 = false;
            pathHistory.add(square2);
        }
        else if(turn3) {
            square3.moveTo(startX, startY);
            square3.lineTo((float)(startX + boxDistance), startY);
            square3.lineTo((float)(startX + boxDistance), (float)(startY + boxDistance));
            square3.lineTo(startX, (float)(startY + boxDistance));
            square3.lineTo(startX, startY);
            //Log.d(TAG, "drawSquare: drawn2");
            score3 ++;
            pathHistory.add(square3);
        }
        else if(turn4) {
            square4.moveTo(startX, startY);
            square4.lineTo((float)(startX + boxDistance), startY);
            square4.lineTo((float)(startX + boxDistance), (float)(startY + boxDistance));
            square4.lineTo(startX, (float)(startY + boxDistance));
            square4.lineTo(startX, startY);
            //Log.d(TAG, "drawSquare: drawn2");
            score4 ++;
            pathHistory.add(square4);
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

        if(players == 2) {
            if(turn1) {
                turn1 = false;
                turn2 = true;
            }
            else if(turn2) {
                turn1 = true;
                turn2 = false;
            }
        }
        else if(players == 3) {
            if(turn1) {
                turn1 = false;
                turn2 = true;
            }
            else if(turn2) {
                turn2 = false;
                turn3 = true;
            }
            else if(turn3) {
                turn1 = true;
                turn3 = false;
            }
        }
        else if (players ==  4) {
            if(turn1) {
                turn1 = false;
                turn2 = true;
            }
            else if(turn2) {
                turn2 = false;
                turn3 = true;
            }
            else if(turn3) {
                turn3 = false;
                turn4 = true;
            }
            else if(turn4) {
                turn1 = true;
                turn4 = false;
            }
        }

    }

    public void drawDots (Canvas canvas){
        for(float x = (int) (boxDistance); x <= boxDistance * (boxes + 1); x += boxDistance) {
            for(float y = (int) (canvasHeight - boxDistance * (boxes + 1)); y <= canvasHeight - boxDistance; y += boxDistance) {
                canvas.drawCircle(x, y,10,blackFill);
            }
        }
    }

    public void drawLinesAndBoxes (Canvas canvas) {
        canvas.drawPath(square1, redFill);
        canvas.drawPath(square2, blueFill);
        canvas.drawPath(square3, dkGrayFill);
        canvas.drawPath(square4, magentaFill);
        canvas.drawPath(path1, redStroke);
        canvas.drawPath(path2, blueStroke);
        canvas.drawPath(path3, dkGrayStroke);
        canvas.drawPath(path4, magentaStroke);
    }

    public void drawScores (Canvas canvas) {
        scoreText.setTextSize((float) (canvasWidth / 13.5));
        turnText.setTextSize(scoreText.getTextSize() / 2);

        float y1div, y2div, y3div = 3, y4div = 3;
        double y5div;
        int winner = 0;

        if(players == 2) {
            y1div = 2;
            y2div = 2;
            y5div = 8.132;
        }

        else {
            y1div = 4;
            y2div = 4;
            y5div = 13.55;
        }

        canvas.drawText("Player 1:  " + score1, (float) (canvasWidth / 21.6), freeSpace / y1div, scoreText);
        canvas.drawText("Player 2:  " + score2, (float) (canvasWidth / 2 + canvasWidth / 21.6), freeSpace / y2div, scoreText);
        if(players >= 3) {
            canvas.drawText("Player 3:  " + score3, (float) (canvasWidth / 21.6), (freeSpace * 2) / y3div, scoreText);
        }

        if(players == 4) {
            canvas.drawText("Player 4:  " + score4, (float) (canvasWidth / 2 + canvasWidth / 21.6), (freeSpace*2) / y4div, scoreText);
        }

        if (turn1)
            canvas.drawText("(Your Turn)", (float) (canvasWidth / 15.42), (float) (freeSpace / y1div + canvasHeight / 22.6), turnText);
        else if (turn2)
            canvas.drawText("(Your Turn)", (float) (canvasWidth / 2 + canvasWidth / 15.42), (float) (freeSpace / y2div + canvasHeight / 22.6), turnText);
        else if (turn3)
            canvas.drawText("(Your Turn)", (float) (canvasWidth / 15.42), (float) ((freeSpace*2) / y3div + canvasHeight / 22.6), turnText);
        else if(turn4)
            canvas.drawText("(Your Turn)", (float) (canvasWidth / 2 + canvasWidth / 15.42), (float) ((freeSpace*2) / y4div + canvasHeight / 22.6), turnText);



        if(gameOver) {
            if(score1 > score2 && score1 > score3 && score1 > score4)
                winner = 1;
            else if(score2 > score1 && score2 > score3 && score2 > score4)
                winner = 2;
            else if(score3 > score1 && score3 > score2 && score3 > score4)
                winner = 3;
            else if(score4 > score1 && score4 > score2 && score4 > score3)
                winner = 4;

            if(winner != 0)
                canvas.drawText("Player " + winner + " Wins!", (float)(canvasWidth/2 - canvasWidth/4.32), (float)(freeSpace - canvasHeight/y5div), scoreText);
            else
                canvas.drawText("Draw", (float)(canvasWidth/2 - canvasWidth/10.8), (float)(freeSpace - canvasHeight/y5div), scoreText);
        }
    }

}
