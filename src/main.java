import java.io.FileReader;
import java.util.ArrayList;

public class main {
    public static void main(String[] args)
    {
        IOHandler fileHandler = new IOHandler();
        ArrayList<Process> processes;
        processes = fileHandler.readProcesses();

        double contextSwitch = fileHandler.readRRParameter(RRParameters.Switch);
        double quantum = fileHandler.readRRParameter(RRParameters.Quantum);

        ArrayList<Process> scheduledProcesses =   RoundRobbin.schedule(processes,contextSwitch , quantum);

        fileHandler.writeResults(scheduledProcesses);
       /* Process process = new Process("T1",10,0,10);
        MemoryChunk memoryc = new MemoryChunk(false,0,1023);
        ArrayList<MemoryChunk> mem = new ArrayList<MemoryChunk>();
        mem.add(memoryc);
        BuddyMemoryAllocator.AllocateMemory(mem,process);
        BuddyMemoryAllocator.Deallocate(mem,process);*/

    }
}
