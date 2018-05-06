import java.util.ArrayList;

public class BuddyMemoryAllocator {

    public static int roundUpMemory(int memoryRequested) {
        return (int)Math.pow(2,(int)Math.ceil(Math.log(memoryRequested) / Math.log(2)));
    }

    public static MemoryChunk getSmallestChunk(ArrayList<MemoryChunk> memoryChunks, int memoryToAllocate) {
        if(memoryChunks.size() == 1) {
            if(memoryToAllocate <= memoryChunks.get(0).getSize()) {
                return memoryChunks.get(0);
            }
            else {
                return null;
            }
        }

        MemoryChunk smallestChunk = null;
        for(int i = 0; i < memoryChunks.size(); i++) {
            //check if it's the first chunk to be checked for size and allocation
            if(smallestChunk == null && memoryChunks.get(i).getSize() >= memoryToAllocate) {
                if(!memoryChunks.get(i).isAllocated()) {
                    smallestChunk = memoryChunks.get(i);
                }
            }
            //traverse the array to get the smallest not allocated chunk available
            if(smallestChunk != null && memoryChunks.get(i).getSize() >= memoryToAllocate && memoryChunks.get(i).getSize() < smallestChunk.getSize()) {
                if(!memoryChunks.get(i).isAllocated()) {
                    smallestChunk = memoryChunks.get(i);
                }
            }
        }
        return smallestChunk;
    }

    public static boolean AllocateMemory(ArrayList<MemoryChunk> memoryChunks, Process process) {
        int memoryToAllocate = process.getMemorySize();
        MemoryChunk smallestChunk = getSmallestChunk(memoryChunks, memoryToAllocate);
        if(smallestChunk == null) {
            return false;
        }

        if(smallestChunk.getSize() > memoryToAllocate) {
            int smallestStartPos = smallestChunk.getStart();
            int numSplits = (int)(Math.log(smallestChunk.getSize() / memoryToAllocate)/Math.log(2));
            for(int i = 0; i < numSplits; i++) {
                splitChunks(memoryChunks, smallestStartPos);
            }

            for(int i = 0; i < memoryChunks.size(); i++) {
                if(memoryChunks.get(i).getStart() == smallestStartPos) {
                    smallestChunk = memoryChunks.get(i);
                }
            }
        }

        smallestChunk.setAllocated(true);
        process.setMemoryStart(smallestChunk.getStart());
        process.setMemoryEnd(smallestChunk.getEnd());
        return true;
    }

    //split the memory chunk
    public static void splitChunks(ArrayList<MemoryChunk> memoryChunks, int memStart) {
        MemoryChunk memoryChunk = null;
        int i;
        for(i = 0; i < memoryChunks.size(); i++) {
            if(memoryChunks.get(i).getStart() == memStart) {
                memoryChunk = memoryChunks.get(i);
                break;
            }
        }

        int memEnd = memoryChunk.getEnd();
        int midPoint = (memEnd + memStart + 1) / 2;
        memoryChunk.setEnd(midPoint - 1);
        memoryChunks.add(i + 1, new MemoryChunk(false,midPoint,memEnd ));
    }

    public static void Deallocate(ArrayList<MemoryChunk> memoryChunks, Process process) {

    }

}
