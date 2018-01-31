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

                println(ext)

                args xsdFile.absolutePath
                args "-p", ext.packageName
                args "-d", ext.destDir
                args "--package-dir"
            }
        }
    }
}
