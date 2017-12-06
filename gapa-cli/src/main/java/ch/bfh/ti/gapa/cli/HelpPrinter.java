package ch.bfh.ti.gapa.cli;

import org.apache.commons.cli.HelpFormatter;

import java.util.Arrays;
import java.util.stream.Collectors;

import static ch.bfh.ti.gapa.cli.CliOptions.options;

class HelpPrinter {
    static void printHelp() {
        System.out.println("Visualizes communication between services.\n" +
                "It connects to a communication gateway over websocket, " +
                "receives communication data, filters it and outputs plantuml.");
        System.out.println();
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("java -jar /path/to/gapa.jar <options>", options);
        System.out.println();
        printErrorCodeDoc();
    }


    private static void printErrorCodeDoc() {
        System.out.println("Possible exit codes:");
        System.out.println("0 - normal termination");
        System.out.println(Arrays.stream(ExceptionType.values())
                .map(e->e.getCode()+" - "+e.getDesc()).collect(Collectors.joining("\n")));
    }
}
