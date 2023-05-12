package upe.process;

public class UProcessModification implements AutoCloseable {
    private UProcess myUProcess;

    public UProcessModification(UProcess p ) {
        this.myUProcess = p;
        myUProcess.inputStarts();
    }

    @Override
    public void close() {
        myUProcess.inputStops();
    }
}
