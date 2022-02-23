package com.chapter01.ex5

object Test1 {
  // 定义 Type class
  trait JsonWriter[A] {
    def write(value: A): Json
  }

  sealed trait Shape
  case class Circle(radius: Double) extends Shape

  def main(args: Array[String]): Unit = {
    // type class instance(隐式值
    implicit val shapeWriter: JsonWriter[Shape] = _ => JsString("a shape")
    implicit val circleWriter: JsonWriter[Circle] = circle => JsObject(
      Map("name" -> JsString("circle"), "radius = " -> JsNumber(circle.radius))
    )
    // type class interface(函数带隐式参数
    def format[A](value: A)(implicit w: JsonWriter[A]): Json = w.write(value)

    val shape: Shape = Circle(10.11)
    println(format(shape)) // JsString(a shape)

    val circle: Circle = Circle(20.99)
    println(format(circle)) // JsObject(Map(name -> JsString(circle), radius =  -> JsNumber(20.99)))
  }
}