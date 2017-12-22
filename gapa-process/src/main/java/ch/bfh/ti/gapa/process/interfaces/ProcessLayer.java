package ch.bfh.ti.gapa.process.interfaces;

import ch.bfh.ti.gapa.process.AsyncTask;

public interface ProcessLayer extends AsyncTask<String, ProcessLayerInput> {
    void stopRecording() throws Throwable;
}
