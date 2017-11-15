# gapa-cli

## Command line usage:
Use the -h option to print the following help message:

    Visualization of communication between services powered by Gateleen. 
    It parses logs and outputs plantuml. 
    If no -f option is given, stdin is used. 
    Logs must be in utf8.
    usage: java -jar /path/to/gapa-cli*.jar <options>
     -f,--file-name <file>                     Path to a file containing logs.
     -h,--help                                 Shows this help
     -i,--inbound-request-pattern <pattern>    The regex pattern for inbound
                                               requests. Must contain regex
                                               groups: date, method, url,
                                               sender.
                                               Default:
                                               "^(?<date>\d{4}-\d{2}-\d{2}
                                               \d{2}:\d{2}:\d{2},\d{3})\s+(\S+
                                               )\s+(\S+)\s+(\S+)\s+(\S+) -
                                               %(\S+)\s+(?<method>GET|PUT|POST
                                               |DELETE)\s+(?<url>\S+)\s+s=(?<s
                                               ender>\w+)"
     -o,--outbound-request-pattern <pattern>   The regex pattern for outbound
                                               requests. Must contain regex
                                               groups: date, method, url,
                                               receiver.
                                               Default:
                                               "^(?<date>\d{4}-\d{2}-\d{2}
                                               \d{2}:\d{2}:\d{2},\d{3})
                                               (?<method>GET|PUT|POST|DELETE)
                                               (?<url>\S+) (?<receiver>\w+)"
     -t,--time-format <pattern>                The time pattern. The format is
                                               specified by DateTimeFormatter
                                               from Java 8 Standard
                                               Library.Locale.GERMANY will be
                                               used.
                                               Default: "yyyy-MM-dd
                                               HH:mm:ss,SSS"