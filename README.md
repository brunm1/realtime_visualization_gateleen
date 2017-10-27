# visualization_gateleen
Visualization of communication between services powered by Gateleen.
It parses logs and outputs plantuml.

## How to run
1. Checkout
2. mvn install
3. cd gapa-cli/target
4. Run application with command below
5. The output will be a plantuml diagram (in text format). 
There is a main method in the gapa-playground module that can generate a png from plantuml.

```bash
    java -jar gapa-cli-1.0.jar "^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+) - %(\\S+)\\s+(?<method>GET|PUT|POST|DELETE)\\s+(?<url>\\S+)\\s+s=(?<sender>\\w+)" "yyyy-MM-dd HH:mm:ss,SSS" < ../../gapa-test/src/test/resources/sample_log 
```

### CLI Parameters

First parameter is the log pattern. It is a regex with labelled groups: date, url, sender, method.
Second parameter is date pattern. The format is specified by DateTimeFormatter from Java 8 Standard Library.

Standard Input is used as log input.

## Known issues
It is planned to extract the receiver of a request by a pattern that is applied to the request url or by analyzing hooks. Until then, the whole url is used as receiver for the plantuml.
