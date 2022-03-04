package com.chapter02.ex2

import com.chapter02.ex1.CustomMonoid._

object Test1 {
  def main(args: Array[String]): Unit = {
    // union(取并集), 可以定义为 Monoid, 单位元为空集合
    // 使用 def定义 unionMonoid, 而非 val, 原因是 def定义可以接受类型参数
    implicit def unionMonoid[A]: Monoid[Set[A]] = new Monoid[Set[A]] {
        override def empty: Set[A] = Set.empty[A]
        override def combine(x: Set[A], y: Set[A]): Set[A] = x union y
      }
    println(identityLaw(Set(1, 2, 3))(unionMonoid)) // true
    println(identityLaw(Set(9))) // true
    println(associativeLaw(Set(1, 2, 3), Set(4, 5), Set(6))(unionMonoid)) // true
    println(associativeLaw(Set(9), Set(10, 20), Set(10))) // true
    println(unionMonoid.combine(Set(10, 20), Set(10, 20))) // Set(10, 20)
    println(unionMonoid.combine(Set(), Set(10, 20))) // Set(10, 20)
    println(unionMonoid.combine(Set(20), Set(10, 20))) // Set(20, 10)
  }
}
