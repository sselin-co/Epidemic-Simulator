package com.diseasemodeling;

public class Circle {
    double x;
    double y;
    double radius;
    double radiusSquared;

    public Circle(float x, float y, float radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }

    public boolean intersects(Rectangle range){
        double xDistance = Math.abs(range.x - x);
        double yDistance = Math.abs(range.y - y);
        double rangeWidth = range.width;
        double rangeHeight = range.height;
        double edges = Math.pow(xDistance - rangeWidth, 2) + Math.pow(yDistance - rangeHeight, 2);

        if (xDistance > radius + rangeWidth || yDistance > radius + rangeHeight)
            return false;
        if (xDistance <= rangeWidth || yDistance <= rangeHeight)
            return true;
        return edges <= radiusSquared;
    }

    public boolean contains(Point p){
        double d = Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2);
        return d <= radiusSquared;
    }
}
