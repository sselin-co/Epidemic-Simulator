package com.diseasemodeling;

import processing.core.PApplet;
import processing.core.PFont;

import org.gwoptics.graphics.graph2D.Graph2D;
import org.gwoptics.graphics.graph2D.traces.ILine2DEquation;
import org.gwoptics.graphics.graph2D.traces.RollingLine2DTrace;


/**
 *  Equations that are to be plot must be encapsulated into a
 *  class implementing the IGraph2DCallback interface.
 *
 *  Desperately needs to operate off of sir data from SimulationSketch in real time
 **/
 class GraphSketch extends PApplet{
    Graph2D g;
    RollingLine2DTrace r1, r2, r3;
    int lineWidth = 10;
    int msRefreshRate = 100;
    float xIncr = 0.001f;
    int widthByHeight;
    float screenSizeMultiplier;

    private int susceptibleColor = 0xFF57E260;
    private int infectedColor = 0xFFEA2A24;
    private int recoveredColor = 0xFFDADC1A;
    private int backgroundColor = 0xFF1e1e1e;

    static class susceptibleEq implements ILine2DEquation {
        public double computePoint(double x, int pos) {
            return SimulationData.getInstance().getSusceptibleData();
        }
    }

    static class infectedEq implements ILine2DEquation{
        public double computePoint(double x,int pos) {
            return SimulationData.getInstance().getInfectedData();
        }
    }

    static class recoveredEq implements ILine2DEquation{
        public double computePoint(double x,int pos) {
            return SimulationData.getInstance().getRecoveredData();
        }
    }

    public void settings(){
        size(width,height);
    }

    public void setup(){
        widthByHeight = width+height;
        screenSizeMultiplier = widthByHeight * 0.1f;
        frameRate = 30;

        g = new Graph2D(this, width/2, height - 100, true);

        r1  = new RollingLine2DTrace(new susceptibleEq(),msRefreshRate,xIncr);
        r1.setLineWidth(lineWidth);
        r1.setTraceColour(87, 226, 96);

        r2 = new RollingLine2DTrace(new infectedEq(),msRefreshRate,xIncr);
        r2.setLineWidth(lineWidth);
        r2.setTraceColour(234, 42, 36);

        r3 = new RollingLine2DTrace(new recoveredEq(), msRefreshRate, xIncr);
        r3.setLineWidth(lineWidth);
        r3.setTraceColour(218, 220, 26);

        g.addTrace(r1);
        g.addTrace(r2);
        g.addTrace(r3);
        g.position.y = 50;
        g.setYAxisTickSpacing(200);
        g.setXAxisTickSpacing(2f);
        g.setYAxisTickFont("SansSerif,", 40, false);
        g.setXAxisTickFont("Arial,", 40, false);
        //g.setXAxisMax(2f);
        g.setYAxisLabel("Population");
        g.setXAxisLabel("");
        g.setYAxisLabelFont("SansSerif", 50, false);
        g.setXAxisLabelFont("SansSerif", 30, false);
        g.setFontColour(255, 255 ,255);
    }

    public int getTotalPopulation(){
        return SimulationData.getInstance().getRecoveredData() +
                SimulationData.getInstance().getInfectedData() +
                SimulationData.getInstance().getSusceptibleData();
    }

    public void resetSketch(){
        setup();
    }

    public void pauseSketch(){
        r1.pause();
        //noLoop();
    }

    public void resumeSketch(){
        r1.unpause();
        //loop();
    }

    public void draw(){
        if (SimulationData.getInstance().getInfectedData() <= 0){
            pauseSketch();
        }
        else {
            resumeSketch();
        }
        background(backgroundColor);
        g.setYAxisMax(getTotalPopulation());
        g.draw();
        displayData();
    }

    public void displayData(){
        int textSize = constrain(5/width, 40, 50);
        PFont font = createFont("SansSerif", textSize);
        textFont(font);
        fill(susceptibleColor);
        text("Susceptible: " + SimulationData.getInstance().getSusceptibleData(), width/2f + 50, height - 250);
        fill(infectedColor);
        text("Infected: " + SimulationData.getInstance().getInfectedData(), width/2f + 50, height - 200);
        fill(recoveredColor);
        text("Recovered: " + SimulationData.getInstance().getRecoveredData(), width/2f + 50, height - 150);
        fill(255);
        text("Average R0: " + SimulationData.getInstance().getAvgrNaught(), width/2f + 50, height-50);
        text("Current R0: " + SimulationData.getInstance().getrNaught(), width/2f + 50, height);
    }
}
