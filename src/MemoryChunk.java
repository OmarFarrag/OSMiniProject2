public class MemoryChunk {
    private boolean allocated;
    private int start;
    private int end;

    public MemoryChunk( int start, int end) {
        this.start = start;
        this.end = end;
    }

    public MemoryChunk(boolean allocated, int start, int end) {
        this.allocated = allocated;
        this.start = start;
        this.end = end;
    }

    public MemoryChunk(int size, boolean allocated) {
        this.allocated = allocated;
    }

    public MemoryChunk(int size) {
    }

    public boolean isAllocated() {
        return allocated;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getSize() {
        return end - start + 1;
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
