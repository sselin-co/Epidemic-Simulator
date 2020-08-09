/**
 * Refer to https://prajwalsouza.github.io/Experiments/Epidemic-Simulation.html for a good example of what we're trying to do
 */
package com.diseasemodeling;

import java.util.ArrayList;
import java.util.Random;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

public class SimpleSimulationSketch extends PApplet {
    private int TARGET_FRAMERATE = 30;

    // Sketch modifier variables from modifier menu
    private int populationNumber;
    private float personSize;
    private float infectionChance;
    private int infectivityRadiusModifier;
    private int socialDistanceChance;
    private int maskWearingChance;
    private int startingInfected;
    private int daysInfected;
    private int secondsPerDay;

    float agentRadius;
    float infectionRadius;

    private int susceptibleColor = 0xFF57E260;
    private int infectedColor = 0xFFEA2A24;
    private int recoveredColor = 0xFFDADC1A;
    private int backgroundColor = 0xFF1e1e1e;

    private ArrayList<Agent> personArrayList;
    private  ArrayList<Agent> dataList;
    ArrayList<Integer> randomIndexList;
    private QuadTree personQuadTree;
    public int widthByHeight;

    public int numberOfSusceptible;
    public int numberOfInfected;
    public int numberOfRecovered;
    public float rNaught;
    public ArrayList<Float> rNaughtList;
    public float avgrNaught;
    public float finalAvgrNaught;
    public int daysElapsed;



    SimpleSimulationSketch(int populationNumber, float personSize, float infectionChance, int radiusModifier
    , int socialDistanceChance, int maskWearingChance, int startingInfected, int daysInfected, int daysPerSecond){
        this.populationNumber = populationNumber;
        this.personSize = personSize;
        this.infectionChance = infectionChance;
        this.infectivityRadiusModifier = radiusModifier;
        this.socialDistanceChance = socialDistanceChance;
        this.maskWearingChance = maskWearingChance;
        this.startingInfected = startingInfected;
        this.daysInfected = daysInfected;
        this.secondsPerDay = daysPerSecond;
    }

    public void settings(){
        size(width, height);
    }

    public void setup() {
        widthByHeight = width+height;
        agentRadius = (widthByHeight /2f*0.01f)*personSize;
        infectionRadius = agentRadius + (widthByHeight /50f*0.01f)* infectivityRadiusModifier;
        frameRate(TARGET_FRAMERATE);
        ellipseMode(RADIUS);
        personQuadTree = new QuadTree(new Rectangle(width/2f, height/2f, width, height), 10);
        resetSketch();
    }

    public void resetSketch(){
        loop();
        try {
            daysElapsed = 0;
            frameCount = 0;
            avgrNaught = 0;
            personArrayList = new ArrayList<>();
            randomIndexList = new ArrayList<>();
            rNaughtList = new ArrayList<>();
            if (populationNumber != 0) {
                for (int i = 0; i < startingInfected; i++)
                    randomIndexList.add(new Random().nextInt(populationNumber));
                for (int i = 0; i < populationNumber; i++) {
                    if (randomIndexList.contains(i)) {
                        personArrayList.add(new Agent());
                        personArrayList.get(i).startTimer();
                        personArrayList.get(i).isInfected = true;
                        personArrayList.get(i).setInfection();
                        personArrayList.get(i).setSocialDistancing();
                        personArrayList.get(i).setMaskWearing();
                        continue;
                    }
                    personArrayList.add(new Agent());
                    personArrayList.get(i).setSocialDistancing();
                    personArrayList.get(i).setMaskWearing();
                }
            }
        } catch (Exception e){
            System.err.println("resetSketch() has crashed");
        }
    }

    public void resetSketch(int populationNumber, float personSize, float infectionChance, int radiusModifier
            , int socialDistanceChance, int maskWearingChance, int startingInfected, int daysInfected, int daysPerSecond){
        this.populationNumber = populationNumber;
        this.personSize = personSize;
        this.infectionChance = infectionChance;
        this.infectivityRadiusModifier = radiusModifier;
        this.socialDistanceChance = socialDistanceChance;
        this.maskWearingChance = maskWearingChance;
        this.startingInfected = startingInfected;
        this.daysInfected = daysInfected;
        this.secondsPerDay = daysPerSecond;
        loop();
        try {
            daysElapsed = 0;
            frameCount = 0;
            avgrNaught = 0;
            agentRadius = (widthByHeight /2f*0.01f)*personSize;
            infectionRadius = agentRadius + (widthByHeight /50f*0.01f)*radiusModifier;
            personArrayList = new ArrayList<>();
            randomIndexList = new ArrayList<>();
            rNaughtList = new ArrayList<>();
            if (populationNumber != 0) {
                for (int i = 0; i < startingInfected; i++)
                    randomIndexList.add(new Random().nextInt(populationNumber));
                for (int i = 0; i < populationNumber; i++) {
                    if (randomIndexList.contains(i)) {
                        personArrayList.add(new Agent());
                        personArrayList.get(i).startTimer();
                        personArrayList.get(i).isInfected = true;
                        personArrayList.get(i).setInfection();
                        personArrayList.get(i).setSocialDistancing();
                        personArrayList.get(i).setMaskWearing();
                        continue;
                    }
                    personArrayList.add(new Agent());
                    personArrayList.get(i).setSocialDistancing();
                    personArrayList.get(i).setMaskWearing();
                }
            }
        } catch (Exception e){
            System.err.println("resetSketch() has crashed");
        }
    }

    public void draw() {
        dataList = personArrayList;
        personQuadTree.clear();
        background(backgroundColor);
        try {
            for (int i = 0; i < personArrayList.size(); i++) {
                personQuadTree.insert(new Point(personArrayList.get(i).position.x, personArrayList.get(i).position.y, personArrayList.get(i)));
                if (frameCount % (TARGET_FRAMERATE * secondsPerDay) == 0) {
                    if (i == 0){
                        daysElapsed++;
                    }
                    personArrayList.get(i).infectionCollisionCheck();
                    personArrayList.get(i).setInfection();
                    personArrayList.get(i).setRecovery();
                }
                personArrayList.get(i).wallCollisionCheck();
                strokeWeight(2); // scale with radius
                stroke(0);
                personArrayList.get(i).display();
            }
        }
        catch (Exception e){
            System.err.println("simpleSketch draw() just crashed");
        }
        displayDebug();
        displayDays();
        if (numberOfInfected == 0){
            noLoop();
        }
    }

    public void pauseSketch(){
        noLoop();
        // perry the platypus?!
        for (Agent p: personArrayList){
            if (p.sirStatus == 'I')
                p.stopTimer();
        }
    }

    public void resumeSketch(){
        loop();
        for (Agent p: personArrayList){
            if (p.sirStatus == 'I')
                p.startTimer();
        }
    }

    public void displayDebug(){
        displayFrameRate();
        displayAgentData();
    }

    public void displayFrameRate(){
        fill(255);
        textSize(32);
        text("FPS: " + (int)frameRate, 0, 30);
    }

    public void displayAgentData(){
        numberOfSusceptible = 0;
        numberOfInfected = 0;
        numberOfRecovered = 0;
        rNaught = 0;
        try {
            for (Agent p : dataList) {
                if (p.sirStatus == 'S')
                    numberOfSusceptible++;
                else if (p.sirStatus == 'I') {
                    numberOfInfected++;
                    rNaught = rNaught + p.numberOfAgentsInfected;
                }
                else if (p.sirStatus == 'R')
                    numberOfRecovered++;
            }
        }
        catch (Exception e){
            System.err.println("displayAgentData() has crashed");
        }
        if (numberOfInfected <= 0){
            rNaught = 0;
            avgrNaught = finalAvgrNaught;
        } else {
            rNaught = Math.round((rNaught/numberOfInfected) * 100) / 100f;
            rNaughtList.add(rNaught);
            rNaughtList.set(0, rNaughtList.get(0) + rNaught);
            avgrNaught = Math.round((getListAverage(rNaughtList)) * 100) / 100f;
            finalAvgrNaught = avgrNaught;
        }
        setSimulationData();
    }

    public void displayDays(){
        fill(255);
        textSize(60);
        text("Days: " + daysElapsed, 0, height-10);
    }

    public void setSimulationData(){
        SimulationData.getInstance().setSusceptibleData(numberOfSusceptible);
        SimulationData.getInstance().setInfectedData(numberOfInfected);
        SimulationData.getInstance().setRecoveredData(numberOfRecovered);
        SimulationData.getInstance().setrNaught(rNaught);
        SimulationData.getInstance().setAvgrNaught(avgrNaught);
    }

    public float getListAverage(ArrayList<Float> list){
        float listAverage = 0;
        for (float f: list){
            listAverage = listAverage + f;
        }
        return listAverage/list.size();
    }

    public class Agent {
        char sirStatus; //S = susceptible, I = infected, R = recovered
        int color;
        PVector position;
        PVector velocity;
        double phi = (2 * Math.PI * Math.random());
        float xspeed = width/600f;
        float yspeed = xspeed;
        Timer infectionTimer;
        float agentInfectionChance;
        float agentInfectionRadius;
        PShape personShape;
        PShape maskShape;
        ExpandingRing er;
        float minRingRadius = agentRadius;
        float maxRingRadius;
        boolean isInfected;
        boolean isWearingMask;
        boolean hasBeenInfected;
        int numberOfAgentsInfected;

        // Default constructor
        Agent(){
            position = new PVector(randomFloatWidth(), randomFloatHeight());
            velocity = new PVector(xspeed * (float)Math.cos(phi), yspeed * (float)Math.sin(phi));
            sirStatus = 'S';
            color = susceptibleColor;
            infectionTimer = new Timer();
            agentInfectionChance = infectionChance;
            agentInfectionRadius = infectionRadius;
        }

        private float randomFloatWidth(){  return new Random().nextFloat() * width; }
        private float randomFloatHeight(){ return new Random().nextFloat() * height; }

        void display(){
            position.x = position.x + (velocity.x);
            position.y = position.y + (velocity.y);
            sirStatusColorFill();
            personShape = createShape(ELLIPSE, position.x, position.y, agentRadius, agentRadius);
            shape(personShape);
            if (isWearingMask){
                fill(250);
                stroke(75);
                maskShape = createShape(RECT, position.x - agentRadius /2, position.y, agentRadius, agentRadius);
                shape(maskShape);
            }
            if (sirStatus == 'I'){
                er = new ExpandingRing(position.x, position.y);
                displayInfectionRadius();
//                if (getTimer().second() >= daysInfected){
//                    setSirStatus('R');
//                    setColorAndFill(recoveredColor);
//                }
            }
        }

        void sirStatusColorFill(){
            if (sirStatus == 'S') {
                fill(color);
            }
            else if (sirStatus == 'I') {
                fill(color);
            }
            else if (sirStatus == 'R') {
                fill(color);
            }
        }

        void displayInfectionRadius() {
            er.display();
        }

        void wallCollisionCheck(){
            if (position.x > width- agentRadius) {
                position.x = width- agentRadius;
                velocity.x *= -1;
            } else if (position.x < agentRadius) {
                position.x = agentRadius;
                velocity.x *= -1;
            } else if (position.y > height- agentRadius) {
                position.y = height- agentRadius;
                velocity.y *= -1;
            } else if (position.y < agentRadius) {
                position.y = agentRadius;
                velocity.y *= -1;
            }
        }

        void infectionCollisionCheck(){
            Circle range = new Circle(position.x, position.y, agentInfectionRadius);
            ArrayList<Point> found = new ArrayList<>();
            personQuadTree.query(range, found);
            for (Point p : found)
                spreadInfection(p.o);
        }

        void spreadInfection(Agent other){
            if (sirStatus == 'I' && other.sirStatus == 'S'){
                if (weightedBooleanGenerator(agentInfectionChance)) {
                    other.isInfected = true;
                    other.startTimer();
                    numberOfAgentsInfected++;
                }
            }
            else if (sirStatus == 'S' && other.sirStatus == 'I'){
                if (other.weightedBooleanGenerator(agentInfectionChance)) {
                    isInfected = true;
                    startTimer();
                    other.numberOfAgentsInfected++;
                }
            }
        }

        void setInfection(){
            if (isInfected && !hasBeenInfected){
                setSirStatus('I');
                setColorAndFill(infectedColor);
                //startTimer();
            }
        }

        void setRecovery(){
            if (infectionTimer.second() >= daysInfected* secondsPerDay){
                setSirStatus('R');
                setColorAndFill(recoveredColor);
                hasBeenInfected = true;
            }
        }

        void setSocialDistancing(){
            float socialDistancingPercentage = socialDistanceChance * 0.01f;
            if (weightedBooleanGenerator(socialDistancingPercentage)){
                velocity.x = 0;
                velocity.y = 0;
            }
        }

        void setMaskWearing(){
            float maskWearingPercentage = maskWearingChance * 0.01f;
            if (weightedBooleanGenerator(maskWearingPercentage)) {
                isWearingMask = true;
                agentInfectionRadius = agentRadius + (agentInfectionRadius*0.2f);
            }
        }

        boolean weightedBooleanGenerator(float weight){
            return Math.random() < weight;
        }

        void startTimer(){
            infectionTimer.start();
        }

        void stopTimer(){
            infectionTimer.stop();
        }

        Timer getTimer(){
            return infectionTimer;
        }

        void setColorByStatus(char status){
            if (status == 'S')
                color = susceptibleColor;
            else if (status == 'I')
                color = infectedColor;
            else if (status == 'R')
                color = recoveredColor;
        }

        void setColorAndFill(int color){
            this.color = color;
            fill(color);
        }

        int getColor(){
            return color;
        }

        void setSirStatus(char sirStatus){
            this.sirStatus = sirStatus;
        }

        public String toString(){
            return position.toString() + "__" + getColor();
        }

        class ExpandingRing{
            float x,y;
            int color;
            PShape ringShape;

            ExpandingRing(float x, float y){
                this.x = x;
                this.y = y;
                color = infectedColor;
                maxRingRadius = agentInfectionRadius;
            }

            void display(){
                noFill();
                stroke(infectedColor);
                strokeWeight(2f);
                ringShape = createShape(ELLIPSE, x, y, minRingRadius, minRingRadius);
                shape(ringShape);
                minRingRadius = minRingRadius+0.3f;
                if (minRingRadius >= maxRingRadius) {
                    minRingRadius = agentRadius +0.3f;
                }
            }
        }
    }

    class Timer {
        int startTime = 0, stopTime = 0;
        boolean running = false;
        
        void start() {
            startTime = millis();
            running = true;
        }
        void stop() {
            stopTime = millis();
            running = false;
        }
        int getElapsedTime() {
            int elapsed;
            if (running) {
                elapsed = (millis() - startTime);
            }
            else {
                elapsed = (stopTime - startTime);
            }
            return elapsed;
        }
        int second() {
            return (getElapsedTime() / 1000) % 60;
        }
        int minute() {
            return (getElapsedTime() / (1000*60)) % 60;
        }
        int hour() {
            return (getElapsedTime() / (1000*60*60)) % 24;
        }
    }
}
