# SpringClient - A Simple Spring Application

This application demonstrates simple examples for how to use Spring Boot, Spring MVC and Spring for REST APIs and Spring Service.

## Run

Using the Maven Wrapper,

```console
$ ./mvnw clean install
$ ./mvnw spring-boot:run
```

Without Maven Wrapper,

```console
$ mvn clean install
$ mvn spring-boot:run
```

## Test

```console
$ curl -X GET 'http://localhost:8080/api/hello?name=pluto'
```

## Maven

To download all dependencies for development, run 

```console
$ mvn dependency:copy-dependencies
```

## Jenkins

```text
node {
    stage 'Checkout'
    git branch: 'master', url: 'https://github.com/remkohdev/springclient.git'   
    // Get the maven tool
    // ** NOTE: This 'M3' maven tool must be configured in the global tools configuration
    def mvnHome = tool 'M3'
    
    stage 'Build'
    sh "${mvnHome}/bin/mvn -f pom.xml clean install -DskipTests"
 
    stage 'Test'
    sh "${mvnHome}/bin/mvn -f pom.xml test"
}
```


## Docs

See [Java: Get Started with Spring Boot](https://medium.com/nycdev/big-java-get-booted-with-spring-1896055c3803) and [Java: Create a Spring MVC App with Tomcat and Maven](https://medium.com/nycdev/java-build-a-tomcat-web-app-with-maven-and-spring-fbc823fa9a37) for a tutorial on Spring Boot and Spring MVC.
