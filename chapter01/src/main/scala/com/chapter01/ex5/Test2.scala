package com.chapter01.ex5

object Test2 {
  // 定义 Type class(逆变
  trait JsonWriter[-A] {
    def write(value: A): Json
  }

  sealed trait Shape
  case class Circle(radius: Double) extends Shape

  def main(args: Array[String]): Unit = {
    // type class instance(隐式值
    implicit val circleWriter: JsonWriter[Circle] = circle => JsObject(
      Map("name" -> JsString("circle"), "radius = " -> JsNumber(circle.radius))
    )
    implicit val shapeWriter: JsonWriter[Shape] = _ => JsString("a shape")

    // type class interface(函数带隐式参数
    def format[A](value: A)(implicit w: JsonWriter[A]): Json = w.write(value)

    val shape: Shape = Circle(10.11)
    println(format(shape)) // JsString(a shape)
    // println(format(shape)(circleWriter)) 匹配 JsonWriter[Circle]会报错, 因为是逆变, 只能匹配第一个参数 shape的类型 Shape或它的父级
    println(format(shape)(shapeWriter)) // JsString(a shape)

    val circle: Circle = Circle(20.99)
    // 默认自动选择父级 instance
    println(format(circle)) // JsString(a shape)
    // 能匹配第一个参数 circle的类型 Circle或它的父级
    println(format(circle)(circleWriter)) // JsObject(Map(name -> JsString(circle), radius =  -> JsNumber(20.99)))
    println(format(circle)(shapeWriter)) // JsString(a shape)
  }
}