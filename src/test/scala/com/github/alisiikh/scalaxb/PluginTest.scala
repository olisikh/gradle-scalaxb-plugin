/**
 * Copyright [2018] [Oleksii Lisikh <alisiikh@gmail.com>]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.alisiikh.scalaxb

import com.github.alisiikh.generated._
import org.scalatest.{FreeSpecLike, Matchers}

import scala.xml._

class PluginTest extends Matchers with FreeSpecLike {

  val simpleXml = getClass.getResourceAsStream("/sample.xml")

  "Scalaxb plugin" - {
    "generates scala classes using xsd schema" in {
      val user = scalaxb.fromXML[User](XML.load(simpleXml))

      user.username shouldBe "john"
      user.password shouldBe "s3cret!"
    }

    "generates XML from an object" in {
      val user = User(username = "foo", password = "bar")

      val userXml = scalaxb.toXML[User](user, "User", defaultScope)

      userXml shouldBe <User xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><username>foo</username><password>bar</password></User>
    }
  }

}
