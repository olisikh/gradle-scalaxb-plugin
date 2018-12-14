package com.github.alisiikh.scalaxb

import java.nio.file.{Path, Paths}

import org.gradle.testkit.runner.{GradleRunner, TaskOutcome}
import org.scalatest._

class SimpleSpec
    extends FunctionalSpec
    with Matchers
    with OptionValues {

  override def schemasFolder: Path = Paths.get(getClass.getResource("/simple").getPath)

  "Scalaxb plugin" - {
    "generates scala sources when" - {
      ":generateScalaxb task is invoked directly" in {
        val result = buildTask("generateScalaxb").build()

        val taskResult = result.task(":generateScalaxb")
        taskResult.getOutcome shouldBe TaskOutcome.SUCCESS

        val buildOutput = result.getOutput
        checkScalaxbOutput(buildOutput)
      }

      ":generateScalaxb task is called during :build task" in {
        val result = buildTask("build").build()

        val taskResult = result.task(":generateScalaxb")
        taskResult.getOutcome shouldBe TaskOutcome.SUCCESS

        val buildOutput = result.getOutput
        checkScalaxbOutput(buildOutput)
      }

      ":generateScalaxb task is called during :processResources task" in {
        val result = buildTask("processResources").build

        val taskResult = result.task(":generateScalaxb")
        taskResult.getOutcome shouldBe TaskOutcome.SUCCESS

        val buildOutput = result.getOutput
        checkScalaxbOutput(buildOutput)
      }
    }
  }


  def buildTask(name: String): GradleRunner = GradleRunner.create
    .withProjectDir(testProjectDir.getRoot)
    .withArguments(name)
    .withDebug(true)
    .withPluginClasspath()

  def checkScalaxbOutput(buildOutput: String): Unit = {
    "generated .*/generated/src/main/scala/com/github/alisiikh/generated/root.scala.".r
      .findFirstIn(buildOutput) shouldBe 'defined
    "generated .*/generated/src/main/scala/com/github/alisiikh/generated/xmlprotocol.scala.".r
      .findFirstIn(buildOutput) shouldBe 'defined
    "generated .*/generated/src/main/scala/scalaxb/scalaxb.scala.".r
      .findFirstIn(buildOutput) shouldBe 'defined
  }
}
