# Feis Spring Archetype

Simple yet useful Spring Boot Maven archetype

## Table of Contents

- [1.0 Brief Description](#10-brief-description)
  - [1.1 Details](#11-details)
- [2.0 Technologies](#20-technologies)
- [3.0 Features](#30-features)
- [4.0 How to use it](#40-how-to-use-it)

## 1.0 Brief Description

One of the most tedious parts of starting a project from scratch is the initial
configuration; building the initial structure, creating the configuration
classes, connecting it to a database, potentially setting up security,
connection to an external IDP, and so on…
For this reason, I wanted to build a project which would give a
small productivity boost for such cases, and, since my main
backend stack is Java + Maven + Spring Boot for almost any project,
the feature Maven offered to create reusable archetypes seemed like
a great way of learning. This project contains all the most basic
features a backend developer would be worried about writing on its own,
from abstract service layer classes with all the most basic CRUD operations,
validation, abstract classes for an easier entities creation by leveraging
Spring Data JPA features, basic security configuration (with option
for connecting it with an IDP such as Keycloak), database connection
properties, logging, and more! By just writing a simple command on the CLI
of your preference, you’ll be able to create a project full of
all these features without any kind of stress!
If you have time, you might find interest in reading the full document,
otherwise, you can directly skip to [4.0 How to use it](#40-how-to-use-it)
to find all instructions related to building your new, shiny project.

### 1.1 Details

This project was built following my personal [Style guides](https://github.com/mfacecchia/code-styleguide)
you may want to follow to keep the whole codebase styling consistent.

## 2.0 Technologies

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)

## 3.0 Features

This project contains most of the features a developer would want to have in a
mid-level/advanced Spring Boot project. The pre-configured features
featured in the project are:

- Logging
- Service layer abstraction for painless CRUD operations configuration
- Base classes for auditing, and Spring Data Entities
- Base DTO classes for either request and response
- Basic Spring Security configuration
- Abstract mapping classes for all entities
- Global Exceptions handler
- Custom App exceptions for the most basic operations
- Base abstract specification builder for most complex queries
- Pre-configured user MVC implementation (which you can take
  inspiration from to build your other entities)

## 4.0 How to use it

Building your new project is as easy as drinking a cup of coffee!
All you have to do is to copy-paste these simple commands in the shell of
your preference, and you'll be good to go!

```zsh
git clone https://github.com/mfacecchia/feis-spring-archetype && \
cd feis-spring-archetype && \
mvn clean install && \
cd .. && \
mvn archetype:generate \
 -DarchetypeGroupId=com.feis \
 -DarchetypeArtifactId=archetype \
 -DarchetypeVersion=1.0.0
```

This is currently the only way to build a project using this archetype, I will soon publish it to a registry to simplify the whole process.

**NOTE:** You may be prompted to input some information about your
new project, such as the GroupId and the ArtifactId.
