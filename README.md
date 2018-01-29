# gradle-scalaxb-plugin
Gradle plugin for Scalaxb http://scalaxb.org/

[![Build Status](https://travis-ci.org/alisiikh/gradle-scalaxb-plugin.svg?branch=master)](https://travis-ci.org/alisiikh/gradle-scalaxb-plugin)

## Installation
https://plugins.gradle.org/plugin/com.github.alisiikh.scalaxb

## Basic setup
```groovy
scalaxb {
    packageName = 'com.example.generated'
    xsdDir = file("$projectDir/src/main/resources/xsd")
    destDir = file("$buildDir/generated/src/main/scala")
}
```

With this setup, plugin generates scalaxb scala classes in `build/generated/src/main/scala` folder of your project with taken xsd schemas from `src/main/resources/xsd` folder of your project, and the package name `com.example.generated` will be used

You also need to inform your configuration to pick up the generated files to the classpath
```groovy
sourceSets {
    test {
        scala.srcDirs "$buildDir/generated/src/main/scala"
    }
}
```
