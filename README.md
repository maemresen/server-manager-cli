
# Server Manager CLI

[![Docker Hub](https://img.shields.io/badge/Docker-Hub-blue)](https://hub.docker.com/repository/docker/maemresen/server-manager-cli/general)

Server Manager CLI is a lightweight proof-of-concept (POC) command-line tool for managing server operations. It demonstrates essential server management capabilities, such as starting, stopping, checking server status, and viewing server history. The workflow involves storing server event data in a file-based H2 database, making it easy to run locally or via Docker.

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Installation](#installation)
3. [Usage](#usage)
4. [Supported Commands](#supported-commands)
5. [Features](#features)
6. [Testing](#testing)
7. [Environment Configuration](#environment-configuration)
8. [Build Configuration](#build-configuration)
9. [License](#license)

---

## Prerequisites

Before getting started, ensure you have the following installed:

- **Java 21**: For running the application locally.
- **Gradle**: For building the application.
- **Docker**: For running the application in a containerized environment (optional).

---

## Installation

### Local Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Build the project using Gradle:
   ```bash
   ./gradlew shadowJar
   ```
3. The runnable JAR will be located in the `build/libs/` directory.

### Using Docker

Pull the Docker image:
```bash
docker pull maemresen/server-manager-cli
```

---

## Usage

### Running Locally

1. Build the JAR:
   ```bash
   ./gradlew shadowJar
   ```
2. Use these commands to manage the server:
    - **Start the Server**:
      ```bash
      java -jar build/libs/server-manager-cli-all.jar up
      ```
    - **Stop the Server**:
      ```bash
      java -jar build/libs/server-manager-cli-all.jar down
      ```
    - **Check Server Status**:
      ```bash
      java -jar build/libs/server-manager-cli-all.jar status
      ```
    - **View Server History**:
      ```bash
      java -jar build/libs/server-manager-cli-all.jar history
      ```

#### H2 Database Behavior
When running the application locally, the default JDBC connection string is:
```bash
jdbc:h2:file:./data/server-status;AUTO_SERVER=TRUE
```
This means:
- A `data` folder will be created in the current working directory.
- The H2 database file `server-status` will be stored in this folder.

For example, you can connect to the database using the H2 Console or a JDBC-based database client.

### Running with Docker

Run commands using Docker:

- **Start the Server**:
  ```bash
  docker run --rm -v /$(PWD)/data:/app/data maemresen/server-manager-cli up
  ```
- **Stop the Server**:
  ```bash
  docker run --rm -v /$(PWD)/data:/app/data maemresen/server-manager-cli down
  ```
- **Check Server Status**:
  ```bash
  docker run --rm -v /$(PWD)/data:/app/data maemresen/server-manager-cli status
  ```
- **View Server History**:
  ```bash
  docker run --rm -v /$(PWD)/data:/app/data maemresen/server-manager-cli history
  ```

#### Why Volume Binding is Needed?

In the above commands, the `-v /$(PWD)/data:/app/data` flag is used for volume binding. This is required to persist server event data stored in the file-based H2 database between Docker container runs. Without this, the database would be reset every time the container is stopped or restarted, leading to a loss of all recorded events.

By binding the container's `/app/data` directory to the local `data` directory, you ensure that the server's event data remains intact and accessible even after the container is removed or re-created.

---

## Supported Commands

### Status
**Command**:
```bash
status
```
**Description**:
- Retrieves the last server status event from the database (`up`, `down`, `starting`, `stopping`).
- If the server is `up`, it calculates and displays the uptime.

**Parameters**: None.

---

### Start Server
**Command**:
```bash
up [--before yyyy-mm-dd hh:mm]
```
**Description**:
- Starts the server and logs the event.
- Optionally specify `--before` to schedule an automatic stop.

**Parameters**:
- `--before yyyy-mm-dd hh:mm`: (Optional) Schedules a server stop at the given timestamp.

---

### Stop Server
**Command**:
```bash
down
```
**Description**:
- Stops the server and logs the event.

**Parameters**: None.

---

### View History
**Command**:
```bash
history [--from yyyy-mm-dd] [--to yyyy-mm-dd] [--sort asc|desc] [--status up|down|starting|stopping]
```
**Description**:
- Displays a list of server events with filtering options.

**Parameters**:
- `--from yyyy-mm-dd`: (Optional) Filter events starting from this date.
- `--to yyyy-mm-dd`: (Optional) Filter events up to this date.
- `--sort asc|desc`: (Optional) Sort events in ascending or descending order.
- `--status`: (Optional) Filter by specific server statuses.

---

## Features

- **Docker Support**: Seamlessly run the application using Docker.
- **Intuitive CLI**: Manage servers with simple commands.
- **History Tracking**: Keep a detailed record of server events with filtering options.
- **Flexible Execution**: Run locally or within a container.

---

## Testing

Run the following commands to test and verify the application:

- Unit tests:
  ```bash
  ./gradlew test
  ```
- Integration tests:
  ```bash
  ./gradlew integrationTest
  ```
- Verify code formatting:
  ```bash
  ./gradlew spotlessCheck
  ```
- Apply code formatting:
  ```bash
  ./gradlew spotlessApply
  ```

---

## Environment Configuration

Configure these environment variables as needed:

| Variable  | Default Value                                       |
|-----------|-----------------------------------------------------|
| `JDBC`    | `jdbc:h2:file:./data/server-status;AUTO_SERVER=TRUE` |
| `DB_USER` | `sa`                                                |
| `DB_PASS` | `-`                                                 |

---

## Build Configuration

### Plugins
- **Spotless**: Code formatting (version 6.25.0).
- **Shadow**: Fat JAR packaging (version 7.1.2).

### Libraries
- **Google Java Format**: Code formatting (version 1.24.0).
- **Commons CLI**: CLI option parsing (version 1.9.0).
- **JUnit**: Testing framework (version 5.11.3).
- **Lombok**: Annotation-based Java simplifications (version 1.18.36).
- **H2 Database**: Lightweight file-based database (version 2.2.220).
- **Logback Core**: Core logging functionality (version 1.5.6).
- **Logback Classic**: Advanced logging features (version 1.5.6).
- **SLF4J API**: Logging API (version 2.0.16).
- **AssertJ**: Fluent assertions (version 3.26.3).
- **Mockito Core**: Mocking framework (version 5.14.2).
- **Mockito JUnit Jupiter**: Integration for JUnit (version 5.14.2).
- **Google Guice**: Dependency injection framework (version 7.0.0).

---

## License

This project is licensed under the [MIT License](LICENSE).

---

Start managing your servers effortlessly with **Server Manager CLI**!
