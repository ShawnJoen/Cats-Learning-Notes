package com.chapter02.ex4

import cats.Monoid
import cats.syntax.semigroup._

object Test3 {
  // 自定义样例类
  case class Order(totalCost: Double, quantity: Double)
  object Order {
    // Order单例对象中定义隐式值
    implicit val orderMonoid = new Monoid[Order] {
        override def empty: Order = Order(0, 0)
        override def combine(x: Order, y: Order): Order = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
      }
  }

  def add[A: Monoid](items: List[A]): A = items.foldLeft(Monoid[A].empty)(_ |+| _)

  def main(args: Array[String]): Unit = {
    /*实现对 Order类型的累加:
     * 1. 依赖 Monoid实例实现累加的操作, 所以不管什么类型, 只要提供该类型的 Monoid实例
     * 2. 将 orderMonoid 放在 Order 伴生对象中, 省去 import的麻烦
     */
    println(add(List(Order(1.0, 2.0), Order(3.0, 4.0), Order(2.0, 3.0)))) // Order(6.0,9.0)
  }
}
