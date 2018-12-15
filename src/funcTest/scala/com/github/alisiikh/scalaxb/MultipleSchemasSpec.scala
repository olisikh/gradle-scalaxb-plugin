package com.github.alisiikh.scalaxb

import java.nio.file.{Path, Paths}

import org.gradle.testkit.runner.TaskOutcome
import org.scalatest._

class MultipleSchemasSpec extends FunctionalSpec with Matchers with OptionValues {

  override def schemasFolder: Path = Paths.get(getClass.getResource("/multiple").getPath)

  "Scalaxb plugin" - {
    "generates scala sources from multiple dependent xsd schema files" in {
      runTask("generateScalaxb") { result =>
        result.task(":generateScalaxb").getOutcome shouldBe TaskOutcome.SUCCESS
        checkScalaxbOutput(result.getOutput)
      }
    }
  }

  def checkScalaxbOutput(buildOutput: String): Unit =
    List(
      "generated .*/generated/src/main/scala/com/github/alisiikh/generated/root.scala.",
      "generated .*/generated/src/main/scala/com/github/alisiikh/generated/user.scala.",
      "generated .*/generated/src/main/scala/com/github/alisiikh/generated/profile.scala.",
      "generated .*/generated/src/main/scala/com/github/alisiikh/generated/xmlprotocol.scala.",
      "generated .*/generated/src/main/scala/scalaxb/scalaxb.scala."
    ).map(_.r)
      .foreach {
        _.findFirstIn(buildOutput) shouldBe 'defined
      }
}
