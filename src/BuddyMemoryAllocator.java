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

        if(process.getMemoryStart() != -1 ) return true;
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

        int memoryToDeallocateStart = process.getMemoryStart();
        int memoryToDeallocateEnd = process.getMemoryEnd();

        int memoryChunkToDeallocateIndex = getMemoryChunkIndexByStart(memoryToDeallocateStart, memoryChunks);

        memoryChunks.get(memoryChunkToDeallocateIndex).setAllocated(false);

        RestructureMemory(memoryChunks);
//
//
//
//        //If start is even power of 2
//        //Then we may merge it with the following chunk
//        if(memoryToDeallocateStart==0 || (Math.log(memoryToDeallocateStart)/Math.log(2))%2 == 0)
//        {
//            /*
//                The condition to merge with the next one is that the next one
//                is not allocated and of same size
//             */
//            if(memoryChunks.get(memoryChunkToDeallocateIndex +1).getSize() == memoryChunks.get(memoryChunkToDeallocateIndex ).getSize()
//                    && !memoryChunks.get(memoryChunkToDeallocateIndex +1).isAllocated())
//            {
//                //Set the end of the first to the end of the second
//                memoryChunks.get(memoryChunkToDeallocateIndex).setEnd(memoryChunks.get(memoryChunkToDeallocateIndex +1).getEnd());
//
//                memoryChunks.remove(memoryChunkToDeallocateIndex+1);
//
//                RestructureMemory(memoryChunks);
//            }
//        }
//        //If start is odd power of 2, then we may merge with the preceding one
//        else
//        {
//            /*
//                The condition to merge with the preceding one is that the preceding one
//                is not allocated and of same size
//             */
//            if(memoryChunks.get(memoryChunkToDeallocateIndex -1).getSize() == memoryChunks.get(memoryChunkToDeallocateIndex ).getSize()
//                    && !memoryChunks.get(memoryChunkToDeallocateIndex -1).isAllocated())
//            {
//                //Set the start of the second to the start of the first
//                memoryChunks.get(memoryChunkToDeallocateIndex).setStart(memoryChunks.get(memoryChunkToDeallocateIndex -1).getStart());
//
//                memoryChunks.remove(memoryChunkToDeallocateIndex-1);
//
//                RestructureMemory(memoryChunks);
//            }
//        }


    }

    public static void RestructureMemory(ArrayList<MemoryChunk> memoryChunks) {
        if(memoryChunks.size() == 1) {
            return;
        }
        if(memoryChunks.size() == 2) {
            if(!(memoryChunks.get(0).isAllocated() || memoryChunks.get(1).isAllocated())) {
                memoryChunks.remove(0);
                memoryChunks.set(0, new MemoryChunk(false, 0, 1023));
            }
        }
        else {

            for (int i = 0; i < memoryChunks.size() - 1; i++) {
                MemoryChunk current = memoryChunks.get(i);
                MemoryChunk next = memoryChunks.get(i + 1);
                if (!current.isAllocated() && !next.isAllocated() && current.getSize() == next.getSize() && (current.getStart() / current.getSize() % 2 == 0)) {

                    current.setEnd(next.getEnd());
                    memoryChunks.remove(i + 1);
                    i--;

                }
            }
        }
    }

    public static int getMemoryChunkIndexByStart(int memoryStart, ArrayList<MemoryChunk> memoryChunks)
    {
        for(int i=0; i<memoryChunks.size(); i++)
        {
            if(memoryChunks.get(i).getStart()==memoryStart)
            {
                return i;
            }
        }
        return -1;
    }

}
