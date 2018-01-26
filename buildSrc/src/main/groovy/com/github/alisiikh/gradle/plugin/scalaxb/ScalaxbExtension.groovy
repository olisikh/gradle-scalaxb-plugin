package com.github.alisiikh.gradle.plugin.scalaxb

import groovy.transform.ToString
import org.gradle.api.Project

@ToString(excludes = 'project', includeNames = true, includePackage = false)
class ScalaxbExtension {
    File xsdDir
    File destDir
    String packageName

    private Project project

    ScalaxbExtension(Project project) {
        this.project = project
    }
}
