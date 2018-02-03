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

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.ConventionTask

class ScalaxbPlugin implements Plugin<Project> {
    private final def SCALAXB_EXT_NAME = 'scalaxb'
    private final def SCALAXB_TASK_NAME = 'generateScalaxb'

    private final def SCALAXB_DEP_HINT = "dependencies {\n" +
            "   scalaxbRuntime 'org.scalaxb:scalaxb_2.12:1.5.2'\n" +
            "}"

    private Project project

    @Override
    void apply(Project project) {
        this.project = project

        def scalaxbExt = project.extensions.create(SCALAXB_EXT_NAME, ScalaxbExtension, project) as ScalaxbExtension

        createConfiguration()
        createTasks(scalaxbExt)
    }

    def createConfiguration() {
        project.configurations {
            create('scalaxbRuntime') {
                visible = false
            }
        }

        project.afterEvaluate { p ->
            def scalaxbDependency = p.configurations.scalaxbRuntime.find { it.name.matches("scalaxb_.*-.*\\.jar") }
            if (!scalaxbDependency) {
                throw new GradleException("No scalaxb dependency found in the scalaxbRuntime classpath.\n" +
                        SCALAXB_DEP_HINT)
            }
        }
    }

    def createTasks(ScalaxbExtension scalaxbExt) {
        def scalaxbGenTask = project.tasks.create(
                name: SCALAXB_TASK_NAME,
                type: ScalaxbGenTask,
                description: "Generate scalaxb sources", group: "scalaxb"
        ) as ConventionTask

        scalaxbGenTask.convention.plugins["scalaxb"] = scalaxbExt

        def scalaCompile = project.tasks.findByName('compileScala')
        scalaCompile?.dependsOn(scalaxbGenTask)
    }
}
