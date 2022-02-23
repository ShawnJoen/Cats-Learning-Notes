package com.chapter01.ex3

import cats.syntax.show._

object Test3 {
  def main(args: Array[String]): Unit = {
    import CatShowInstances._

    println("Hello world!".show) // (Hello world! is String!)
    println(888.show) // (888 is Int!)
    println(999L.show) // 999 (未自定义 Instance
    println(Cat("Tom", 25, "White").show) // (Tom is String!) is a (25 is Int!) year-old (White is String!) cat.
  }
}