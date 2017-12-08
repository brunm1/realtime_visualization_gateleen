# Gapa

Visualization of communication between services powered by Gateleen.
It parses logs and outputs plantuml.

## Getting Started

1. Checkout
2. mvn install
3. cd gapa-cli/target
4. Run application with command below (assuming an compatible gateleen instance is running on port 7012)

```bash
    java -jar gapa.jar -w ws://localhost:7012
```

5. The output will be a plantuml diagram (in text format). 
There is a main method in the gapa-playground module that can generate a png from plantuml.

### CLI Parameters

See readme of submodule gapa-cli

## Known issues

For release 1.2 are no issues known.

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
| integration | Used to connect gapa with a gateleen instance |


