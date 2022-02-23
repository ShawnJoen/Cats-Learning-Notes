package com.chapter01.ex4

import cats.syntax.eq._

object Test6 {
  def main(args: Array[String]): Unit = {
    // println(Some(1) === None) 报错 value === is not a member of Some[Int]
    println((Some(1) : Option[Int]) === (None : Option[Int])) // false
    println(Option(1) === Option.empty[Int]) // false
    println(Option(1) =!= Option.empty[Int]) // true

    /**
     * 使用 cats.syntax.option
     * for some and none
     */
    import cats.syntax.option._
    println(1.some === none[Int]) // false
    println(1.some =!= none[Int]) // true
  }
}