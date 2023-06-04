package com.wit.gobang;

import android.graphics.Point;

import java.util.List;

public class WinnerChecker {
    private Point point1, point2;
    private final List<Point> points;
    private int x;
    private int y;

    public WinnerChecker(List<Point> points) {
        this.points = points;
    }

    public boolean checkWinningCondition() {
        for (Point point : points) {
            x = point.x;
            y = point.y;

            if (check(Constants.LEFT_DIAGONAL) || check(Constants.RIGHT_DIAGONAL) || check(Constants.VERTICAL) || check(Constants.HORIZONTAL)) {
                return true;
            }
        }
        return false;
    }

    private boolean check(int direction) {
        int count = 1;
        for (int i = 1; i < 3; i++) {
            switch (direction) {
                case Constants.HORIZONTAL:
                    point1 = new Point(x - i, y);
                    point2 = new Point(x + i, y);
                    break;
                case Constants.VERTICAL:
                    point1 = new Point(x, y - i);
                    point2 = new Point(x, y + i);
                    break;
                case Constants.LEFT_DIAGONAL:
                    point1 = new Point(x - i, y + i);
                    point2 = new Point(x + i, y - i);
                    break;
                case Constants.RIGHT_DIAGONAL:
                    point1 = new Point(x - i, y - i);
                    point2 = new Point(x + i, y + i);
                    break;
            }
            if (!(points.contains(point1) || points.contains(point2))) {
                break;
            }
            if (points.contains(point1)) {
                count++;
            }
            if (points.contains(point2)) {
                count++;
            }
        }
        return count == Constants.MAX_COUNT_IN_LINE;
    }
}
