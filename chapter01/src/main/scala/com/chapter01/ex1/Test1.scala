package com.chapter01.ex1

object Test1 {
  def main(args: Array[String]): Unit = {
    // 导入指定 Type class Instances
    import JsonWriterInstances._
    // Objects方式, 调用 Type class interface. 使用时, 只需导入 Instances
    println(Json.toJson(Person("Dave", "dave@example.com")))
    // 编译器会自动, 将指定隐式值匹配到隐式参数中
    // println(Json.toJson(Person("Dave", "dave@example.com"))(personWriter))
    // JsObject(Map(name -> JsString(Dave), email -> JsString(dave@example.com)))

    // Syntax方式, 调用 Type class interface. 使用时, 需导入 Instances和 Interface Syntax
    import JsonSyntax._
    println(Person("Shawn", "Shawn@example.com").toJson)
    // 同样, 编译器会自动, 将指定隐式值匹配到隐式参数中
    // println(Person("Dave", "dave@example.com").toJson(personWriter))
    // JsObject(Map(name -> JsString(Shawn), email -> JsString(Shawn@example.com)))

    // 可通过 implicitly, 调用 implicit scope内的任意值, 而只需指定类型
    println(implicitly[JsonWriter[String]])
    // com.cats.ex1.JsonWriterInstances$$anon$1@1b40d5f0

    // 基于 A的 instance来构造 Option[A]的 instance
    println(Json.toJson(Option("A string"))) // JsString(A string)
    // 编译器会按以下顺序匹配对应的隐式值(type class instance)
    println(Json.toJson(Option("A string"))(optionWriter[String])) // JsString(A string)
    println(Json.toJson(Option("A string"))(optionWriter(stringWriter))) // JsString(A string)

    println(Json.toJson(Option(Person("Mia", "Mia@example.com"))))
    // JsObject(Map(name -> JsString(Mia), email -> JsString(Mia@example.com)))
  }
}
