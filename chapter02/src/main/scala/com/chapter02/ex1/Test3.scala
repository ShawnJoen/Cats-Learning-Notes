package com.chapter02.ex1

object Test3 {
  def main(args: Array[String]): Unit = {
    import CustomMonoid._

    // Boolean的 exclusive or可以定义一个 monoid, 单位元为 false
    implicit val eitherMonoid: Monoid[Boolean] = new Monoid[Boolean] {
        override def empty: Boolean = false
        override def combine(x: Boolean, y: Boolean): Boolean = (x && !y) || (!x && y)
      }
    println("单位元 1: " + identityLaw(true)(eitherMonoid)) // 单位元 1: true
    println("单位元 2: " + identityLaw(false)) // 单位元 2: true
    println("true, false, true: " + associativeLaw(true, false, true)(eitherMonoid)) // true
    println("false, false, true: " + associativeLaw(false, false, true)) // true
    println("true, false, false: " + associativeLaw(true, false, false)) // true
    println("false, false, false: " + associativeLaw(false, false, false)) // true
    println("true, true, true: " + associativeLaw(true, true, true)) // true
    println(eitherMonoid.combine(true, false)) // true
    println(eitherMonoid.combine(false, true)) // true
    println(eitherMonoid.combine(false, false)) // false
    println(eitherMonoid.combine(true, true)) // false
  }
}
