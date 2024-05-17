# Music Store Playlist Manager

## Overview
This project aims to develop a digital music store application focusing on playlist management. It focuses on using advanced techniques for compilation automation and continuous integration (CI). In addition, it combines Test-Driven Development (TDD) and Behavioral-Driven Development (BDD) approaches.

## Features
- Create new playlists.
- Delete existing playlists.
- Add favorite songs to playlists.
- Remove songs from playlists.

## Technologies
This application is developed in Java 8, following to TDD and BDD practices. It leverages several advanced tools and frameworks:
- **Mutation Testing**: Pitest
- **Code Coverage**: JaCoCo and Coveralls
- **Code Quality**: SonarCloud
- **Containerization**: Docker
- **CI Server**: GitHub Actions

The application is tested with GitHub Actions on Java 8, Java 11 and Java 17. Maven is used for the entire build process and dependency management. The GUI has been developed with Swing and the application uses a MongoDB as a database.

## Usage
From the directory containing `pom.xml`, you can build the project and run all tests with the following Maven command:
```sh
mvn clean verify
```
This command will build the application and run all tests. You can use two specific profiles for enhanced testing:
- **Code Coverage**: `jacoco`
- **Mutation Testing**: `pitest`

To include both Code Coverage and Mutation Testing, run:
```sh
mvn clean verify -Pjacoco,pitest
```
You can skip certain tests using the following variables: `skipUT`, `skipIT`, and `skipBDD`.

Before running the application, start a Docker container for MongoDB:
```sh
docker run -p 27017:27017 --rm mongo:6.0.14
```
Then, run the application with:
```sh
java -jar target\music-store-0.0.1-jar-with-dependencies.jar
```

### MongoDB Configuration
Configure the MongoDB connection with the following parameters:
- `--mongo-host`: MongoDB host address (default: `localhost`)
- `--mongo-port`: MongoDB port (default: `27017`)
- `--db-name`: Database name (default: `music_store`)
- `--db-song-collection`: Song collection name (default: `songs`)
- `--db-playlist-collection`: Playlist collection name (default: `playlists`)

## Badges
- **Build Status**:
<div style="display: flex; flex-direction: row; gap: 10px;">
    <img src="https://github.com/DavideDelBimbo/Music-Store/actions/workflows/maven.yml/badge.svg" alt="Build">&nbsp;&nbsp;
    <img src="https://github.com/DavideDelBimbo/Music-Store/actions/workflows/pitest.yml/badge.svg" alt="Mutation Testing">
</div>
- **Coveralls**:
<div style="display: flex; flex-direction: row; gap: 10px;">
    <img src="https://coveralls.io/repos/github/DavideDelBimbo/Music-Store/badge.svg" alt="Coverage Status">
</div>
- **SonarCloud**:
<div style="display: flex; flex-direction: row; gap: 10px;">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=DavideDelBimbo_Music-Store&metric=alert_status" alt="Quality Gate Status">&nbsp;&nbsp;
    <img src="https://sonarcloud.io/api/project_badges/measure?project=DavideDelBimbo_Music-Store&metric=bugs" alt="Bugs">&nbsp;&nbsp;
    <img src="https://sonarcloud.io/api/project_badges/measure?project=DavideDelBimbo_Music-Store&metric=vulnerabilities" alt="Vulnerabilities">&nbsp;&nbsp;
    <img src="https://sonarcloud.io/api/project_badges/measure?project=DavideDelBimbo_Music-Store&metric=security_rating" alt="Security Rating">&nbsp;&nbsp;
    <img src="https://sonarcloud.io/api/project_badges/measure?project=DavideDelBimbo_Music-Store&metric=code_smells" alt="Code Smells">&nbsp;&nbsp;
    <img src="https://sonarcloud.io/api/project_badges/measure?project=DavideDelBimbo_Music-Store&metric=coverage" alt="Coverage">&nbsp;&nbsp;
    <img src="https://sonarcloud.io/api/project_badges/measure?project=DavideDelBimbo_Music-Store&metric=duplicated_lines_density" alt="Duplicated Lines (%)">
</div>