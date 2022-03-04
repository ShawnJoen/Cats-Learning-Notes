package com.chapter02.ex4

import cats.Monoid
import cats.syntax.semigroup._
import cats.instances.option._

object Test2 {
  def main(args: Array[String]): Unit = {
    /**
     * 需求 add函数, 需兼容 Int和 Option[Int]类型入参, 此时只能使用 Monoid来实现
     */
    def add1[A](items: List[A])(implicit m: Monoid[A]): A = items.foldLeft(m.empty)(_ |+| _)
    println(add1(List(10, 20, 30, 40, 50, 60))) // 210
    println(add1(List(Option(10), Option(20), Option(30), Option(40), Option(50), Option(60)))) // Some(210)

    /**
     * 可以将上面的函数简化: 通过 Scala的 context bound:
     *
     * 1. 去掉第二个参数列表 (implicit m: Monoid[A]),
     * 2. 将 def add1[A]改写为 def add2[A: Monoid], 这里的 Monoid是描述 implicit值, 是通过伴生对象 Monoid.apply[A]来获取 Monoid的 instance的
     */
    def add2[A: Monoid](items: List[A]): A = items.foldLeft(Monoid[A].empty)(_ |+| _)
    println(add2(List(10, 20, 30, 40, 50, 60))) // 210
    println(add2(List(Option(10), Option(20), Option(30), Option(40), Option(50), Option(60)))) // Some(210)
  }
}
