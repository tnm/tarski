package org.tnmx.tarski.tests

import com.simple.simplespec.Spec
import org.tnmx.tarski._
import org.junit.Test

// Define some validation rules
case class NameRule(name: String) extends ValidationRule {
  def validate = (name == "Tom")
  def onError = ValidationError("Name was not Tom.")
}

case class AgeRule(age: Int) extends ValidationRule {
  def validate = (age >= 25)
  def onError = ValidationError("Age was not greater than or equal to 25.")
}

case class FriendsRule(friends: List[String]) extends ValidationRule {
  def validate = (friends.size == 3)
  def onError = ValidationError("Friends list was not equal to 3.")
}

case class WordSizeRule(word: String) extends ValidationRule {
  def validate = (word.length() > 7)
  def onError = ValidationError("100 Word Too Short") // i.e., an application-specific error code
}

// Examples that extends Validations
case class PersonExample(name: String, age: Int, friends: List[String]) extends Validations {
  val validations = Seq(NameRule(name), AgeRule(age), FriendsRule(friends))
}

case class WordExample(word: String) extends Validations {
  val validations = Seq(WordSizeRule(word))
}

// Tests
class TarskiSpec extends Spec {
  val tomGoodExample = PersonExample("Tom", 28, List("Jess", "Ben", "Jessica"))
  val jessBadExample = PersonExample("Jess", 28, List("Tom"))
  val wordExample = WordExample("nope")

  class `A case class implementing Validations` {
    @Test def `has an isValid method, which returns true if the case class is valid` = {
      tomGoodExample.isValid.must(be(true))
    }

    @Test def `has an isValid method, which returns false if the case class is not valid` = {
      jessBadExample.isValid.must(be(false))
    }

    @Test def `has a getErrors method, which returns a set of ValidationError's` = {
      val errors = Set[ValidationError](
                          ValidationError("Name was not Tom."),
                          ValidationError("Friends list was not equal to 3."))
      jessBadExample.getErrors.must(be(errors))
    }
  }

  class `The getErrors method` {
    @Test def `returns an empty set if there are no errors` = {
      val nullErrors = Set[ValidationError]()
      tomGoodExample.getErrors.must(be(nullErrors))
    }

    @Test def `returns a single error if there is only one error` = {
      val error = Set[ValidationError](
                          ValidationError("100 Word Too Short"))
      wordExample.getErrors.must(be(error))
    }
  }

  class `A ValidationError case class` {
    @Test def `has a getMessage method, returning the error message` = {
      val anError = Set[ValidationError](ValidationError("100 Word Too Short"))
      anError.head.getMessage.must(be("100 Word Too Short"))
    }
  }
}
