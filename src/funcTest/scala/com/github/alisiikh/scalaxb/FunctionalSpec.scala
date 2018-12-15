/**
 * Copyright (c) 2018 alisiikh@gmail.com <Oleksii Lisikh>
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

import java.io.{File, FileWriter, PrintWriter}
import java.nio.file.Path

import org.gradle.internal.impldep.org.apache.commons.io.FileUtils
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.{BuildResult, GradleRunner}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FreeSpecLike}

trait FunctionalSpec extends FreeSpecLike with BeforeAndAfter with BeforeAndAfterAll {

  def schemasFolder: Path
  def scalaxbOverrides: String = ""

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

    withBuildFileWriter(buildFile) { writer =>
      writer.write(
        s"""plugins {
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
          |  compile 'org.scala-lang:scala-library:2.12.6'
          |}
          |
          |scalaxb {
          |  packageName = 'com.github.alisiikh.generated'
          |  srcDir = file("$$projectDir/src/main/resources")
          |  destDir = file("$$buildDir/generated/scala")
          |  verbose = true
          |  $scalaxbOverrides
          |}
        """.stripMargin
      )
    }
  }

  after {
    buildFile.delete()
  }

  def runGradle(args: String*)(f: BuildResult => Unit): Unit =
    f {
      val result = GradleRunner.create
        .withProjectDir(testProjectDir.getRoot)
        .withArguments(args: _*)
        .withPluginClasspath()
        .build()

      println(result.getOutput)
      result
    }

  def withBuildFileWriter(file: File, append: Boolean = false)(body: PrintWriter => Unit): Unit = {
    val writer = new PrintWriter(new FileWriter(file, append))
    try {
      body(writer)
    } finally {
      writer.flush()
      writer.close()
    }
  }
}
