package com.wit.gobang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class ChessBoardView extends View {
    // 棋盘的宽度
    private int boardWidth;
    // 棋盘每格的长度
    private float squareSize;//或者cellSize
    private final Paint paint = new Paint();
    // 定义黑白棋子的Bitmap
    private Bitmap whiteStone, blackStone;

    // 判断当前落下的棋子是否是白色的
    private boolean isCurrentBlackStone = true;
    // 记录黑白棋子位置的列表
    private ArrayList<Point> whiteStoneArray = new ArrayList<>();
    private ArrayList<Point> blackStoneArray = new ArrayList<>();

    // 游戏是否结束
    private boolean isGameOver;

    public ChessBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        Log.i("dog", "djf");
    }

    public void retractAFalseMove() {
        int position = 0;
        if (isCurrentBlackStone) {
            position = whiteStoneArray.size() - 1;
            if (-1 == position) {
                return; // size为零，不做操作
            }
            whiteStoneArray.remove(position);
        } else {
            position = blackStoneArray.size() - 1;
            if (-1 == position) {
                return;
            }
            blackStoneArray.remove(position);
        }
        isCurrentBlackStone = !isCurrentBlackStone;
        invalidate();
    }

    private void init() {
        paint.setColor(0x88000000);//设置画笔颜色为 ARGB 格式的半透明黑色，即 alpha 值为 88（十六进制，对应十进制值为 136），后八位为黑色。ARGB 是 Android 中颜色值的一种表示方式，前两位是 alpha（透明度）值，然后依次是红、绿、蓝三种颜色的值。
        paint.setAntiAlias(true);//设置抗锯齿，使绘制出来的图形边缘更加平滑。
        paint.setDither(true);//设置抖动，使渐变过程中从一个颜色到另一个颜色时更加平滑过渡。
        paint.setStyle(Paint.Style.STROKE);//设置画笔绘制模式为描边模式，即只有线条的轮廓被绘制出来，不会填充颜色。其他可选的模式包括 FILL（填充整个图形）、FILL_AND_STROKE（既填充颜色又描边）、STROKE（只描边但不填充）。

        whiteStone = BitmapFactory.decodeResource(getResources(), R.mipmap.white_stone);
        blackStone = BitmapFactory.decodeResource(getResources(), R.mipmap.black_stone);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);//获取到MeasureSpec中存储的实际大小。
        int widthModel = MeasureSpec.getMode(widthMeasureSpec);//获取到MeasureSpec的测量模式

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);//MeasureSpec中的测量模式包括UNSPECIFIED、EXACTLY和AT_MOST，其中UNSPECIFIED表示未指定测量模式。
        //在UNSPECIFIED模式下，View大小可以根据其内容自由调整。比如说，ScrollView可以滑动，因此它的子View的高度是不确定的，这时候就需要使用UNSPECIFIED模式来进行测量。

        int width = Math.min(widthSize, heightSize);
        if (widthModel == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightModel == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制棋盘的网格
        drawBoard(canvas);
        // 绘制棋盘的黑白棋子
        drawPieces(canvas);
        // 检查游戏是否结束
        checkGameOver();
    }

    // 检查游戏是否结束
    private void checkGameOver() {
        WinnerChecker whiteWinner = new WinnerChecker(whiteStoneArray);
        boolean whiteWin = whiteWinner.checkWinningCondition();
        WinnerChecker blackWinner = new WinnerChecker(blackStoneArray);
        boolean blackWin = blackWinner.checkWinningCondition();

        if (whiteWin || blackWin) {
            isGameOver = true;
            String text = whiteWin ? "白棋胜利" : "黑棋胜利";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    // 根据黑白棋子的数组绘制棋子
    private void drawPieces(Canvas canvas) {
        for (final Point whitPoint : whiteStoneArray) {//使用了基于范围的for语法，从而避免了手动管理循环变量的问题。
            drawPiece(canvas, whitPoint, whiteStone);
        }

        for (final Point blackPoint : blackStoneArray) {
            drawPiece(canvas, blackPoint, blackStone);
        }
    }
    private void drawPiece(Canvas canvas, Point point, Bitmap bitmap) {
        float left = (point.x + (1 - Constants.PIECE_TO_SQUARE_HEIGHT_RATIO) / 2) * squareSize;
        float top = (point.y + (1 - Constants.PIECE_TO_SQUARE_HEIGHT_RATIO) / 2) * squareSize;
        canvas.drawBitmap(bitmap, left, top, null);
    }

    // 绘制棋盘的网线
    private void drawBoard(Canvas canvas) {
        int width = boardWidth;
        float everyLineHeight = squareSize;

        for (int i = 0; i < Constants.MAX_LINE; i++) {
            int startX = (int) (everyLineHeight / 2);
            int endX = (int) (width - everyLineHeight / 2);

            int y = (int) ((0.5 + i) * everyLineHeight); //0.5 是为了让线条处于格子的正中间，而不是格子的顶端或底部。
            canvas.drawLine(startX, y, endX, y, paint);
            canvas.drawLine(y, startX, y, endX, paint);
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        boardWidth = width;
        squareSize = boardWidth * 1.0f / Constants.MAX_LINE; //计算每个方格的大小。由于棋盘是正方形，因此可以通过棋盘的宽度来计算出每个方格的大小。

        int pieceWidth = (int) (squareSize * Constants.PIECE_TO_SQUARE_HEIGHT_RATIO);
        whiteStone = Bitmap.createScaledBitmap(whiteStone, pieceWidth, pieceWidth, false);//第四个参数 filter，它控制着图片缩放时是否使用双线性过滤算法。双线性过滤是一种常见的图像处理技术，可以在缩放时平滑地过渡像素值，避免出现锯齿和马赛克等不美观的现象。
        blackStone = Bitmap.createScaledBitmap(blackStone, pieceWidth, pieceWidth, false);//方法用于创建一个指定大小的新的 Bitmap 对象
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver) {
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            //判断当前触摸事件的行为类型是否为 ACTION_UP，如果是，说明用户已经松开手指，触摸事件结束，然后我们可以进行相应的操作。
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point point = getValidPoint(x, y);
            if (whiteStoneArray.contains(point) || blackStoneArray.contains(point)) {
                Toast.makeText(getContext(), "已有棋子", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (isCurrentBlackStone) {
                blackStoneArray.add(point);
            } else {
                whiteStoneArray.add(point);
            }
            invalidate();
            isCurrentBlackStone = !isCurrentBlackStone;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        int rowIndex = (int) (x / squareSize);
        int columIndex = (int) (y / squareSize);

        return new Point(rowIndex, columIndex);
    }

    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";

    @Override
    protected Parcelable onSaveInstanceState() {
        //在 Activity 销毁或暂停时被调用，用于保存 View 的状态。在该方法中，可以使用 Bundle 存储需要保存的状态信息，并返回一个 Parcelable 实例作为结果。
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, isGameOver);

        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, blackStoneArray);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, whiteStoneArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // Activity 重新创建时被调用，用于恢复 View 的状态。在该方法中，可以从传入的 Parcelable 实例中获取之前保存的状态信息，并进行恢复操作。
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            isGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            whiteStoneArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            blackStoneArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void playAgain() {
        whiteStoneArray.clear();
        blackStoneArray.clear();
        isGameOver = false;
        isCurrentBlackStone = true;
        invalidate();//是 Android 中 View 类的一个方法。当调用这个方法时，它会告诉系统当前的 View 已经无效了，需要刷新绘制，以便更新界面。在下一个绘制周期中自动调用 onDraw() 方法重新进行绘制
    }
}
