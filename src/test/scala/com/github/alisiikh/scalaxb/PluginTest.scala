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
