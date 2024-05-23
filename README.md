# Data Analysis Service

This service provides endpoints to add data and an endpoint to analyse the data by providing basic stats.

## Pre-requisites

* Java 21
* Gradle
* Docker

## Testing

### Unit

`gradle test`

### Integration

1. Run the service locally (`./docker-start.sh`)
2. `gradle integrationtest`

## Running service locally

Use the script `./docker-start.sh`.

## Requirements

1. POST endpoint - `add` - that accepts a `symbol` string and `value` number
2. POST endpoint - `add_batch` - that accepts a `symbol` string and an array `values` of numbers
3. GET endpoint - `stats` - that accepts a `symbol` string and `k` int
4. Single-node implementation
5. Up to 100 of unique symbols
6. Any suitable language and framework
7. Must handle high volume
8. Efficient implementation of data handling

## Implementation

1. Used Spring Boot + Kotlin to create a REST service and Kotest for testing.
2. The service runs in Docker - used with e.g. Kubernetes, it should allow good scalability
3. To achieve high efficiency:
    * stats are kept as a running total
    * aggregated values (average and variance) are calculated based on previous variance, deprecated value and new value in series
    * full data series is only used in some cases to find the min and max
4. Stats use population and sample variance depending on whether the whole data set is analysed

## Improvements

1. Add observability to improve the detection of issues and ability to diagnose problems.
2. Introduce data persistence. Currently, all data is kept in memory.
3. Performance testing
