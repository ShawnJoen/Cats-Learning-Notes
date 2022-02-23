package com.chapter01.ex4

import cats.Eq
import cats.instances.int._

object Test4 {
  def main(args: Array[String]): Unit = {
    val eqInt = Eq[Int]
    println(eqInt.eqv(123, 123)) // true
    println(eqInt.eqv(123, 222)) // false
    // eqInt.eqv(123, "234") // 类型比较错误: 不同类型无法比较
  }
}