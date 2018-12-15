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
package com.github.alisiikh.scalaxb


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.ConventionTask
import org.gradle.api.plugins.scala.ScalaPlugin

class ScalaxbPlugin implements Plugin<Project> {
    private final def SCALAXB_EXT_NAME = 'scalaxb'
    private final def SCALAXB_TASK_NAME = 'generateScalaxb'

    private Project project

    @Override
    void apply(Project project) {
        this.project = project

        project.pluginManager.apply(ScalaPlugin)

        def scalaxbExt = project.extensions.create(SCALAXB_EXT_NAME, ScalaxbExtension)

        createConfiguration()
        createTask(scalaxbExt)
    }

    def createConfiguration() {
        project.configurations {
            ['scalaxb', 'scalaxbRuntime'].each {
                create(it) {
                    visible = false
                }
            }

            scalaxbRuntime.extendsFrom(scalaxb)
            compile.extendsFrom(scalaxb)
        }

        project.afterEvaluate { p ->
            def ext = project.extensions.findByType(ScalaxbExtension)

            p.sourceSets.main {
                scala {
                    srcDirs += [ext.destDir]
                }
            }

            p.dependencies {
                scalaxb "org.scalaxb:scalaxb_${ext.scalaMajorVersion}:${ext.toolVersion}"
            }
        }
    }

    def createTask(ScalaxbExtension scalaxbExt) {
        def scalaxbGenTask = project.tasks.create(
                name: SCALAXB_TASK_NAME,
                type: ScalaxbGenTask,
                description: 'Generates scala sources from xsd schemas.',
                group: 'scalaxb'
        ) as ConventionTask

        scalaxbGenTask.convention.plugins[SCALAXB_EXT_NAME] = scalaxbExt

        project.tasks.findByName('compileScala').dependsOn(scalaxbGenTask)
    }
}
