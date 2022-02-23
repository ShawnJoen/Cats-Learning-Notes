package com.chapter01.ex2

object Test2 {
  def main(args: Array[String]): Unit = {
    import PrintableInstances._
    // Type class interface Objects方式
    println(Printable.format("Hello world!")) // Hello world!
    Printable.print("Hello world!") // Hello world!
    Printable.print(Cat("Mia", 30, "RED")) // Mia is a 30 year-old RED cat.

    // Type class interface Syntax方式
    import PrintableSyntax._
    // println("Hello world! by syntax.".format)
    "Hello world! by syntax.".print // Hello world! by syntax.
    Cat("Shawn", 35, "Blue").print // Shawn is a 35 year-old Blue cat.
  }
}
