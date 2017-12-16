# gapa-process
${gapa-process.description}

## diagram
Generates a sequence diagram (in PlantUML syntax) from recorded data.

## filter
Controls which records are used to generate the diagram. Eg. status information, pings, etc. could be ignored to keep the diagram readable.

The filters are defined as JSONObjects. The attribute "type" is required and defines what kind of filter it is. The other attributes are specified by the schema of the type.

The schemas are located in [./src/main/resources](./src/main/resources). 
Examples of each test type (which are also used for the unit tests) are in [./src/test/resources](./src/test/resources). 

The attribute `type` is mandatory and has to be the name of one of the Filter-Types (as CamelCase, starting with a lowercase letter, unlike the ClassNames)
### GenericRegexFilter
Returns true if a given regex pattern matches the selected attribute of a record.

#### Parameters
* __attribute__: the name of the attribute that is checked
* __pattern__: a regex pattern

#### Schema
The Schema is located at [./src/main/resources/GenericRegexFilter.json](./src/main/resources/GenericRegexFilter.json)
#### Example
```json
{
  "type": "genericRegexFilter",
  "name": "myFirstRegexFilter",
  "attribute": "url",
  "pattern": "/gateleen/server/.*"
}
```
### TimeFilter
Returns true for all records that are before/after the specified time.
#### Parameters
* __time__: the time which is used as threshold
* before: if `true`, only messages from _before_ the given date pass, if `false` (default) only messages _after_ the given date pass.

#### Schema
The Schema is located at [./src/main/resources/TimeFilter.json](./src/main/resources/TimeFilter.json)

#### Example
```json
{
  "type": "timeFilter",
  "name": "myFirstTimeFilter",
  "time": "2017-12-01T20:00:00Z",
  "before": true
}

```

### AndFilter
The AndFilter consists of an array of filters. It returns true if all filters return true.

#### Schema
The Schema is located at [./src/main/resources/AndFilter.json](./src/main/resources/AndFilter.json)

#### Example
```json
{
  "type": "andFilter",
  "name": "AndFilterExample",
  "filters": [
    {
      "type": "timeFilter",
      "time": "2017-12-01T20:00:00Z"
    },
    {
      "type": "genericRegexFilter",
      "attribute": "url",
      "pattern": "/gateleen/server/.*"
    }
  ]
}
```
[./src/test/resources/AndFilterExample.json](./src/test/resources/AndFilterExample.json)

### OrFilter
The OrFilter consists of an array of filters. It returns true if at least one of the filters returns true.

#### Schema
The Schema is located at [./src/main/resources/OrFilter.json](./src/main/resources/OrFilter.json)

#### Example
```json
{
  "type": "orFilter",
  "name": "OrFilterExample",
  "filters": [
    {
      "type": "timeFilter",
      "time": "2017-12-01T20:00:00Z"
    },
    {
      "type": "genericRegexFilter",
      "attribute": "url",
      "pattern": "/gateleen/server/.*"
    }
  ]
}
```
[./src/test/resources/OrFilterExample.json](./src/test/resources/OrFilterExample.json)

## interfaces

## reader

## recording

## resources
