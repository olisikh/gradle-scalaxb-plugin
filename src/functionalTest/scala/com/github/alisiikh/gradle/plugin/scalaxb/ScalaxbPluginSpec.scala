package com.github.alisiikh.gradle.plugin.scalaxb

import java.io.{File, FileWriter, PrintWriter}

import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.{GradleRunner, TaskOutcome}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FreeSpecLike, Matchers}

import scala.io.Source

class ScalaxbPluginSpec extends FreeSpecLike with Matchers with BeforeAndAfter with BeforeAndAfterAll {

  val deleteTestFolderOnExit = true

  val testProjectDir = new TemporaryFolder

  var buildFile: File = _
  var xsdFile: File = _

  override def beforeAll: Unit = {
    super.beforeAll()
    testProjectDir.create()
    testProjectDir.newFolder("src", "main", "resources", "xsd")

    val xsdFile = testProjectDir.newFile("src/main/resources/xsd/sample.xsd")

    withWriter(xsdFile) { writer =>
      writer.write(
        Source
          .fromInputStream(getClass.getResourceAsStream("/xsd/simple.xsd"))
          .mkString
      )
    }
  }

  override def afterAll: Unit = {
    super.afterAll()

    if (deleteTestFolderOnExit) testProjectDir.delete()
  }

  before {
    buildFile = testProjectDir.newFile("build.gradle")

    withWriter(buildFile) { writer =>
      writer.write("""
          |plugins {
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
        """.stripMargin)
    }
  }

  after {
    buildFile.delete()
  }

  // TODO: check that build/generated folder contains scala sources after :generateScalaxb task is performed
  "Scalaxb plugin" - {
    "generates scala sources when" - {
      ":generateScalaxb task is invoked directly" in {
        injectScalaxbPluginConfig()

        val result = GradleRunner.create
          .withProjectDir(testProjectDir.getRoot)
          .withArguments("generateScalaxb")
          .withPluginClasspath()
          .build

        val taskResult = result.task(":generateScalaxb")
        taskResult.getOutcome shouldBe TaskOutcome.SUCCESS
      }

      ":generateScalaxb task is called during :build task" in {
        injectScalaxbPluginConfig()

        val result = GradleRunner.create
          .withProjectDir(testProjectDir.getRoot)
          .withArguments("build")
          .withPluginClasspath()
          .build

        val taskResult = result.task(":generateScalaxb")
        taskResult.getOutcome shouldBe TaskOutcome.SUCCESS
      }
    }
  }

  def injectScalaxbPluginConfig(): Unit =
    withWriter(buildFile, append = true) { writer =>
      writer.append(
        """
          |scalaxb {
          |    packageName = 'com.github.alisiikh.generated'
          |    srcDir = file("$buildDir/src/main/resources/xsd")
          |    destDir = file("$buildDir/generated/src/main/scala")
          |}
        """.stripMargin
      )
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
