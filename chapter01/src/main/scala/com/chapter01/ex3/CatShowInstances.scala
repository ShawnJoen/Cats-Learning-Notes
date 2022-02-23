package com.chapter01.ex3

import cats.Show
import cats.syntax.show._

final case class Cat(name: String, age: Int, color: String)

// 给 Cats自带的 Show type class, 实现自定义 Instances
object CatShowInstances {
  implicit val showString: Show[String] = str => s"(${str} is String!)"

  implicit val showInt: Show[Int] = i => s"(${i} is Int!)"

  implicit val catShow: Show[Cat] = Show[Cat] {
    cat => s"${ cat.name.show} is a ${cat.age.show} year-old ${cat.color.show} cat."
  }
}