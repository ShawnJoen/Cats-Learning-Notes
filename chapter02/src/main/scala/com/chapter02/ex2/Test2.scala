package com.chapter02.ex2

object Test2 {
  // 判断是否满足结合律法则:
  def associativeLaw[A](x: A, y: A, z: A)(implicit m: Semigroup[A]): Boolean =
    m.combine(m.combine(x, y), z) == m.combine(x, m.combine(y, z))

  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  def main(args: Array[String]): Unit = {
    // intersect取交集只能定义为 Semigroup, 因为无法为它找到有意义的单位元
    implicit def insersectSemigroup[A]: Semigroup[Set[A]] = (x, y) => x intersect y
    println(associativeLaw(Set(1, 2, 3), Set(4, 5), Set(6))(insersectSemigroup)) // true
    println(associativeLaw(Set(9), Set(10, 20), Set(10))) // true
    println(insersectSemigroup.combine(Set(10, 20), Set(10, 20))) // Set(10, 20)
    println(insersectSemigroup.combine(Set(), Set(10, 20))) // Set()
    println(insersectSemigroup.combine(Set(20), Set(10, 20))) // Set(20)
  }
}
