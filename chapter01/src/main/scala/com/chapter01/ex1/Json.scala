package com.chapter01.ex1

sealed trait Json

final case class JsObject(get: Map[String, Json]) extends Json

final case class JsString(get: String) extends Json

final case class JsNumber(get: Double) extends Json

case object JsNull extends Json

// 定义 Type class
trait JsonWriter[A] {
  def write(value: A): Json
}

final case class Person(name: String, email: String)

// 按对应类型实现 Type class Instances(隐式值)(Instances名称可以和 Type class名称相同, 如果相同, 则无需额外引入 Instances伴生对象
object JsonWriterInstances {
  // String类型的 Type class Instance
  implicit val stringWriter: JsonWriter[String] = (value: String) => JsString(value)

  // Person类型的 Type class Instance
  implicit val personWriter: JsonWriter[Person] = new JsonWriter[Person] {
    def write(value: Person): Json = JsObject(Map(
      "name" -> JsString(value.name),
      "email" -> JsString(value.email)
    ))
  }

  // 基于 A的 instance来构造 Option[A]的 instance
  implicit def optionWriter[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] = (option: Option[A]) => option match {
    case Some(aValue) => writer.write(aValue)
    case None => JsNull
  }
}

/**
 * 通常有两种方式创建 interface:
 *
 * 1. Interface Objects
 * 2. Interface Syntax(此种方式, 在 Cats中称作 syntax
 */

// Objects方式, 定义 Type class interface
object Json {
  // 对外部暴露的功能(带隐式参数的函数
  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json = w.write(value)
}

// Syntax方式
object JsonSyntax {
  // 隐式类
  implicit class JsonWriterOps[A](value: A) {
    // 对外部暴露的功能(带隐式参数的函数
    def toJson(implicit w: JsonWriter[A]): Json = w.write(value)
  }
}