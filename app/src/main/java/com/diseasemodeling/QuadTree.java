package com.diseasemodeling;

import java.util.ArrayList;

class QuadTree {
    Rectangle boundary;
    int capacity; // max no. of points
    ArrayList <Point> points ;
    boolean divide = false;
    QuadTree northeast;
    QuadTree northwest;
    QuadTree southeast;
    QuadTree southwest;

    public QuadTree (Rectangle rect , int cap){
        this.boundary = rect;
        this.capacity = cap;
        points = new ArrayList<>();
    }

    public boolean insert (Point p){
        if (!this.boundary.contains(p))
            return false;
        if (this.points.size() < this.capacity ){
            this. points.add(p);
            return true;
        }
        else {
            if (!this.divide) {
                subDivide();
            }
            if (this.northeast.insert(p)){
                return true;
            }
            else if (this.northwest.insert(p)){
                return true;
            }
            else if (this.southeast.insert(p)){
                return true;
            }
            else if(this.southwest.insert(p)){
                return true;
            }
            else return false;
        }
    }
    public void subDivide (){
        double x = this.boundary.x;
        double y = this.boundary.y;
        double w = this.boundary.width;
        double h = this.boundary.height;
        this.northeast = new QuadTree(new Rectangle (x+w/2 , y-h/2 , w/2 , h/2),capacity);
        this.northwest = new QuadTree(new Rectangle (x-w/2 , y-h/2 , w/2 , h/2),capacity);
        this.southeast = new QuadTree(new Rectangle (x+w/2 , y+h/2 , w/2 , h/2),capacity);
        this.southwest = new QuadTree(new Rectangle (x-w/2 , y+h/2 , w/2 , h/2),capacity);
        this.divide = true;
    }
    public void query (Rectangle range , ArrayList <Point> points) {
        if (range.intersects(this.boundary)) {
            for (int i = 0; i < this.points.size(); i++) {
                if (range.contains(this.points.get(i)))
                    points.add(this.points.get(i));
            }
            if (this.divide) {
                this.northeast.query(range, points);
                this.northwest.query(range, points);
                this.southeast.query(range, points);
                this.southwest.query(range, points);
            }
        }
    }

    public void query (Circle range , ArrayList <Point> found) {
        if (range.intersects(this.boundary)) {
            for (int i = 0; i < this.points.size(); i++) {
                if (range.contains(this.points.get(i))) {
                    found.add(this.points.get(i));
                }
            }
            if (this.divide) {
                this.northeast.query(range, found);
                this.northwest.query(range, found);
                this.southeast.query(range, found);
                this.southwest.query(range, found);
            }
        }
    }

    public void clear(){
        if (divide){
            northeast = null;
            northwest = null;
            southeast = null;
            southwest = null;
        }
        points.clear();
        divide = false;
    }
}