import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.Comparator.comparing;

public class RoundRobbin {
    static ArrayList<Process> schedule (ArrayList<Process> processesList , double contextSwitch, double quantum)
    {
        //Sort all processes by arrival time
        processesList.sort(comparing(Process::getArrivalTime));

        //Queue for active processes
        Queue<Process> activeProcesses = new LinkedList<Process>();

        ArrayList<Process> processNumberOrder = new ArrayList<>();

        //Arraylist of all time nodes in the schedule
        ArrayList<Double> NodesTime = new ArrayList<Double>();

        //Arraylist of number of process attached to each time node
        ArrayList<String> NodesProcessesNumbers = new ArrayList<String>();

        //Array list of Waiting time
        ArrayList<Double> waitingTime = new ArrayList<Double>();

        //Turnaround time List
        ArrayList<Double> turnaroundTime = new ArrayList<Double>();

        //Weighted turnaround time List
        ArrayList<Double> weightedTurnaroundTime = new ArrayList<Double>();

        //Process number for waiting etc list
        ArrayList<String> processNumber = new ArrayList<String>();

        ArrayList<MemoryChunk> memoryChunks = new ArrayList<MemoryChunk>();
        memoryChunks.add(new MemoryChunk(false,0,1023));
        ArrayList<Process> finishedProcesses = new ArrayList<Process>();

        double time=0;
        int done =0;
        Process temp = null;
        String prevProcessID = null;
        int numberOfProcesses = processesList.size();
        Process process = null;
        boolean firstTime = true;

        //an IOHandler to log the stats in the logfile
        IOHandler ioHandler = new IOHandler();

        //While there are still unfinished processes
        while(done<numberOfProcesses)
        {
            if(!processesList.isEmpty()) {
                for(int i = 0; i < processesList.size(); i++) {
                    if(time >= processesList.get(i).getArrivalTime() &&
                            !processesList.get(i).isRunning() && processesList.get(i).getNumWait() == 0) {
                        processNumberOrder.add(processesList.get(i));
                    }
                }
            }

            for(int i = 0; i < processNumberOrder.size(); i++) {
                if (processNumberOrder.get(i).getRemainingTime() <= 0) {
                    processNumberOrder.remove(i);
                }
            }

                ioHandler.writeToLogFile("Queue: ", false);

            /*
            need to reorder the queue
             */

                for (int i = 0; i < processNumberOrder.size(); i++) {
                    if (processNumberOrder.get(i).getID() == prevProcessID) {
                        processNumberOrder.remove(i);
                        if (temp != null) {
                            processNumberOrder.add(temp);
                        }
                    }
                }

                for (int i = 0; i < processNumberOrder.size(); i++) {
                    ioHandler.writeToLogFile(processNumberOrder.get(i).getID() + ", ", false);
                }

                ioHandler.writeToLogFile("", true);

            //Add the processes whose arrival time is less than or equal current time
            //and check the list is not empty
            while(!processesList.isEmpty() ) {
                if(processesList.get(0).getArrivalTime()<=time) {
                    activeProcesses.add(processesList.remove(0));
                }
                else {
                    break;
                }

            }

            //If there was a process running till the current time, add it to the back of the queue
            if(temp!=null){ activeProcesses.add(temp);}

            //If there is a process to be run
            if(activeProcesses.peek()!= null) {

                //Temp is the current running process
                temp = activeProcesses.remove();
                process = temp;

                double tempRemTime = temp.getRemainingTime();

                //Check if there is memory to be allocated
                if(BuddyMemoryAllocator.AllocateMemory(memoryChunks,temp)) {


                    //Check that the scheduler wasn't idle just before, and that another process was running
                    //If so, then there is context switch
                    if(prevProcessID!=null && !prevProcessID.equals(temp.getID())) {
                        time += 1;
                        double prevTime = time;
                        NodesTime.add(time);
                        NodesProcessesNumbers.add("0");
                        time += contextSwitch;
                        ioHandler.writeToLogFile("Process switching:    Started at " + prevTime + ", finished at " + time, true);
                        time += 1;
                    }

                    ////////////////////////////////////////////////////////////


                    //Check if it is the first time to run

                        if (!temp.getRunning()) {

                            temp.setRunning(true);
                            temp.setWaitTime(time - temp.getArrivalTime());
                        }
                    prevProcessID = temp.getID();
                    ioHandler.writeToLogFile("Executing Process " + prevProcessID + " :", false);

                    NodesTime.add(time);
                    NodesProcessesNumbers.add(temp.getID());
                    temp.decreaseRemainingTime(quantum);

                }
                else
                {
                    if(temp.isToBeHalted()) {
                        temp = null;
                    }
                    continue;
                }

                /////////////////////////////////////////////////////////////////

                ioHandler.writeToLogFile(" started at " + time, false);
                //Check if the process hasn't finished
                //If the process hasn't ended then it would take a full quantum
                //If no, then it would take only the remaining time
                if(temp.getRemainingTime()>0) {
                    time = time + quantum;
                    ioHandler.writeToLogFile(" finished at " + time, false);
                    ioHandler.writeToLogFile(", " + temp.getRemainingTime() + " remaining, ", false);

                }
                else {
                    time += tempRemTime;
                    ioHandler.writeToLogFile(" finished at " + time, false);
                }

                ioHandler.writeToLogFile(" memory starts at " + temp.getMemoryStart() + " and ends at " + temp.getMemoryEnd(), true);


                //Check if the process has finished
                if(temp.getRemainingTime()<=0) {

                    //Add the statistics with process number labels
                    processNumber.add(temp.getID());
                    waitingTime.add(temp.getWaitTime());
                    double t = time-temp.getArrivalTime();
                    turnaroundTime.add(t);
                    weightedTurnaroundTime.add(t/temp.getRunTime());

                    BuddyMemoryAllocator.Deallocate(memoryChunks,temp);

                    finishedProcesses.add(temp);

                    temp.setFinishTime(time);
                    done++;

                    //If this is the last process
                    //Add the closure
                    if(done==numberOfProcesses)
                    {
                        NodesTime.add(time);
                        NodesProcessesNumbers.add(temp.getID());
                        NodesTime.add(time);
                        NodesProcessesNumbers.add("0");
                    }

                    process = temp;

                    temp=null;
                }
            }
            //No process to be run now
            else {

                BuddyMemoryAllocator.RestructureMemory(memoryChunks);
                //Set scheduler to idle
                NodesTime.add(time);
                NodesProcessesNumbers.add("0");
                prevProcessID=null;
                if(processesList != null && processesList.size() > 0) {
                    //Time jump ... Hoopppaaaaaa
                    time = processesList.get(0).getArrivalTime();
                }

                if(processesList == null || processesList.size() <= 0) {
                    break;
                }
            }

        }
        return finishedProcesses;
    }

}
