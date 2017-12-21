package ch.bfh.ti.gapa.cli;

/**
 * Takes arguments and returns an exit code
 * like a command line application.
 */
public interface Cli {
    /**
     *
     * @param args Command line arguments
     * @return exit code
     */
    int run(String[] args);
}
