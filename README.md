# gradle-scalaxb-plugin
Gradle plugin for Scalaxb http://scalaxb.org/

[![Build Status](https://travis-ci.org/alisiikh/gradle-scalaxb-plugin.svg?branch=master)](https://travis-ci.org/alisiikh/gradle-scalaxb-plugin)

## Installation
https://plugins.gradle.org/plugin/com.github.alisiikh.scalaxb

## Basic setup
1. Add scalaxb dependency to scalaxbRuntime configuration:
```groovy
dependencies {
   scalaxbRuntime 'org.scalaxb:scalaxb_2.12:1.5.3'
}
```

2. Configure scalaxb plugin

```groovy
scalaxb {
    packageName = 'com.example.generated'
    srcDir = file("$projectDir/src/main/resources/xsd")
    destDir = file("$buildDir/generated/src/main/scala")
}
```

3. Add generated sources folder to the sourceSet where you require them

```groovy
sourceSets {
    test {
        scala.srcDirs "$buildDir/generated/src/main/scala"
    }
}
```

With this setup, plugin generates scalaxb scala classes in `build/generated/src/main/scala` folder of your project with taken xsd schemas from `src/main/resources/xsd` folder of your project, and the package name `com.example.generated` will be used

