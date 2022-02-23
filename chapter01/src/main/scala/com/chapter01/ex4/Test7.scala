package com.chapter01.ex4

import java.util.Date
import java.util.concurrent.TimeUnit
import cats.Eq
import cats.syntax.eq._

object Test7 {
  def main(args: Array[String]): Unit = {
    /**
     * def instance[A](f: (A, A) => Boolean): Eq[A] = new Eq[A] {
     *    def eqv(x: A, y: A) = f(x, y)
     * }
     */
    implicit val dateEq: Eq[Date] = Eq.instance[Date] {
      (date1, date2) => date1.getTime === date2.getTime
    }

    val x = new Date() // now
    val y = new Date() // a bit later than now
    println(x === x) // true
    println(x === y) // true sometimes false
    TimeUnit.SECONDS.sleep(1)
    val z = new Date()
    println(z === y) // false
  }
}