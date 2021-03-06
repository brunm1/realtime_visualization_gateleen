# gapa-cli
Provides a command line interface for the user. The application is packaged here.

## Command line usage:
Use the -h option to print the following help message:

    Visualizes communication between services.
    It connects to a communication gateway over websocket,
    receives communication data, filters it and outputs PlantUml.
    Filters can be configured with a json config file.
    
    usage: java -jar /path/to/gapa.jar <options>
     -c,--config <path>        Set the path to a user config file.
     -h,--help                 Shows this help
     -n,--server-name <name>   Name of the central server that receives and
                               sends requests.
     -s,--schema               Print json config schema.
     -v,--version              Print version number.
     -w,--websocket <uri>      Connect over websocket to this URI.
    
    Possible exit codes:
    0 - Normal termination.
    5 - Invalid command usage.
    6 - Could not recognize some arguments.
    7 - Exception raised in process logic.
    10 - Failed to load the default config.
    12 - Could not parse configuration values.
    13 - Could not read configuration from command line arguments.
    14 - Could not print version.
    15 - Could not print config schema.
