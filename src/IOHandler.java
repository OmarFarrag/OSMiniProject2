import java.io.*;
import java.util.ArrayList;
import java.util.List;

enum RRParameters{
    Quantum,
    Switch
}

public class IOHandler {


    //This function returns the processes read from the input file
    public ArrayList<Process> readProcesses() {
        String fileName = "input.txt";

        //This will reference one line from input at a time
        String line = null;

        ArrayList<Process> processes = null;

        try {

            //FileReader reads text files in default encoding
            FileReader fileReader = new FileReader(fileName);

            //wrap the file reader in BufferedReader
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //Discard first three lines
            line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            line = bufferedReader.readLine();

            processes = new ArrayList<Process>();

            String processID;
            int runTime, arrivalTime, memSize;

            while ((line = bufferedReader.readLine()) != null) {
                //Split the line by tap spaces
                String[] processProps = line.split("\t");

                processes.add(new Process(processProps[0], Double.parseDouble(processProps[1]), Double.parseDouble(processProps[2]), Integer.parseInt(processProps[3])));
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return processes;
    }

    //Function that returns a specific parameter for the RR algorithm
    public double readRRParameter(RRParameters parameter) {
        String fileName = "input.txt";

        //FileReader reads text files in default encoding
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //wrap the file reader in BufferedReader
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        if(parameter==RRParameters.Quantum) {
            try {
                //return the second string in the first line, converted to double
                return Double.parseDouble(bufferedReader.readLine().split("\t")[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (parameter == RRParameters.Switch)
        {

            try {

                //Skip one line
                bufferedReader.readLine();

                return Double.parseDouble(bufferedReader.readLine().split("\t")[1]);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    public void writeResults(ArrayList<Process> processes)
    {
        String  fileName = "output.txt";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);


            BufferedWriter printWriter = new BufferedWriter(fileWriter);
            String formatStr = "%-15s %-15s %-15s %-15s %-15s %-15s %-15s%n";

            printWriter.write(String.format(formatStr,"process_id","run_time","arrival_time","finish_time","mem_size","mem_start","mem_end"));
            printWriter.newLine();

            Process current=null;
            for(int i =0; i<processes.size(); i++) {
                current = processes.get(i);
                printWriter.append(String.format(formatStr,current.getID(),String.valueOf(current.getRunTime()),String.valueOf(current.getArrivalTime()),String.valueOf(current.getFinishTime()),String.valueOf(current.getMemorySize()),String.valueOf(current.getMemoryStart()),String.valueOf(current.getMemoryEnd())));
                printWriter.newLine();
            }


            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public static void main(String[] args)
    {
        IOHandler fileHandler = new IOHandler();
        ArrayList<Process> processes = fileHandler.readProcesses();
        double quantum = fileHandler.readRRParameter(RRParameters.Quantum);
        double switchTime = fileHandler.readRRParameter(RRParameters.Switch);

        fileHandler.writeResults(processes);


        int x;

    }
}
