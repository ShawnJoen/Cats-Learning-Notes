package com.chapter02.ex1

object Test1 {
  def main(args: Array[String]): Unit = {
    import CustomMonoid._

    // Boolean的 and可以定义一个 monoid, 其操作符 &&(逻辑运算符), 单位元为 true
    implicit val andMonoid: Monoid[Boolean] = new Monoid[Boolean] {
      override def empty: Boolean = true
      override def combine(x: Boolean, y: Boolean): Boolean = x && y
    }
    println("单位元 1: " + identityLaw(true)(andMonoid)) // 单位元 1: true
    println("单位元 2: " + identityLaw(false)) // 单位元 2: true
    println("true, false, true: " + associativeLaw(true, false, true)(andMonoid)) // true
    println("false, false, true: " + associativeLaw(false, false, true)) // true
    println("true, false, false: " + associativeLaw(true, false, false)) // true
    println("false, false, false: " + associativeLaw(false, false, false)) // true
    println("true, true, true: " + associativeLaw(true, true, true)) // true
    println(andMonoid.combine(true, false)) // false
    println(andMonoid.combine(false, true)) // false
    println(andMonoid.combine(false, false)) // false
    println(andMonoid.combine(true, true)) // true
  }
}
