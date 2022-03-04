package com.chapter02.ex1

object CustomMonoid {
  // 判断是否满足结合律法则:
  def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean =
    m.combine(m.combine(x, y), z) == m.combine(x, m.combine(y, z))

  // 判断是否满足单位元法则:
  def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean =
    m.combine(x, m.empty) == x && m.combine(m.empty, x) == x

  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

//  object Monoid {
//    // 该方法返回 type class instance
//    def apply[A](implicit m: Monoid[A]): Monoid[A] = m
//  }
}
