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

import groovy.transform.ToString

@ToString(excludes = 'project', includeNames = true, includePackage = false)
class ScalaxbExtension {
    // TODO: have a look into
    // https://github.com/eed3si9n/scalaxb/blob/master/cli/src/main/scala/scalaxb/compiler/Main.scala
    // it contains more available parameters
    File srcDir
    File destDir
    Boolean packageDir = true
    String packageName
    Map<String, String> packages
    Boolean autoPackages
    String classPrefix
    String paramPrefix
    String attributePrefix
    Boolean prependFamily
    String wrapContents
    String protocolFile
    String protocolPackage
    Long contentsLimit
    Long chunkSize
    Boolean withRuntime
    Boolean laxAny
    Boolean blocking
    Boolean verbose
    String dispatchVersion
}
