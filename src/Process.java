public class Process {
    String ID;
    double arrivalTime;
    double runTime;
    double waitTime;
    double remainingTime;
    double turnAroundTime;
    boolean running ;
    int memoryStart;
    int memoryEnd;
    int memorySize;

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

    double finishTime;





    public Process(String f_ID, double f_runTime, double f_arrivalTime, int f_memSize)
    {
        ID = f_ID;
        arrivalTime = f_arrivalTime;
        runTime = f_runTime;
        remainingTime = f_runTime;
        memorySize = f_memSize;
        memoryStart=-1;
        memoryEnd=-1;
        running = false;
    }

    public Process(int memorySize) {
        this.memorySize = memorySize;
    }

    public double getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setRunTime(double runTime) {
        this.runTime = runTime;
    }

    public void setRemainingTime(double remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setMemoryStart(int memoryStart) {
        this.memoryStart = memoryStart;
    }

    public void setMemoryEnd(int memoryEnd) {
        this.memoryEnd = memoryEnd;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public boolean isRunning() {
        return running;
    }

    public int getMemoryStart() {
        return memoryStart;
    }

    public int getMemoryEnd() {
        return memoryEnd;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public String getID() {
        return ID;
    }

    public double getArrivalTime()
    {
        return arrivalTime;
    }

    public double getRunTime() {
        return runTime;
    }

    public void setRunning(boolean b){running = b;}
    public boolean getRunning(){return running;}

    public void setWaitTime(double t){waitTime=t;}
    public double getWaitTime(){return waitTime;}
    public void setTurnAroundTime(double t){turnAroundTime=t;}
    public void decreaseRemainingTime(double t){remainingTime-=t;}
    public double getRemainingTime(){ return remainingTime;}
}
