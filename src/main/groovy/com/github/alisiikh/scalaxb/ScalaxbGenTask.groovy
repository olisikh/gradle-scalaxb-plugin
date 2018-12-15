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

import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.OutputDirectory

class ScalaxbGenTask extends JavaExec {

    private static final def schemaFilter = new FileFilter() {
        @Override
        boolean accept(File file) {
            return file.name.endsWith(".xsd") || file.name.endsWith(".wsdl")
        }
    }

    ScalaxbGenTask() {
    }

    ScalaxbExtension getExtension() {
        project.extensions.getByType(ScalaxbExtension)
    }

    @InputDirectory
    File getSrcDir() {
        getExtension().srcDir
    }

    @OutputDirectory
    File getOutputDir() {
        getExtension().destDir
    }

    @Override
    void exec() {
        def ext = project.extensions.getByType(ScalaxbExtension)

        def schemaFiles = ext.srcDir.listFiles(schemaFilter).toList()
        if (schemaFiles) {
            project.javaexec { spec ->
                classpath = project.configurations.scalaxbRuntime
                main = 'scalaxb.compiler.Main'

                workingDir = ext.srcDir
                systemProperties = System.properties as Map
                standardInput = System.in
                standardOutput = System.out

                ext.with {
                    schemaFiles.each {
                        spec.args it.name
                    }

                    spec.args "-d", destDir
                    if (packageDir) {
                        spec.args "--package-dir"
                    }
                    if (packageName) {
                        spec.args "-p", packageName
                    }
                    packages?.each {
                        spec.args "--package:${it.key}=${it.value}"
                    }
                    if (classPrefix) {
                        spec.args "--class-prefix", classPrefix
                    }
                    if (paramPrefix) {
                        spec.args "--param-prefix", paramPrefix
                    }
                    if (wrapContents) {
                        spec.args "--wrap-contents", wrapContents
                    }
                    if (contentsLimit) {
                        spec.args "--contents-limit", contentsLimit
                    }
                    if (chunkSize) {
                        spec.args "--chunk-size", chunkSize
                    }
                    if (protocolFile) {
                        spec.args "--protocol-file", protocolFile
                    }
                    if (protocolPackage) {
                        spec.args "--protocol-package", protocolPackage
                    }
                    if (prependFamily) {
                        spec.args "--prepend-family"
                    }
                    if (withRuntime) {
                        spec.args "--no-runtime"
                    }
                    if (laxAny) {
                        spec.args "--lax-any"
                    }
                    if (blocking) {
                        spec.args "--blocking"
                    }
                    if (dispatchVersion) {
                        spec.args "--dispatch-version", dispatchVersion
                    }
                    if (verbose) {
                        spec.args "--verbose"
                    }
                }
            }
        } else {
            project.logger.warn("No schema files found to generate scala classes from!")
        }
    }
}
