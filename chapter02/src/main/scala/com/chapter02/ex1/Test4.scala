package com.chapter02.ex1

object Test4 {
  def main(args: Array[String]): Unit = {
    import CustomMonoid._

    // Boolean的 exclusive nor可以定义一个 monoid, 单位元为 true
    implicit val norMonoid: Monoid[Boolean] = new Monoid[Boolean] {
        override def empty: Boolean = true
        override def combine(x: Boolean, y: Boolean): Boolean = (!x || y) && (x || !y)
      }
    println("单位元 1: " + identityLaw(true)(norMonoid)) // 单位元 1: true
    println("单位元 2: " + identityLaw(false)) // 单位元 2: true
    println("true, false, true: " + associativeLaw(true, false, true)(norMonoid)) // true
    println("false, false, true: " + associativeLaw(false, false, true)) // true
    println("true, false, false: " + associativeLaw(true, false, false)) // true
    println("false, false, false: " + associativeLaw(false, false, false)) // true
    println("true, true, true: " + associativeLaw(true, true, true)) // true
    println(norMonoid.combine(true, false)) // false
    println(norMonoid.combine(false, true)) // false
    println(norMonoid.combine(false, false)) // true
    println(norMonoid.combine(true, true)) // true
  }
}
