tarski
========

What is It?
------------

**tarski** makes it easy to add arbitrary **validations** to any Scala class, without anything fancy,
enabling you to validate user input or web service responses at runtime.

**tarski** also provides meaningful, application-defined information about what validation rules failed,
so your application can act accordingly.


Setup
------------

First, include **tarski** as a project dependency. The current release is for Scala 2.9.1.

If you're using Maven:

```xml
<repositories>
  <repository>
    <id>tnm-releases</id>
    <url>https://github.com/tnm/tnm-mvn-repo/raw/master/releases/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>org.tnmx</groupId>
    <artifactId>tarski_2.9.1</artifactId>
    <version>0.1.1</version>
  </dependency>
</dependencies>
```

Or SBT 0.10:

```scala

resolvers += "tnm Repo" at "https://github.com/tnm/tnm-mvn-repo/raw/master/releases/"

libraryDependencies += "org.tnmx"  %% "tarski"  % "0.1.1"

```

(I'll put it on Central soon).

How to Use
------------

**tarski** provides a base trait, `Validation`. In order to use, all you need to do is mix in that trait to your
class, and define `validations`. `validations` is a `Seq` of type `ValidationRule`, explained in more detail below.

Instances of your class will then have a `isValid` method and a `getErrors` method.

If all the validation rules "pass", `isValid` will return `true`.

If any validation rule "fails", `isValid` will return `false`.

If there are errors, you can call `getErrors`, which will return a set of type `ValidationError`, with string
representations of what tests failed. This type is explained in more detail below.


ValidationRule
---------------

In your application, you define **validation rules**. A validation rule is just a case class that extends `ValidationRule`
and implements two methods: `validate` and `onError`.

`validate` is any Boolean expression that you wish to test. `onError` is a `ValidationError` you wish to return in case that
test fails.


ValidationError
----------------

A `ValidationError` is a case class with a `String` parameter (such as, "Name was not Tom"). This message
can be directly passed on to a front-end web service, or you can use application logic to handle different sorts
of validation error responses.


An Example
-----------

```scala
import org.tnmx.tarski._
```

Define some validation rules.

```scala
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

```

Write a case class that extends `Validations`.

```scala
case class Person(name: String, age: Int, friends: List[String]) extends Validations {
  val validations = Seq(NameRule(name), AgeRule(age), FriendsRule(friends))
}
```

Create some objects. In practice, this data will almost certainly come from runtime input, like web forms.


```scala
val tom = Person("Tom", 28, List("Jess", "Ben", "Jessica"))
val jess = Person("Jess", 28, List("Ben"))
```

And now call some methods:

```scala
tom.isValid
  > true

jess.isValid
  > false

jess.getErrors
  > Set[ValidationError](ValidationError("Name was not Tom."),
                         ValidationError("Friends list was not equal to 3."))
```


A Common Use Case
------------------

One nice use case for **tarski** is for validating user input at runtime, and then acting accordingly. For example, you might
be using [Jerkson](https://github.com/codahale/jerkson) to parse JSON data for a web service, and inserting data from a parsed 
case class into a database. If you want to provide application-level validation for instances of those case classes, you can simply
mix `Validation` into your case class (like above) and define predicates that must be met. Then just call `isValid` before doing
anything more.

Author & MIT License
---------------------

Copyright (c) Ted Nyman

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.




