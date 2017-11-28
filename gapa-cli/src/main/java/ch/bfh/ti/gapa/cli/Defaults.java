package ch.bfh.ti.gapa.cli;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

class Defaults {
    static final String defaultInboundRequestPatternStr = "^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})" +
            "\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+) - %(\\S+)\\s+" +
            "(?<method>GET|PUT|POST|DELETE)" +
            "\\s+" +
            "(?<url>\\S+)" +
            "\\s+s=" +
            "(?<sender>\\w+)";
    static final Pattern defaultInboundRequestPattern = Pattern.compile(defaultInboundRequestPatternStr);

    static final String defaultOutboundRequestPatternStr = "^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) " +
            "(?<method>GET|PUT|POST|DELETE) " +
            "(?<url>\\S+) " +
            "(?<receiver>\\w+)";
    static final Pattern defaultOutboundRequestPattern = Pattern.compile(defaultOutboundRequestPatternStr);

    static final Locale usedLocale = Locale.GERMANY;

    static final String defaultDateTimeFormatterStr = "yyyy-MM-dd HH:mm:ss,SSS";
    static final DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern(defaultDateTimeFormatterStr, usedLocale);
}
