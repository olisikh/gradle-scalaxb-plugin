package com.github.alisiikh.scalaxb

import java.nio.file.{Path, Paths}

import org.gradle.testkit.runner.TaskOutcome
import org.scalatest._

class SimpleSpec extends FunctionalSpec with Matchers with OptionValues {

  override def schemasFolder: Path = Paths.get(getClass.getResource("/simple").getPath)

  "Scalaxb plugin" - {
    "generates scala sources from xsd schemas when" - {
      ":generateScalaxb task is invoked directly" in {
        runTask("generateScalaxb") { result =>
          result.task(":generateScalaxb").getOutcome shouldBe TaskOutcome.SUCCESS
          checkScalaxbOutput(result.getOutput)
        }
      }

      ":generateScalaxb task is called during :build task" in {
        runTask("build") { result =>
          result.task(":generateScalaxb").getOutcome shouldBe TaskOutcome.SUCCESS
          checkScalaxbOutput(result.getOutput)
        }
      }

      ":generateScalaxb task is called during :processResources task" in {
        runTask("processResources") { result =>
          result.task(":generateScalaxb").getOutcome shouldBe TaskOutcome.SUCCESS
          checkScalaxbOutput(result.getOutput)
        }
      }
    }
  }

  def checkScalaxbOutput(buildOutput: String): Unit =
    List(
      "generated .*/generated/src/main/scala/com/github/alisiikh/generated/root.scala.",
      "generated .*/generated/src/main/scala/com/github/alisiikh/generated/xmlprotocol.scala.",
      "generated .*/generated/src/main/scala/scalaxb/scalaxb.scala."
    ).map(_.r)
      .foreach {
        _.findFirstIn(buildOutput) shouldBe 'defined
      }
}
