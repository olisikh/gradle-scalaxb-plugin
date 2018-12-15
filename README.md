# gradle-scalaxb-plugin
Gradle plugin for Scalaxb http://scalaxb.org/

[![Build Status](https://travis-ci.org/alisiikh/gradle-scalaxb-plugin.svg?branch=master)](https://travis-ci.org/alisiikh/gradle-scalaxb-plugin)

## Installation
Please have a look at Gradle Plugins Portal [guide](https://plugins.gradle.org/plugin/com.github.alisiikh.scalaxb)

## Basic setup
No additional configuration is required, but there are options for overriding.

Plugin can be configured the following way:
```groovy
scalaxb {
  srcDir = file("$projectDir/src/main/resources/schemas") // default: src/main/resources
  destDir = file("$buildDir/generated/scala")  // default: build/generated/scala
  
  toolVersion = '1.5.2' // scalaxb version, default: 1.5.2
  scalaMajorVersion = '2.12' // default: 2.12
  
  // scalaxb specific settings
  packageName = "my.generated.stuff" // target package, default: empty  
  packageDir = true // generates package folders, default: false
  packages = ["http://some.namespace": "some.namespace"] // specifies the target package for namespaceURI, default: empty
  autoPackages = false // generates packages for different namespaces automatically, default: false
  mutable = false // generates mutable classes, default: false
  visitor = false // generates visitor, default: false
  laxAny = false // relaxes namespace constraints of xs:any, default: false
  dispatchAs = false // generates Dispatch "as", default: false
  blocking = false // generates blocking SOAP client, default: false
  dispatchVersion = "" // version of Dispatch, default: provided by scalaxb
  noDispatchClient = false // disables generation of Dispatch client, default: false
  verbose = false // be extra verbose, default: false
  noVarargs = false // uses Seq instead of the varargs, default: false
  noRuntime = false // skips runtime files, default: false 
  ignoreUnknown = false // ignores unknown Elements, default: false
  protocolPackage = "my.protocol.package" // package for protocols, default: empty
  protocolFile = "myxmlprotocol.scala" // protocol file name, default: provided by scalaxb (xmlprotocol.scala)
  namedAttributes = false // generates named fields for attributes, default: false
  chunkSize = 1024 // segments long sequences into chunks, default: provided by scalaxb (10)
  contentLimit = 1024 * 1024 // defines long contents to be segmented (default: max)
  wrapContents = "HaveNoIdeaWhatIsThat" // wraps inner contents into a separate case class
  classPrefix = "Generated" // prefixes generated class names, default: empty
  paramPrefix = "g" // prefixes generated parameter names, default: empty
  attributePrefix = "g" // prefixes generated attribute parameters, default: empty
  prependFamily = false // prepends family name to class names, default: false
}
```

## Example project

Please have a look here: [example](https://github.com/alisiikh/gradle-scalaxb-plugin/tree/master/example)

With this setup, plugin generates scalaxb scala classes in `build/generated/scala`
folder of your project from xsd schemas at `src/main/resources` folder, 
and uses the package name `com.github.alisiikh.example`.
