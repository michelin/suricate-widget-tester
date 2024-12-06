<div align="center">

<img src=".readme/logo.png" height="200" alt="Suricate"/>

# Suricate Widget Tester

[![GitHub Build](https://img.shields.io/github/actions/workflow/status/michelin/suricate-widget-tester/push_master.yml?branch=master&logo=github&style=for-the-badge)](https://img.shields.io/github/actions/workflow/status/michelin/suricate-widget-tester/push_master.yml)
[![GitHub release](https://img.shields.io/github/v/release/michelin/suricate-widget-tester?logo=github&style=for-the-badge)](https://github.com/michelin/suricate-widget-tester/releases)
[![GitHub commits since latest release (by SemVer)](https://img.shields.io/github/commits-since/michelin/suricate-widget-tester/latest?logo=github&style=for-the-badge)](https://github.com/michelin/suricate-widget-tester/commits/master)
[![GitHub Stars](https://img.shields.io/github/stars/michelin/suricate-widget-tester?logo=github&style=for-the-badge)](https://github.com/michelin/suricate)
[![GitHub Watch](https://img.shields.io/github/watchers/michelin/suricate-widget-tester?logo=github&style=for-the-badge)](https://github.com/michelin/suricate)
[![SonarCloud Coverage](https://img.shields.io/sonar/coverage/michelin_suricate-widget-tester?logo=sonarcloud&server=https%3A%2F%2Fsonarcloud.io&style=for-the-badge)](https://sonarcloud.io/component_measures?id=michelin_suricate-widget-tester&metric=coverage&view=list)
[![SonarCloud Tests](https://img.shields.io/sonar/tests/michelin_suricate-widget-tester/master?server=https%3A%2F%2Fsonarcloud.io&style=for-the-badge&logo=sonarcloud)](https://sonarcloud.io/component_measures?metric=tests&view=list&id=michelin_suricate-widget-tester)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?logo=apache&style=for-the-badge)](https://opensource.org/licenses/Apache-2.0)

[Install](#install) • [Suricate](https://github.com/michelin/suricate) • [Widgets](https://github.com/michelin/suricate-widgets)

Build and validate your Suricate widget in a sandboxed setup before integrating it into a Suricate instance.

![Suricate widget tester](.readme/dashboard.png)

</div>

## Table of Contents

* [Download](#download)
* [Install](#install)
* [Configuration](#configuration)
  * [Repository](#repository)
* [Contribution](#contribution)

## Download

You can download the Suricate Widget Tester as a fat jar from the [GitHub releases page](https://github.com/michelin/suricate-widget-tester/releases) (requires Java 21).

## Install

The Suricate Widget Tester is built on the [Spring Boot framework](https://spring.io/) and can be configured using a Spring Boot
configuration file, which includes a sample file located at `src/main/resources/application.properties`.

If necessary, you can override the properties from the default `application.properties` file by following
the [Spring Boot externalized configuration guide](https://docs.spring.io/spring-boot/reference/features/external-config.html).
For example, you can create a custom  `/config/application.properties` or set the `--spring.config.location` system
property when running the fat jar file:

```console
java -jar suricate-widget-tester.jar --spring.config.location=classpath:\,file:C:\myCustomLocation\
```

After running the command, the application will be accessible on http://localhost:8085/.

## Configuration

### Repository

The Suricate Widget Tester reads the content of a widget repository. 
The repository must follow the required structure (see the [official open-source widgets GitHub repository](https://github.com/michelin/suricate-widgets)).

The repository location can be configured with the following property:

```yml
application.widgets.repository: <path-to-repository>
```

It is set to `/tmp` by default.

## Contribution

We welcome contributions from the community! Before you get started, please take a look at our [contribution guide](https://github.com/michelin/suricate-widget-tester/blob/master/CONTRIBUTING.md) to learn about our guidelines and best practices. We appreciate your help in making Suricate a better tool for everyone.
