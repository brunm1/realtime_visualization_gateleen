package ch.bfh.ti.gapa.cli.reading.file;

import ch.bfh.ti.gapa.cli.parsing.ParseException;
import ch.bfh.ti.gapa.cli.raw.RawInput;

@FunctionalInterface
public interface DefaultConfigFileReader {
    void read(RawInput input);
}
