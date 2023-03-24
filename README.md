# Suricate Widget Tester

[![GitHub Build](https://img.shields.io/github/actions/workflow/status/michelin/suricate-widget-tester/continuous_integration.yml?branch=master&logo=github&style=for-the-badge)](https://img.shields.io/github/actions/workflow/status/michelin/suricate-widget-tester/continuous_integration.yml)
[![GitHub release](https://img.shields.io/github/v/release/michelin/suricate-widget-tester?logo=github&style=for-the-badge)](https://github.com/michelin/suricate-widget-tester/releases)
[![GitHub commits since latest release (by SemVer)](https://img.shields.io/github/commits-since/michelin/suricate-widget-tester/latest?logo=github&style=for-the-badge)](https://github.com/michelin/suricate-widget-tester/commits/master)
[![GitHub Stars](https://img.shields.io/github/stars/michelin/suricate-widget-tester?logo=github&style=for-the-badge)](https://github.com/michelin/suricate)
[![GitHub Watch](https://img.shields.io/github/watchers/michelin/suricate-widget-tester?logo=github&style=for-the-badge)](https://github.com/michelin/suricate)
[![SonarCloud Coverage](https://img.shields.io/sonar/coverage/michelin_suricate-widget-tester?logo=sonarcloud&server=https%3A%2F%2Fsonarcloud.io&style=for-the-badge)](https://sonarcloud.io/component_measures?id=michelin_suricate-widget-tester&metric=coverage&view=list)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?logo=apache&style=for-the-badge)](https://opensource.org/licenses/Apache-2.0)

This repository contains the source code of the Suricate Widget Tester application.

![Suricate widget tester](src/main/webapp/assets/images/widget-tester.png)

# Table of Contents

* [Introduction](#introduction)
* [Download](#download)
* [Usage](#usage)
* [Testing a Widget](#testing-a-widget)
  * [JavaScript Execution](#javascript-execution)
  * [Widget and Category Parameters](#widget-and-category-parameters)
  * [Display](#display)
  
# Introduction

The Widget Tester allows developers to test a widget before deploying it to a Suricate instance. It has multiple advantages:
- It's fast and easy to set up and use.
- It makes the development and testing of widgets easier and faster.
- It's fully independent from Suricate. A local Suricate instance is not required for it to work.

The widget tester is able to test the following points.

# Download

You can download the Widget Tester as a fat jar from the [GitHub releases page](https://github.com/michelin/suricate-widget-tester/releases). Please note that Java 8 is required.

# Usage

The Widget Tester is released as a JAR archive containing both the front-end and back-end parts, so you can run the whole application in just one command:

```console
java -jar suricate-widget-tester.jar
```

After running the command, the application will be accessible on http://localhost:8085/.

# Testing a Widget

## JavaScript Execution

The widget tester executes the JavaScript file of a widget. It can be used to ensure that:

- The calls to REST APIs provide the expected responses.
- The processing of REST API responses works as expected.
- The widget and category parameters work as expected.
- The return of the `run` method contains all the required data in the expected format.
- The syntax of the `script.js` file is correct.

In short, it ensures that the widget execution works, and helps identify issues if it doesn't.

## Widget and Category Parameters

The widget tester is able to read the widget and category parameters and display them in the user interface. Users can fill them before they are injected into the JavaScript part for widget execution.

This ensures that the parameters work as expected during widget execution.

## Display

The widget tester displays the widget directly in the user interface so that the user can see what it looks like. It can be used to ensure that:

- The widget rendering meets expectations.
- The computed data is displayed as expected.
- The resizing and moving of the widget work as expected.

If JavaScript libraries are defined in the `description.yml` file, they will be picked up from the `libraries` folder and injected into the DOM so that they are available for the widget.
