package com.chapter02.ex3

import cats.instances.int._
import cats.instances.option._
import cats.syntax.semigroup._ // or cats.syntax.monoid._

object Test1 {
  def main(args: Array[String]): Unit = {
    val a = Option(1)
    val b = Option(2)
    println(a |+| b) // Some(3)
    println(a combine b) // Some(3)

    println(1 combine 2) // 3
    println(1 |+| 2) // 3

    val c = Option(Set(1,2,3))
    val d = Option(Set(3,4,5))
    println(c |+| d) // Some(Set(5, 1, 2, 3, 4))

    val e = List(1,2,3)
    val f = List(3,4,5)
    println(e combine f) // List(1, 2, 3, 3, 4, 5)
  }
}
