package com.chapter02.ex3

import cats.Monoid
import cats.instances.int._
import cats.instances.option._

object Test4 {
  def main(args: Array[String]): Unit = {
    val a = Option(1)
    val b = Option(2)
    println(Monoid[Option[Int]].combine(a, b)) // Some(3)
  }
}
