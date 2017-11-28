package ch.bfh.ti.gapa.cli;

import org.apache.commons.cli.HelpFormatter;

import java.util.Arrays;
import java.util.stream.Collectors;

import static ch.bfh.ti.gapa.cli.CliOptions.options;

class HelpPrinter {
    static void printHelp() {
        System.out.println("Visualization of communication between services powered by Gateleen.\n" +
                "It parses logs and outputs plantuml.\n" +
                "If no -f option is given, stdin is used.\n" +
                "Logs must be in utf8.");
        System.out.println();
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("java -jar /path/to/gapa-cli*.jar <options>", options);
        System.out.println();
        printErrorCodeDoc();
    }


    private static void printErrorCodeDoc() {
        System.out.println("Possible exit codes:");
        System.out.println("0 - normal termination");
        System.out.println(Arrays.stream(Error.values())
                .map(e->e.getCode()+" - "+e.getDesc()).collect(Collectors.joining("\n")));
    }
}
