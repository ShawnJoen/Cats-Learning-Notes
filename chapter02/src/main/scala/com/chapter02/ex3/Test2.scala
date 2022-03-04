package com.chapter02.ex3

import cats.Monoid
import cats.instances.string._

object Test2 {
  def main(args: Array[String]): Unit = {
    println(Monoid[String].combine("hello", "world!")) // helloworld!
    println(Monoid[String].empty)
    // 下面的例子与上面的完全等价
    println(Monoid.apply[String].combine("hello", "world!")) // helloworld!
    println(Monoid.apply[String].empty)
  }
}
