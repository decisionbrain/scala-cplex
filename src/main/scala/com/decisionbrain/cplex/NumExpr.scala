/*
 * Source file provided under Apache License, Version 2.0, January 2004,
 *  http://www.apache.org/licenses/
 *  (c) Copyright DecisionBrain SAS 2016,2020
 *
 */

package com.decisionbrain.cplex

import com.decisionbrain.cplex.cp.CpModel
import ilog.concert.IloNumExpr

/**
  * Class for numeric expressions
  *
  * @param expr  is the numeric expression
  * @param modeler is the constraint programming model
  */
class NumExpr(val expr: IloNumExpr)(implicit modeler: Modeler) {

  /**
    * Returns true if the given argument is a numeric expression
    *
    * @param a is the given argument
    * @return true if the argument is an instance of class NumExpr
    */
  def canEqual(a: Any) = a.isInstanceOf[NumExpr]

  /**
    * Returns true if the given argument given is equal to this numeric expression
    *
    * @param that is the numeric expression to compare to
    * @return true if both numeric expressions are equals
    */
  override def equals(that: scala.Any): Boolean = {
    that match {
      case that: NumExpr => that.canEqual(this) && this.hashCode() == that.hashCode()
      case _ => false
    }
  }

  /**
    * Returns the hash code
    *
    * @return the hash code
    */
  override def hashCode(): Int = return getIloNumExpr().hashCode()

  /**
    * Returns the constraint programming model
    *
    * @return the constraint programming model
    */
  def getModeler(): Modeler = modeler

  /**
    * Return the CPLEX numeric expression.
    *
    * @return the numeric expression
    */
  def getIloNumExpr(): IloNumExpr = expr

  /**
    * Creates and returns an expression representing the sum of this numeric expression and another numeric
    * expression.
    *
    * @param y is the other numeric expression
    * @return a numeric expression representing the sum <em>x + y</em>
    */
  def +(y: NumExpr): NumExpr = modeler.sum(this, y)

  /**
    * Creates and returns an expression representing the sum of this numeric expression and a numeric constant.
    *
    * @param v is the numeric constant
    * @return a numeric expression representing the sum <em>x + v</em>
    */
  def +(v: Double): NumExpr = modeler.sum(this, v)

  /**
    * Creates and returns an expression representing the sum of the numeric expressions <em>x</em> and <em>that</em>
    *
    * @param y is the numeric expression to add
    * @return a numeric expression representing the sum <em>x - y</em>
    */
  def -(y: NumExpr): NumExpr = modeler.diff(this, y)

  /**
    * Creates and returns an expression representing the diff of this numeric expression and a numeric constant.
    *
    * @param v is the numeric constant
    * @return a numeric expression representing the sum <em>x - v</em>
    */
  def -(v: Double): NumExpr = modeler.diff(this, v)

  /**
    * Create and returns an expression that is the negation of this expression.
    *
    * @return the negation of this expression
    */
  def unary_- : NumExpr = modeler.negative(this)

  /**
    * Creates and returns an expression representing the product of the expression x and the value v.
    *
    * @param v is the value to use in the product.
    * @return a numeric expression representing the product <em>e1 * v</em>.
    */
  def *(v: Double): NumExpr = modeler.prod(this, v)

  /**
    * Creates and returns an expression representing the product of the expression x and another expression y.
    *
    * @param expr is the value to use in the product.
    * @return a numeric expression representing the product <em>e1 * v</em>.
    */
  def *(expr: NumExpr): NumExpr = modeler.prod(this, expr)

  /**
    * Creates and returns the division e1 / e2.
    *
    * @param expr is the divisor
    * @return an integer expression equals to the integer division e1 / e2.
    */
  def /(expr: NumExpr): NumExpr = modeler match {
    case model: CpModel => model.quot(this, expr)
    case _ => throw new UnsupportedOperationException("Operator \'/\' only supported on CpModel")
  }


  /**
    * Creates and returns an instance of IloRange that represents the constraint <em>expr >= v</em>.
    *
    * @param value is the lower bound of the new greater-than-or-equal-to constraint.
    * @return a new <em>IloRange</em> instance that represents the constraint <em>x >= value</em>.
    */
  def >=(value: Double): Range = modeler.ge(this, value)

  /**
    * Creates and returns an instance of IloConstraint that represents the constraint <em>expr1 >= expr2</em>.
    *
    * @param e is the right-hand side expression of the greater than or equal constraint
    * @return a new constraint
    */
  def >=(e: NumExpr): Constraint = modeler.ge(this, e)

  /**
    * Creates and returns an instance of IloRange that represents the constraint <em>expr <= v</em>.
    *
    * @param value is the upper bound of the new less-than-or-equal-to constraint.
    * @return a new <em>IloRange</em> instance that represents the constraint <em>x <= value</em>.
    */
  def <=(value: Double): Range = modeler.le(this, value)

  /**
    * Creates and returns an instance of IloRange that represents the constraint <em>expr1 <= expr2</em>.
    *
    * @param e is the right-handside expression of the new less-than-or-equal-to constraint.
    * @return a new <em>IloConstraint</em>
    */
  def <=(e: NumExpr): Constraint = modeler.le(this, e)

  /**
    * Creates and returns an instance of IloRange that represents the constraint <em>expr == value</em>.
    *
    * @param value is the numeric value of the new equal-to constraint.
    * @return a new <em>IloRange</em> instance that represents the constraint <em>expr == value</em>.
    */
  def ==(value: Double): Range = modeler.eq(this, value)

  /**
    * Creates and returns an instance of IloRange that represents the constraint <em>expr == value</em>.
    *
    * @param value is the integer value of the new equal-to constraint.
    * @return a new <em>IloRange</em> instance that represents the constraint <em>expr == value</em>.
    */
  def ==(value: Int): Range = modeler.eq(this, value)

  /**
    * Creates and returns an instance of IloRange that represents the constraint <em>expr1 == expr2</em>.
    *
    * @param e is the numeric expression of the new equal-to constraint.
    * @return a new <em>IloConstraint</em> instance that represents the constraint <em>expr1 == expr2</em>.
    */
  def ==(e: NumExpr): Constraint = modeler.eq(this, e)

  /**
    * Return a character string that represents the numeric expression.
    *
    * @return a character string
    */
  override def toString: String = expr.toString()
}

object NumExpr {
  /**
    * Converts a CPLEX numeric expression to a numeric expression.
    *
    * @param e is the CPLEX numeric expression
    * @param modeler is the optimization model the numeric expression belongs to
    * @return a numeric expression
    */
  def apply(e: IloNumExpr)(implicit modeler: Modeler) = new NumExpr(e)

  /**
    * Converts a constant value to a numeric expression.
    *
    * @param v is the constant value
    * @param modeler is the optimization model
    * @return a numeric expression
    */
  def apply(v: Double)(implicit modeler: Modeler) = modeler.linearNumExpr(v)

  /**
    * Implicit Conversion of an object of type Double to a NumConstant i.e. a constant numeric expression.
    *
    * @param value is the value of the constant numeric expression
    * @param modeler is the constraint programming model
    */
  implicit class LinearNumExpr(value: Double)(implicit modeler: Modeler)
    extends NumExpr(modeler.getIloModeler().linearNumExpr(value)) {
  }

}