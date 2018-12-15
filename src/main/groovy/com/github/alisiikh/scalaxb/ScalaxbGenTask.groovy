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

class ScalaxbGenTask extends JavaExec {

    private static final def schemaFilter = new FileFilter() {
        @Override
        boolean accept(File file) {
            return file.name.endsWith(".xsd") || file.name.endsWith(".wsdl")
        }
    }

    ScalaxbGenTask() {
    }

    @Override
    void exec() {
        def ext = project.extensions.getByType(ScalaxbExtension)

        def schemaFiles = ext.srcDir.listFiles(schemaFilter).toList()
        if (schemaFiles) {
            project.javaexec {
                classpath = project.configurations.scalaxbRuntime
                main = 'scalaxb.compiler.Main'

                systemProperties = System.properties as Map
                standardInput = System.in
                standardOutput = System.out

                ext.with {
                    args schemaFiles*.absolutePath.join(" ")
                    args "-d", destDir
                    if (packageDir) {
                        args "--package-dir"
                    }
                    if (packageName) {
                        args "-p", packageName
                    }
                    packages?.each {
                        args "--package:${it.key}=${it.value}"
                    }
                    if (classPrefix) {
                        args "--class-prefix", classPrefix
                    }
                    if (paramPrefix) {
                        args "--param-prefix", paramPrefix
                    }
                    if (wrapContents) {
                        args "--wrap-contents", wrapContents
                    }
                    if (contentsLimit) {
                        args "--contents-limit", contentsLimit
                    }
                    if (chunkSize) {
                        args "--chunk-size", chunkSize
                    }
                    if (protocolFile) {
                        args "--protocol-file", protocolFile
                    }
                    if (protocolPackage) {
                        args "--protocol-package", protocolPackage
                    }
                    if (prependFamily) {
                        args "--prepend-family"
                    }
                    if (withRuntime) {
                        args "--no-runtime"
                    }
                    if (laxAny) {
                        args "--lax-any"
                    }
                    if (blocking) {
                        args "--blocking"
                    }
                    if (dispatchVersion) {
                        args "--dispatch-version", dispatchVersion
                    }
                    if (verbose) {
                        args "--verbose"
                    }
                }
            }
        }
    }
}
