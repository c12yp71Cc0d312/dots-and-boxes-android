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
import android.widget.Toast;

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
    int turn = 1;
    Path path1 = new Path();
    Path path2 = new Path();
    Path path3 = new Path();
    Path path4 = new Path();
    Path square1 = new Path();
    Path square2 = new Path();
    Path square3 = new Path();
    Path square4 = new Path();
    Map<Integer, Boolean> horLinesChecker;
    Map<Integer, Boolean> verLinesChecker;
    int score1, score2, score3, score4;
    int boxesCompleted = 0;
    boolean gameOver = false;
    boolean pointScored = false;
    ArrayList<Path> path1History, path2History, path3History, path4History;
    ArrayList<Path> square1History, square2History, square3History, square4History;
    ArrayList<Boolean> lineDirHistory;
    ArrayList<Integer> horLineHistory, verLineHistory;
    ArrayList<Integer> turnHistory;
    Bitmap undo;
    ArrayList<Integer> boxHistory;

    public GameScreen(Context context, MainActivity mainActivity) {
        super(context);
        setBackgroundResource(R.drawable.paperbg);
        this.mainActivity = mainActivity;
        boxes = mainActivity.getMode();
        players = mainActivity.getPlayers();
        path1History = new ArrayList<>();
        path2History = new ArrayList<>();
        path3History = new ArrayList<>();
        path4History = new ArrayList<>();
        square1History = new ArrayList<>();
        square2History = new ArrayList<>();
        square3History = new ArrayList<>();
        square4History = new ArrayList<>();

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

        horLinesChecker = new LinkedHashMap<>();
        verLinesChecker = new LinkedHashMap<>();

        for(int t = 0; t <= boxes*(boxes+1) - 1; t++) {
            horLinesChecker.put(t, false);
            verLinesChecker.put(t, false);
        }

        score1 = 0;
        score2 = 0;
        score3 = 0;
        score4 = 0;

        lineDirHistory = new ArrayList<>();
        horLineHistory = new ArrayList<>();
        verLineHistory = new ArrayList<>();
        turnHistory = new ArrayList<>();
        boxHistory = new ArrayList<>();

        undo = BitmapFactory.decodeResource(getResources(),R.drawable.undo1);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :

                if(xPos >= canvasWidth - 150 && yPos <= 150) {
                    undo();
                    postInvalidate();
                    return true;
                }

                else {
                    checkTouchInsideGrid(xPos, yPos);       //postInvalidate in function
                    return true;
                }
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

        Log.d(TAG, "onDraw: dirhis" + lineDirHistory);
        Log.d(TAG, "onDraw: verhis" + verLineHistory);
        Log.d(TAG, "onDraw: horhis" + horLineHistory);

    }

    public void drawLine(float x, float y, boolean v) {
        float startX, startY, endX, endY;

        if(v) {                                 //vertical
            startX = x;
            startY = y;
            endX = x;
            endY = (float) (y + boxDistance);
        }
        else {                                  //horizontal
            startY = y;
            endY = y;
            startX = x;
            endX = (float) (x + boxDistance);
        }

        if(turn == 1) {
            path1 = new Path();
            path1.moveTo(startX, startY);
            path1.lineTo(endX, endY);
            path1History.add(path1);
            turnHistory.add(1);
        }
        else if(turn == 2) {
            path2 = new Path();
            path2.moveTo(startX, startY);
            path2.lineTo(endX, endY);
            path2History.add(path2);
            turnHistory.add(2);
        }
        else if(turn == 3) {
            path3 = new Path();
            path3.moveTo(startX, startY);
            path3.lineTo(endX, endY);
            path3History.add(path3);
            turnHistory.add(3);
        }
        else if(turn == 4) {
            path4 = new Path();
            path4.moveTo(startX, startY);
            path4.lineTo(endX, endY);
            path4History.add(path4);
            turnHistory.add(4);
        }
    }


    public void verticalLineMarker(int X, int Y) {
        int lineNumber = boxes*X + Y;
        verLinesChecker.put(lineNumber, true);
        verLineHistory.add(lineNumber);
        lineDirHistory.add(true);
        //Log.d(TAG, "verticalLineMarker: " + lineNumber);
        verLineBoxer(lineNumber, X, Y);
    }

    public void horizontalLineMarker(int X, int Y) {
        int lineNumber = X + boxes*Y;
        horLinesChecker.put(lineNumber, true);
        horLineHistory.add(lineNumber);
        lineDirHistory.add(false);
        //Log.d(TAG, "horizontalLineMarker: " + lineNumber);
        horLineBoxer(lineNumber, X, Y);
    }

    public void horLineBoxer(int lineNo, int x, int y) {
        pointScored = false;
        int numOfBoxesScored = 0;
        if(lineNo >= boxes) {
            int leftSide = boxes*x + y - 1;
            int rightSide = boxes*(x + 1) + y - 1;
            int top = x + boxes*(y - 1);
            if(verLinesChecker.get(leftSide) && verLinesChecker.get(rightSide) && horLinesChecker.get(top)) {
                drawSquare(x, y-1);
                pointScored = true;
                Log.d(TAG, "horLineBoxer: point scored true");
                numOfBoxesScored++;
            }
        }
        if(lineNo < boxes*boxes) {
            int leftSide = boxes*x + y;
            int rightSide = boxes*(x + 1) + y;
            int bottom = x + boxes*(y + 1);
            if(verLinesChecker.get(leftSide) && verLinesChecker.get(rightSide) && horLinesChecker.get(bottom)) {
                drawSquare(x, y);
                pointScored = true;
                Log.d(TAG, "horLineBoxer: point scored true");
                numOfBoxesScored++;
            }
        }
        boxHistory.add(numOfBoxesScored);
    }

    public void verLineBoxer(int lineNo, int x, int y) {
        pointScored = false;
        int numOfBoxesScored = 0;
        if(lineNo >= boxes) {
            int top = x + boxes*y - 1;
            int bottom = x + boxes*(y + 1) - 1;
            int leftSide = boxes*(x - 1) + y;
            if(horLinesChecker.get(top) && horLinesChecker.get(bottom) && verLinesChecker.get(leftSide)) {
                drawSquare(x-1, y);
                pointScored = true;
                Log.d(TAG, "verLineBoxer: point scored true");
                numOfBoxesScored++;
            }
        }
        if(lineNo < boxes*boxes) {
            int top = x + boxes*y;
            int bottom = x + boxes*(y + 1);
            int rightSide = boxes*(x + 1) + y;
            if(horLinesChecker.get(top) && horLinesChecker.get(bottom) && verLinesChecker.get(rightSide)) {
                drawSquare(x, y);
                pointScored = true;
                Log.d(TAG, "verLineBoxer: point scored true");
                numOfBoxesScored++;
            }
        }
        boxHistory.add(numOfBoxesScored);
    }

    public void drawSquare(int dotX, int dotY) {
        //Log.d(TAG, "drawSquare: reached");
        boxesCompleted ++;
        float startX = (float) (boxDistance*(dotX + 1));
        float startY = (float) (canvasHeight - boxDistance*(boxes - dotY + 1));
        if(turn == 1) {
            square1 = new Path();
            square1.moveTo(startX, startY);
            square1.lineTo((float)(startX + boxDistance), startY);
            square1.lineTo((float)(startX + boxDistance), (float)(startY + boxDistance));
            square1.lineTo(startX, (float)(startY + boxDistance));
            square1.lineTo(startX, startY);
            //Log.d(TAG, "drawSquare: drawn1");
            score1 ++;
            square1History.add(square1);
            //turn == 2 = true;
            //turn == 1 = false;
            //pathHistory.add(square1);
        }
        else if(turn == 2) {
            square2 = new Path();
            square2.moveTo(startX, startY);
            square2.lineTo((float)(startX + boxDistance), startY);
            square2.lineTo((float)(startX + boxDistance), (float)(startY + boxDistance));
            square2.lineTo(startX, (float)(startY + boxDistance));
            square2.lineTo(startX, startY);
            //Log.d(TAG, "drawSquare: drawn2");
            score2 ++;
            square2History.add(square2);
            //turn == 1 = true;
            //turn == 2 = false;
            //pathHistory.add(square2);
        }
        else if(turn == 3) {
            square3 = new Path();
            square3.moveTo(startX, startY);
            square3.lineTo((float)(startX + boxDistance), startY);
            square3.lineTo((float)(startX + boxDistance), (float)(startY + boxDistance));
            square3.lineTo(startX, (float)(startY + boxDistance));
            square3.lineTo(startX, startY);
            //Log.d(TAG, "drawSquare: drawn2");
            score3 ++;
            square3History.add(square3);
            //pathHistory.add(square3);
        }
        else if(turn == 4) {
            square4 = new Path();
            square4.moveTo(startX, startY);
            square4.lineTo((float)(startX + boxDistance), startY);
            square4.lineTo((float)(startX + boxDistance), (float)(startY + boxDistance));
            square4.lineTo(startX, (float)(startY + boxDistance));
            square4.lineTo(startX, startY);
            //Log.d(TAG, "drawSquare: drawn2");
            score4 ++;
            square4History.add(square4);
            //pathHistory.add(square4);
        }

        if(boxesCompleted == boxes*boxes)
            gameOver = true;
    }

    public boolean checkVerLineExists(int X, int Y) {
        int lineNo = boxes*X + Y;
        if(!verLinesChecker.get(lineNo)) {
            return false;
        }
        return true;
    }

    public boolean checkHorLineExists(int X, int Y) {
        int lineNo = X + boxes*Y;
        if(!horLinesChecker.get(lineNo)) {
            return false;
        }
        return true;
    }

    public void switchTurn() {

        if(players == 2) {
            if(turn == 1) {
                turn = 2;
            }
            else if(turn == 2) {
                turn = 1;
            }
        }
        else if(players == 3) {
            if(turn == 1) {
                turn = 2;
            }
            else if(turn == 2) {
                turn = 3;
            }
            else if(turn == 3) {
                turn = 1;
            }
        }
        else if (players ==  4) {
            if(turn == 1) {
                turn = 2;
            }
            else if(turn == 2) {
                turn = 3;
            }
            else if(turn == 3) {
                turn = 4;
            }
            else if(turn == 4) {
                turn = 1;
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
        for(Path s1 : square1History)
            canvas.drawPath(s1, redFill);
        for(Path s2 : square2History)
            canvas.drawPath(s2, blueFill);
        for(Path s3 : square3History)
            canvas.drawPath(s3, dkGrayFill);
        for(Path s4 : square4History)
            canvas.drawPath(s4, magentaFill);
        for(Path p1 : path1History)
            canvas.drawPath(p1, redStroke);
        for(Path p2 : path2History)
            canvas.drawPath(p2, blueStroke);
        for(Path p3 : path3History)
            canvas.drawPath(p3, dkGrayStroke);
        for(Path p4 : path4History)
            canvas.drawPath(p4, magentaStroke);
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

        if (turn == 1)
            canvas.drawText("(Your Turn)", (float) (canvasWidth / 15.42), (float) (freeSpace / y1div + canvasHeight / 22.6), turnText);
        else if (turn == 2)
            canvas.drawText("(Your Turn)", (float) (canvasWidth / 2 + canvasWidth / 15.42), (float) (freeSpace / y2div + canvasHeight / 22.6), turnText);
        else if (turn == 3)
            canvas.drawText("(Your Turn)", (float) (canvasWidth / 15.42), (float) ((freeSpace*2) / y3div + canvasHeight / 22.6), turnText);
        else if(turn == 4)
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

    public void undo() {
        Log.d(TAG, "undo: called");
        int moves = lineDirHistory.size();

        if(gameOver)
            gameOver = false;

        if(moves == 0) {
            Toast.makeText(getContext(), "No moves made", Toast.LENGTH_SHORT).show();
        }

        else {
            int lastTurn = turnHistory.get(turnHistory.size() - 1);
            int lastTurnScore = boxHistory.get(boxHistory.size() - 1);
            if(lastTurn == 1) {
                path1History.remove(path1History.size() - 1);
                for(int i = 1; i <= lastTurnScore; i++) {
                    square1History.remove(square1History.size() - 1);
                    score1--;
                    boxesCompleted--;
                }
            }
            else if(lastTurn == 2) {
                path2History.remove(path2History.size() - 1);
                for(int i = 1; i <= lastTurnScore; i++) {
                    square2History.remove(square2History.size() - 1);
                    score2--;
                    boxesCompleted--;
                }
            }
            else if(lastTurn == 3) {
                path3History.remove(path3History.size() - 1);
                for(int i = 1; i <= lastTurnScore; i++) {
                    square3History.remove(square3History.size() - 1);
                    score3--;
                    boxesCompleted--;
                }
            }
            else if(lastTurn == 4) {
                path4History.remove(path4History.size() - 1);
                for(int i = 1; i <= lastTurnScore; i++) {
                    square4History.remove(square4History.size() - 1);
                    score4--;
                    boxesCompleted--;
                }
            }
            for(int i = 1; i < players; i++) {
                switchTurn();
            }
            turn = turnHistory.get(turnHistory.size() - 1);
            turnHistory.remove(turnHistory.size()-1);
            boxHistory.remove(boxHistory.size() - 1);
        }


        if(moves > 0) {
            if (lineDirHistory.get(lineDirHistory.size() - 1)) {
                verLinesChecker.put(verLineHistory.get(verLineHistory.size() - 1), false);
                lineDirHistory.remove(lineDirHistory.size() - 1);
                verLineHistory.remove(verLineHistory.size() - 1);
                Log.d(TAG, "undo: ver");
            }
            else if (!lineDirHistory.get(lineDirHistory.size() - 1)) {
                horLinesChecker.put(horLineHistory.get(horLineHistory.size() - 1), false);
                lineDirHistory.remove(lineDirHistory.size() - 1);
                horLineHistory.remove(horLineHistory.size() - 1);
                Log.d(TAG, "undo: hor");
            }
        }
        
    }

    public void checkTouchInsideGrid(float xPos, float yPos){
        for (float x = (int) (boxDistance), dotX = 0; x <= boxDistance * (boxes + 1); x += boxDistance, dotX++) {
            for (float y = (int) (canvasHeight - boxDistance * (boxes + 1)), dotY = 0; y <= canvasHeight - boxDistance; y += boxDistance, dotY++) {

                if (x == boxDistance * (boxes + 1) && y != canvasHeight - boxDistance) {
                    if (xPos <= (x + 30) && xPos >= (x - 30) && yPos > (y + 20) && yPos < (y + boxDistance - 20)) {
                        if (!checkVerLineExists((int) dotX, (int) dotY)) {
                            drawLine(x, y, true);
                            verticalLineMarker((int) dotX, (int) dotY);
                            if (!pointScored)
                                switchTurn();
                            postInvalidate();
                        }
                    }
                } else if (y == canvasHeight - boxDistance && x != boxDistance * (boxes + 1)) {
                    if (yPos <= (y + 30) && yPos >= (y - 30) && xPos > (x + 20) && xPos < (x + boxDistance - 20)) {
                        if (!checkHorLineExists((int) dotX, (int) dotY)) {
                            drawLine(x, y, false);
                            horizontalLineMarker((int) dotX, (int) dotY);
                            if (!pointScored)
                                switchTurn();
                            postInvalidate();
                        }
                    }
                } else if (x != boxDistance * (boxes + 1) && y != canvasHeight - boxDistance) {
                    if (xPos <= (x + 30) && xPos >= (x - 30) && yPos > (y + 20) && yPos < (y + boxDistance - 20)) {
                        if (!checkVerLineExists((int) dotX, (int) dotY)) {
                            drawLine(x, y, true);
                            verticalLineMarker((int) dotX, (int) dotY);
                            if (!pointScored)
                                switchTurn();
                            postInvalidate();
                        }
                    } else if (yPos <= (y + 30) && yPos >= (y - 30) && xPos > (x + 20) && xPos < (x + boxDistance - 20)) {
                        if (!checkHorLineExists((int) dotX, (int) dotY)) {
                            drawLine(x, y, false);
                            horizontalLineMarker((int) dotX, (int) dotY);
                            if (!pointScored)
                                switchTurn();
                            postInvalidate();
                        }
                    }
                }

            }
        }
    }

}
