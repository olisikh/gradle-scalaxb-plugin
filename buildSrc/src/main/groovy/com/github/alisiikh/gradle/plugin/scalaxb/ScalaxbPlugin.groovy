/*
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
package com.github.alisiikh.gradle.plugin.scalaxb

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.scala.ScalaPlugin
import scalaxb.compiler.Config
import scalaxb.compiler.Config$
import scalaxb.compiler.ConfigEntry
import scalaxb.compiler.Module

import static scala.collection.JavaConverters.asScalaBuffer
import static scala.collection.JavaConverters.mapAsScalaMap
import static scalaxb.compiler.Module$.MODULE$ as Module
import static scala.collection.Map$.MODULE$ as ScalaMap
import static scala.Option$.MODULE$ as Option
import static scala.Some$.MODULE$ as Some

class ScalaxbPlugin implements Plugin<Project> {

    private def SCALAXB_EXT_NAME = 'scalaxb'
    private def SCALAXB_TASK_NAME = 'generateScalaxb'

    private Project project

    @Override
    void apply(Project project) {
        this.project = project

        project.pluginManager.apply ScalaPlugin

        def scalaxbExt = project.extensions.create(SCALAXB_EXT_NAME, ScalaxbExtension, project) as ScalaxbExtension

        project.task(SCALAXB_TASK_NAME) {
            group = 'scalaxb'

            doLast {
                scalaxbExt.destDir?.mkdirs()

                Module module = Module.moduleByFileName(scalaxbExt.xsdDir)

                def xsdFiles = collectXsdFiles(scalaxbExt)

                Config config = buildConfig(scalaxbExt)
                module.processFiles(asScalaBuffer(Arrays.asList(xsdFiles)), config)
            }
        }
        project.tasks.findByName('compileScala').dependsOn(SCALAXB_TASK_NAME)
    }

    private Config buildConfig(ScalaxbExtension scalaxbExt) {
        def packages = ScalaMap.apply(mapAsScalaMap([
                (Option.empty()): Some.apply(scalaxbExt.packageName)
        ]).toSeq())

        [
                ConfigEntry.GeneratePackageDir$.MODULE$,
                new ConfigEntry.Outdir(scalaxbExt.destDir),
                new ConfigEntry.PackageNames(packages)
        ].inject(Config$.MODULE$['default'] as Config) { Config cfg, ConfigEntry cfgItem ->
            cfg.update(cfgItem)
        }
    }

    private File[] collectXsdFiles(ScalaxbExtension scalaxbExt) {
        scalaxbExt.xsdDir.listFiles(new FilenameFilter() {
            @Override
            boolean accept(File dir, String name) {
                name.endsWith('.xsd')
            }
        })
    }
}
