package com.diseasemodeling;

public class Point {
        double x;
        double y ;
        SimpleSimulationSketch.Agent o;

        public Point (double x, double y){
            this.x = x;
            this.y = y;
        }

        public Point (double x , double y, SimpleSimulationSketch.Agent o){
            this.x = x;
            this.y = y;
            this.o = o;
        }

        public double getX(){
            return this.x;
        }
        public double getY(){
            return this.y;
        }

}
