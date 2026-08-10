# RATPlus-API

Backend REST API for **RATPlus**, a Paris public-transport departure information application.

RATPlus-API is built with Spring Boot and uses the **Île-de-France Mobilités (IDFM) API** to retrieve public-transport lines, stations and real-time departures.

---

## Tech Stack

* Java 26
* Spring Boot 4.1.0
* Maven
* Spring Web MVC
* Spring Validation
* Spring Boot Actuator
* Docker
* Île-de-France Mobilités (IDFM) API

---

## Configuration

The application runs on port:

```text
8081
```

The IDFM API key is configured through application properties.

Example:

```properties
idfm.token=${IDFM_TOKEN}
```

The API key should not be committed to Git.

For local development, configure the required environment variable or local configuration before starting the application.

---

## Supported Transport Modes

RATPlus currently supports four types of public transport:

| Transport  | IDFM physical mode           |
| ---------- | ---------------------------- |
| Metro      | `physical_mode:Metro`        |
| RER        | `physical_mode:RapidTransit` |
| Tram       | `physical_mode:Tramway`      |
| Transilien | `physical_mode:LocalTrain`   |

Bus and other transport modes are intentionally excluded.

The backend retrieves all four supported transport modes in a single IDFM request.

---

# API

Base URL when running locally:

```text
http://localhost:8081
```

---

## Get Lines

### `GET /api/lines`

Returns all supported Metro, RER, Tram and Transilien lines.

### Example response

```json
[
  {
    "id": "line:IDFM:C01371",
    "name": "Métro 1"
  }
]
```

### LineDto

```text
id   : String
name : String
```

The `id` is the original IDFM line identifier.

Example:

```text
line:IDFM:C01371
```

---

# Get Stations

### `GET /api/stations/{lineId}`

Returns the stations associated with a line.

Example:

```text
GET /api/stations/line:IDFM:C01371
```

### Example response

```json
[
  {
    "name": "Argentine",
    "ids": [
      "stop_point:IDFM:22088",
      "stop_point:IDFM:463121"
    ],
    "label": "Argentine (Paris)"
  },
  {
    "name": "Bastille",
    "ids": [
      "stop_point:IDFM:22089",
      "stop_point:IDFM:463019"
    ],
    "label": "Bastille (Paris)"
  }
]
```

### StationDto

```text
name  : String
ids   : List<String>
label : String
```

---

## Multiple Stop Points

A station can contain multiple IDFM stop points.

For example:

```text
Argentine

stop_point:IDFM:22088
stop_point:IDFM:463121
```

These stop points represent different directions/platforms of the same station.

They are therefore grouped into one `StationDto`.

The backend keeps all corresponding stop-point IDs.

---

## Station Ordering

IDFM does not return stations according to their physical order on the line.

The API response is generally ordered alphabetically.

The backend currently preserves the order returned by IDFM.

---

# Get Next Departures

### `GET /api/departures`

Returns the next departures for a line and one or more stop points.

The request requires:

* `lineId`
* `stationIds`

Example:

```text
GET /api/departures?lineId=line:IDFM:C01371&stationIds=stop_point:IDFM:22099&stationIds=stop_point:IDFM:463044
```

The backend sends one request to IDFM for each stop point, then combines the results.

---

## DepartureDto

```text
direction      : String
destination    : String
arrivalTime    : LocalDateTime
departureTime  : LocalDateTime
status         : String
```

### Example response

```json
[
  {
    "direction": "La Défense",
    "destination": "La Défense (Grande Arche)",
    "arrivalTime": "2026-08-10T22:41:06.385",
    "departureTime": "2026-08-10T22:41:06.385",
    "status": "onTime"
  },
  {
    "direction": "Château de Vincennes",
    "destination": "Château de Vincennes",
    "arrivalTime": "2026-08-10T22:43:54.285",
    "departureTime": "2026-08-10T22:43:54.285",
    "status": "onTime"
  }
]
```

The departures returned by the backend are combined and sorted by time.

The backend returns a flat list rather than grouping departures by direction.

---

# Time Zone

IDFM returns timestamps in UTC.

Example:

```text
2026-08-10T20:41:06.385Z
```

RATPlus-API converts IDFM timestamps to:

```text
Europe/Paris
```

For example:

```text
2026-08-10T20:41:06.385Z
```

becomes:

```text
2026-08-10T22:41:06.385
```

The conversion is performed using Java's `Europe/Paris` timezone.

Daylight-saving time is handled automatically.

---

# IDFM Integration

RATPlus-API uses the Île-de-France Mobilités APIs for:

* Transport lines
* Stations / stop points
* Real-time departures

The backend uses dedicated `NavitiaClient` and `IdfmClient` to communicate with the external API.

The IDFM API key is sent by the client when making requests.

The external API response is mapped to internal DTOs before being returned by RATPlus-API.

---

# Running Locally

Make sure Java 26 and Maven are installed.

Run the application with:

```bash
./mvnw spring-boot:run
```

The API will be available at:

```text
http://localhost:8081
```

Example:

```text
http://localhost:8081/api/lines
```

---

# Build

Create the application JAR:

```bash
./mvnw clean package
```

The generated JAR will be located in:

```text
target/
```

---

# Docker

The application can also be packaged and run using Docker.

The project contains a `Dockerfile` for containerized deployment.

The application listens on port:

```text
8081
```

When deploying to a hosting platform, the application port can be configured according to the platform requirements.

---

# Deployment

The backend is designed to be deployed as a Dockerized Spring Boot application.

Current deployment target:

**Render**

The IDFM API key must be configured as an environment variable on the deployment platform and must not be committed to the repository.

---

# Security

The following information must never be committed to Git:

* IDFM API key
* Production secrets
* Credentials
* Environment-specific private configuration

Use environment variables for sensitive configuration.

---

# Current Status

Implemented:

* [x] Spring Boot project
* [x] Java 26
* [x] IDFM API integration
* [x] Retrieve supported transport lines
* [x] Filter Metro / RER / Tram / Transilien
* [x] Retrieve stations for a line
* [x] Group multiple stop points belonging to the same station
* [x] Retrieve real-time departures
* [x] Merge departures from multiple stop points
* [x] Convert timestamps to Europe/Paris
* [x] Sort departures by time
* [x] Spring Boot Actuator
* [x] Docker configuration
* [x] Local API running on port 8081

---

# Future Improvements

Potential future improvements include:

* Better error handling for IDFM API failures
* Validation of API request parameters
* Caching of static line/station data
* Improved departure filtering
* Automated tests
* Production monitoring
* API documentation with OpenAPI / Swagger
