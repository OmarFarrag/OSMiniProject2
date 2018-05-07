import java.util.ArrayList;

public class Schedule {
    ArrayList<Double> m_NodesTime;
    ArrayList<String> m_NodesProcessesNumbers;
    ArrayList<String> m_processNumber;
    ArrayList<Double> m_waitingTime;
    ArrayList<Double> m_turnaroundTime;
    ArrayList<Double> m_weightedTurnaroundTime;

    public Schedule(ArrayList<Double> NodesTime, ArrayList<String> NodesProcessesNumbers,
                    ArrayList<Double> waitingTime,ArrayList<Double> turnaroundTime, ArrayList<Double> weightedTurnaroundTime , ArrayList<String> processNumber)
    {
        m_NodesTime= NodesTime;
        m_NodesProcessesNumbers = NodesProcessesNumbers;
        m_waitingTime = waitingTime;
        m_turnaroundTime = turnaroundTime;
        m_weightedTurnaroundTime = weightedTurnaroundTime;
        m_processNumber = processNumber;
    }
}
