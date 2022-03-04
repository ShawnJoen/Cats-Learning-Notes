package com.chapter02.ex4

import cats.syntax.semigroup._

object Test4 {
  def main(args: Array[String]): Unit = {
    import cats.instances.set._
    println(Set(1, 2, 3) |+| Set(1, 2, 4)) // Set(1, 2, 3, 4)

    import cats.instances.map._
    println(Map("a" → 1) |+| Map("b" → 2)) // Map(b -> 2, a -> 1)

    import cats.instances.string._
    println("Hello" |+| " world!") // Hello world!

    import cats.instances.int._
    println(1 |+| 2) // 3
  }
}
