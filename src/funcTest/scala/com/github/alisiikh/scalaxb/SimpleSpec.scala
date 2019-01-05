/**
 * Copyright (c) 2019 alisiikh@gmail.com <Oleksii Lisikh>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.alisiikh.scalaxb

import java.nio.file.{Path, Paths}

import org.gradle.testkit.runner.TaskOutcome
import org.scalatest._

class SimpleSpec extends FunctionalSpec with Matchers with OptionValues {

  override def schemasFolder: Path = Paths.get(getClass.getResource("/simple").getPath)

  "Scalaxb plugin" - {
    "generates scala sources from xsd schemas when" - {
      ":generateScalaxb task is invoked directly" in {
        runGradle("clean", "generateScalaxb") { result =>
          result.task(":generateScalaxb").getOutcome shouldBe TaskOutcome.SUCCESS
          checkScalaxbOutput(result.getOutput)
        }
      }

      ":generateScalaxb task is run before :build task" in {
        runGradle("clean", "build") { result =>
          result.task(":generateScalaxb").getOutcome shouldBe TaskOutcome.SUCCESS
          checkScalaxbOutput(result.getOutput)
        }
      }

      ":generateScalaxb task is run before :compileScala task" in {
        runGradle("clean", "compileScala") { result =>
          result.task(":compileScala").getOutcome shouldBe TaskOutcome.SUCCESS
          checkScalaxbOutput(result.getOutput)
        }
      }

      ":generateScalaxb is supports incremental cache" in {
        runGradle("clean", "generateScalaxb") { result =>
          result.task(":generateScalaxb").getOutcome shouldBe TaskOutcome.SUCCESS
          checkScalaxbOutput(result.getOutput)
        }

        runGradle("generateScalaxb") { result =>
          result.task(":generateScalaxb").getOutcome shouldBe TaskOutcome.UP_TO_DATE
        }
      }
    }
  }

  def checkScalaxbOutput(buildOutput: String): Unit =
    List(
      "generated .*/generated/scala/com/github/alisiikh/generated/root.scala.",
      "generated .*/generated/scala/com/github/alisiikh/generated/xmlprotocol.scala.",
      "generated .*/generated/scala/scalaxb/scalaxb.scala."
    ).map(_.r)
      .foreach {
        _.findFirstIn(buildOutput) shouldBe 'defined
      }
}
