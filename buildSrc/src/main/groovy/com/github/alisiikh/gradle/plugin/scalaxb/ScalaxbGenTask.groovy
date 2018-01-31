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

import org.gradle.api.tasks.JavaExec

class ScalaxbGenTask extends JavaExec {

    ScalaxbGenTask() {
    }

    void exec() {
        def ext = project.extensions.getByType(ScalaxbExtension)

        project.configurations.scalaxbRuntime.each { println it }

        def xsdFiles = ext.xsdDir.listFiles(new FileFilter() {
            @Override
            boolean accept(File file) {
                return file.name.endsWith(".xsd")
            }
        })

        xsdFiles.each { xsdFile ->
            project.javaexec {
                classpath = project.configurations.scalaxbRuntime
                main = 'scalaxb.compiler.Main'

                systemProperties = System.properties as Map
                standardInput = System.in
                standardOutput = System.out

                args xsdFile.absolutePath
                args "-p", ext.packageName
                args "-d", ext.destDir

                if (ext.packageDir) {
                    args "--package-dir"
                }
            }
        }
    }
}
