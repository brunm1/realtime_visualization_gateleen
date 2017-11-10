# Gapa

Visualization of communication between services powered by Gateleen.
It parses logs and outputs plantuml.

## Getting Started

1. Checkout
2. mvn install
3. cd gapa-cli/target
4. Run application with command below

```bash
    java -jar gapa-cli-1.0.jar "^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+) - %(\\S+)\\s+(?<method>GET|PUT|POST|DELETE)\\s+(?<url>\\S+)\\s+s=(?<sender>\\w+)" "yyyy-MM-dd HH:mm:ss,SSS" < ../../gapa-test/src/test/resources/sample_log 
```

5. The output will be a plantuml diagram (in text format). 
There is a main method in the gapa-playground module that can generate a png from plantuml.

### CLI Parameters

First parameter is the log pattern. It is a regex with labelled groups: date, url, sender, method.
Second parameter is date pattern. The format is specified by DateTimeFormatter from Java 8 Standard Library.

Standard Input is used as log input.

## Known issues

It is planned to extract the receiver of a request by analyzing hooks (routes and listeners). Until then, the whole url is used as receiver for the plantuml.

## Modules

| Module | Description |
|---|---|
| cli | A command line tool to analyse Log files and output sequence diagrams. |
| config | Currently not used |
| database | Currently not used |
| domain | The classes in this module represent the domain model. Input data will be converted into this model. If we add persistence, the data will be persisted in this domain model. |
| playground | Contains tools for developers to play with specific features. Currently, there is a tool to render PlantUml. |
| process | Logic for parsing and filtering of communication data and generating the output as sequence diagrams. |
| test | End to end test for gapa. |
| web | Currently not used |

