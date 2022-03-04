package com.chapter02.ex3

import cats.Semigroup
import cats.instances.string._

object Test3 {
  def main(args: Array[String]): Unit = {
    println(Semigroup.apply[String].combine("hello", "world!")) // helloworld!
  }
}
