package com.chapter01.ex4

object Test5 {
  def main(args: Array[String]): Unit = {
    // 使用 syntax方式
    import cats.syntax.eq._
    /**
     * === 比较两个对象相等
     * =!= 比较两个对象不相等
     */
    println(123 === 123) // true
    println(123 === 234) // false
    println(123 =!= 234) // true
    // println(123 === "123") // 类型比较错误: 不同类型无法比较
  }
}