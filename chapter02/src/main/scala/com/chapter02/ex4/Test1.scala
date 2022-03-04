package com.chapter02.ex4

object Test1 {
  def main(args: Array[String]): Unit = {
    /**
     * 两种实现
     * 1. 使用 Monoid, 2. 未使用 Monoid
     */
    def add1(items: List[Int]): Int = items.foldLeft(0)(_ + _)
    println(add1(List(10, 20, 30, 40, 50, 60))) // 210

    def add2(items: List[Int]): Int = {
      import cats.Monoid
      import cats.instances.int._
      import cats.syntax.monoid._
      items.foldLeft(Monoid[Int].empty)(_ |+| _)
    }
    println(add2(List(10, 20, 30, 40, 50, 60))) // 210
  }
}
