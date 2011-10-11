package org.tnmx.tarski

/**
 * A validation error returns a String error message.
 */
case class ValidationError(errorMessage: String)

/**
 * A ValidationRule consists of a boolean expression specified in the `validate` method,
 * and an `onError` method, which returns a particular ValidationError if the validate method returns false.
 */
trait ValidationRule {
  def validate: Boolean

  def onError: ValidationError
}

trait Validations {
  /**
   * A seq of type `ValidationRule`. Each item represents a particular validation to be applied.
   */
  val validations: Seq[ValidationRule]

  /**
   * Returns true if all validation rules evaluate to true, false otherwise.
   */
  def isValid: Boolean = {
    validations.forall(_.validate == true)
  }

  /**
   * Return a set of ValidationError's, if there are any
   */
  def getErrors: Set[ValidationError] = {
    validations.filter(_.validate == false).map(_.onError).toSet
  }
}
