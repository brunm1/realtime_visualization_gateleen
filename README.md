# Gapa

Gapa is a command line application that connects to a websocket server. The server sends messages to gapa. The messages contain data about HTTP communication between nodes. The data may be filtered and is converted into a sequence diagram in PlantUml format.

## Getting Started

1. Checkout
2. mvn install
3. cd gapa-cli/target
4. Run application with command below. It is assumed that a compatible websocket server is running on port 7012. See module [gapa-integration](gapa-integration/README.md) for sample implementation).

```bash
    java -jar gapa.jar
```

5. The websocket server has to send messages to gapa. The module [gapa-integration](gapa-integration/README.md) defines the used message format.
6. Press enter to stop recording.
7. The output of StdOut will be a plantuml diagram (in text format). StdErr contains errors or debug information.
8. (Optional) There is a main method in [gapa-playground](gapa-playground/README.md) module that can generate a png from plantuml.

### CLI Parameters

See readme of submodule [gapa-cli](gapa-cli/README.md)

## Known issues

For version 1.4-SNAPSHOT are no issues known.

## Modules

| Module | Description |
|---|---|
| [gapa-cli](gapa-cli/README.md) | Provides a command line interface for the user. The application is packaged here. |
| [gapa-domain](gapa-domain/README.md) | Contains data models that can be used in the future to persist data. The data models are currently also used by the process logic. |
| [gapa-integration](gapa-integration/README.md) | Contains the data model used for the communication between gapa and a websocket server. Furthermore, it contains a websocket client for gapa and converters that can be used by the server to prepare messages for gapa. |
| [gapa-playground](gapa-playground/README.md) | Contains experiments or tools to play around. Only for development (won't be published with application). |
| [gapa-process](gapa-process/README.md) | Contains the process logic of the project. Converts gapa messages into plantuml. |
| [gapa-test](gapa-test/README.md) | Contains application tests. They verify if the packaged application fulfills the requirements of the requirements document. |


