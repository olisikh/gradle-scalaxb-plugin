package com.github.alisiikh.scalaxb
import java.io.{File, FileWriter, PrintWriter}
import java.nio.file.Path

import org.gradle.internal.impldep.org.apache.commons.io.FileUtils
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FreeSpecLike}

trait FunctionalSpec extends FreeSpecLike with BeforeAndAfter with BeforeAndAfterAll {

  def schemasFolder: Path

  val testProjectDir = new TemporaryFolder

  var buildFile: File = _

  override def beforeAll(): Unit = {
    require(schemasFolder.toFile.isDirectory, "schemasFolder must be a directory!")

    super.beforeAll()
    testProjectDir.create()
    testProjectDir.newFolder("src", "main", "resources")

    FileUtils.copyDirectory(schemasFolder.toFile,
                            new File(testProjectDir.getRoot.getAbsolutePath, "src/main/resources"))
  }

  override def afterAll(): Unit = {
    super.afterAll()
    testProjectDir.delete()
  }

  before {
    buildFile = testProjectDir.newFile("build.gradle")

    withWriter(buildFile) { writer =>
      writer.write(
        """plugins {
          |  id 'scala'
          |  id 'com.github.alisiikh.scalaxb'
          |}
          |
          |repositories {
          |  mavenLocal()
          |  mavenCentral()
          |  jcenter()
          |}
          |
          |dependencies {
          |  compile 'org.scala-lang:scala-library:2.12.3'
          |  scalaxbRuntime 'org.scalaxb:scalaxb_2.12:1.5.2'
          |}
          |
          |scalaxb {
          |  packageName = 'com.github.alisiikh.generated'
          |  srcDir = file("$projectDir/src/main/resources")
          |  destDir = file("$buildDir/generated/src/main/scala")
          |}
        """.stripMargin
      )
    }
  }

  after {
    buildFile.delete()
  }

  def withWriter(file: File, append: Boolean = false)(body: PrintWriter => Unit): Unit = {
    val writer = new PrintWriter(new FileWriter(file, append))
    try {
      body(writer)
    } finally {
      writer.flush()
      writer.close()
    }
  }
}
