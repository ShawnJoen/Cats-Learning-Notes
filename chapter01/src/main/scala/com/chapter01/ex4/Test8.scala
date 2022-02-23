package com.chapter01.ex4

import cats.syntax.eq._

object Test8 {
  def main(args: Array[String]): Unit = {
    import CatEqInstances._

    val cat1 = Cat("Shawn", 35, "Blue")
    val cat2 = Cat("Mia", 30, "RED")
    println(Option(cat1) === Option(cat2)) // false
    val cat3 = Cat("Shawn", 35, "Blue")
    println(Option(cat1) === Option(cat3)) // true
    // println(Option(cat1) === Option(1)) // 类型不同报错
    println(Option(cat1) === Option.empty[Cat]) // false
    println(Option(cat1) =!= Option.empty[Cat]) // true
    println(Option.empty[Cat] === Option.empty[Cat]) // true
  }
}