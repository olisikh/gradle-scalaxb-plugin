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

    @InputDirectory
    File getSrcDir() {
        getExtension().getSrcDir()
    }

    @OutputDirectory
    File getDestDir() {
        getExtension().getDestDir()
    }

    private ScalaxbExtension getExtension() {
        project.extensions.getByType(ScalaxbExtension)
    }

    @Override
    void exec() {
        def ext = getExtension()

        def schemaFiles = getSrcDir().listFiles(schemaFilter).toList()
        if (schemaFiles) {
            project.javaexec { spec ->
                classpath = project.configurations.scalaxbRuntime
                main = 'scalaxb.compiler.Main'

                workingDir = getSrcDir()
                systemProperties = System.properties as Map
                standardInput = System.in
                standardOutput = System.out

                schemaFiles.each {
                    spec.args it.name
                }

                spec.args "-d", getDestDir()

                if (ext.packageDir) {
                    spec.args "--package-dir"
                }
                if (ext.packageName) {
                    spec.args "-p", ext.packageName
                }
                ext.packages?.each {
                    spec.args "--package:${it.key}=${it.value}"
                }
                if (ext.classPrefix) {
                    spec.args "--class-prefix", ext.classPrefix
                }
                if (ext.paramPrefix) {
                    spec.args "--param-prefix", ext.paramPrefix
                }
                if (ext.wrapContents) {
                    spec.args "--wrap-contents", ext.wrapContents
                }
                if (ext.contentsLimit) {
                    spec.args "--contents-limit", ext.contentsLimit
                }
                if (ext.chunkSize) {
                    spec.args "--chunk-size", ext.chunkSize
                }
                if (ext.protocolFile) {
                    spec.args "--protocol-file", ext.protocolFile
                }
                if (ext.protocolPackage) {
                    spec.args "--protocol-package", ext.protocolPackage
                }
                if (ext.prependFamily) {
                    spec.args "--prepend-family"
                }
                if (ext.laxAny) {
                    spec.args "--lax-any"
                }
                if (ext.blocking) {
                    spec.args "--blocking"
                }
                if (ext.dispatchVersion) {
                    spec.args "--dispatch-version", ext.dispatchVersion
                }
                if (ext.attributePrefix) {
                    spec.args "--attribute-prefix", ext.attributePrefix
                }
                if (ext.namedAttributes) {
                    spec.args "--named-attributes"
                }
                if (ext.autoPackages) {
                    spec.args "--auto-packages"
                }
                if (ext.mutable) {
                    spec.args "--mutable"
                }
                if (ext.visitor) {
                    spec.args "--visitor"
                }
                if (ext.noVarargs) {
                    spec.args "--no-varargs"
                }
                if (ext.ignoreUnknown) {
                    spec.args "--ignore-unknown"
                }
                if (ext.dispatchAs) {
                    spec.args "--dispatch-as"
                }
                if (ext.noDispatchClient) {
                    spec.args "--no-dispatch-client"
                }
                if (ext.noRuntime) {
                    spec.args "--no-runtime"
                }
                if (ext.verbose) {
                    spec.args "--verbose"
                }
            }
        } else {
            project.logger.warn("No schema files found at ${getSrcDir()} to generate scala classes from!")
        }
    }
}
