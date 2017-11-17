package ch.bfh.ti.gapa.process.interfaces;

import java.io.IOException;

@FunctionalInterface
public interface ProcessLayer {
    String process(Input input) throws IOException;
}
