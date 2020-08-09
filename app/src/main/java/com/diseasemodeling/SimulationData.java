package com.diseasemodeling;

public class SimulationData  {
    public static int susceptibleData = 500;
    public static int infectedData;
    public static int recoveredData;
    public static float rNaught;
    public static float avgrNaught;

    private static SimulationData singleton = new SimulationData();

    private SimulationData(){
        if (singleton != null){
            throw new RuntimeException("Use getInstance() to get SimulationData");
        }
    }

    public int getSusceptibleData() {
        return susceptibleData;
    }

    public int getInfectedData() {
        return infectedData;
    }

    public int getRecoveredData() {
        return recoveredData;
    }

    public float getrNaught(){
        return rNaught;
    }

    public float getAvgrNaught(){
        return avgrNaught;
    }

    public void setSusceptibleData(int susceptibleData) {
        SimulationData.susceptibleData = susceptibleData;
    }

    public void setRecoveredData(int recoveredData) {
        SimulationData.recoveredData = recoveredData;
    }

    public void setInfectedData(int infectedData) {
        SimulationData.infectedData = infectedData;
    }

    public void setrNaught(float rNaught){
        SimulationData.rNaught = rNaught;
    }

    public void setAvgrNaught(float avgrNaught){
        SimulationData.avgrNaught = avgrNaught;
    }

    public synchronized static SimulationData getInstance(){
        if (singleton == null){
            singleton = new SimulationData();
        }
        return singleton;
    }

    public void resetSingleTon(){
        singleton = null;
    }

    @Override
    public String toString() {
        return "SimulationData{" + "\n"+
                "susceptibleData=" + susceptibleData + "\n"+
                ", infectedData=" + infectedData + "\n"+
                ", recoveredData=" + recoveredData + "\n"+
                '}';
    }
}
