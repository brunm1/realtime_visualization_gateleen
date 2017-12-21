package ch.bfh.ti.gapa.cli.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Defines a one line format for log output.
 * The timestamp is formatted as ISO-8601.
 * The default format from {@link java.util.logging.SimpleFormatter} uses
 * two lines per log record which is difficult to read.
 */
public class SlimFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        //ISO-8601
        Instant instant = Instant.ofEpochMilli(record.getMillis());
        String time = instant.toString();
        pw.print(time);
        pw.print(' ');

        //level
        pw.print(record.getLevel());
        pw.print(' ');

        //location
        pw.print(record.getSourceClassName());
        pw.print(' ');
        pw.print(record.getSourceMethodName());
        pw.print(": ");

        //message
        pw.print(record.getMessage());

        //stacktraces
        Throwable t = record.getThrown();
        if (t != null) {
            pw.println();
            t.printStackTrace(pw);
            pw.close();
        }

        pw.println();

        return sw.toString();
    }
}
