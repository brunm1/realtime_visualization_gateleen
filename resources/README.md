# Gapa

${gapa.description}

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

For version ${project.version} are no issues known.

## Modules

| Module | Description |
|---|---|
| [gapa-cli](gapa-cli/README.md) | ${gapa-cli.description} |
| [gapa-domain](gapa-domain/README.md) | ${gapa-domain.description} |
| [gapa-integration](gapa-integration/README.md) | ${gapa-integration.description} |
| [gapa-playground](gapa-playground/README.md) | ${gapa-playground.description} |
| [gapa-process](gapa-process/README.md) | ${gapa-process.description} |
| [gapa-test](gapa-test/README.md) | ${gapa-test.description} |


