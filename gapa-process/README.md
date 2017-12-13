# gapa-process
Process logic of the project

## diagram
Generates a sequence diagram (in PlantUML syntax) from recorded data.

## filter
Controls which records are used to generate the diagram. Eg. status information, pings, etc. could be ignored to keep the diagram readable.

The filters are defined as JSONObjects. The attribute "type" is required and defines what kind of filter it is. The other attributes are specified by the schema of the type.

The schemas are located in src/main/resources. Examples of each test type (which are also used for the unit tests) are in src/test/resources. 

The attribute `type` is mandatory and has to be the name of one of the Filter-Types (as CamelCase, starting with a lowercase letter, unlike the ClassNames)
### GenericRegexFilter
Returns true if a given regex pattern matches the selected attribute of a record.

#### Parameters
* attribute: the name of the attribute that is checked
* pattern: a regex pattern

#### Example
``` 
{
  "type": "genericRegexFilter",
  "name": "myFirstRegexFilter",
  "attribute": "url",
  "pattern": "/gateleen/server/.*"
}
```
### TimeFilter

### AndFilter

### OrFilter

## interfaces

## reader

## recording

## resources
