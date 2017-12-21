package ch.bfh.ti.gapa.cli.printer;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

public class CliPrintOptions extends Options {
    public CliPrintOptions() {
        OptionGroup printOptionGroup = new OptionGroup();

        Option help = Option.builder("h")
                .desc("Shows this help")
                .longOpt("help")
                .build();

        Option version = Option.builder("v")
                .desc("Print version number.")
                .longOpt("version")
                .build();

        Option jsonConfigSchema = Option.builder("s")
                .desc("Print json config schema.")
                .longOpt("schema")
                .build();

        printOptionGroup.addOption(help);
        printOptionGroup.addOption(version);
        printOptionGroup.addOption(jsonConfigSchema);

        this.addOptionGroup(printOptionGroup);
    }
}
