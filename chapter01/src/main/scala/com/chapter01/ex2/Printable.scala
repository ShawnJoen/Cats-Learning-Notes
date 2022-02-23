package com.chapter01.ex2

// type class
trait Printable[A] {
  def format(a: A): String
}

final case class Cat(name: String, age: Int, color: String)

// type class instances
object PrintableInstances {
  implicit val stringPrintable: Printable[String] = (a: String) => a
  implicit val intPrintable: Printable[Int] = (a: Int) => a.toString
  implicit val catPrintable: Printable[Cat] = (cat: Cat) => {
    s"${Printable.format(cat.name)} is a ${Printable.format(cat.age)} year-old ${Printable.format(cat.color)} cat."
  }
}

// type class interface(Objects方式
object Printable {
  def format[A](a: A)(implicit printable: Printable[A]) = {
    printable.format(a)
  }

  def print[A](a: A)(implicit printable: Printable[A]) = {
    println(format(a))
  }
}

// type class interface(Syntax方式
object PrintableSyntax {
  implicit class PrintableOps[A](a: A) {
    def format(implicit printable: Printable[A]): String = {
      printable.format(a)
    }

    def print(implicit printable: Printable[A]) = {
      println(format)
    }
  }
}