package com.wit.gobang;

public class Constants {

    // 五子连珠
    public final static int MAX_COUNT_IN_LINE = 5;
    // 棋盘的行数
    final static int MAX_LINE = 15;

    public final static float PIECE_TO_SQUARE_HEIGHT_RATIO = 3 * 1.0f / 4; //表示棋子大小相对于格子高度的比例系数。

    // 检查的方向
    final static int HORIZONTAL = 0;
    final static int VERTICAL = 1;
    final static int LEFT_DIAGONAL = 2;
    final static int RIGHT_DIAGONAL = 3;
}
