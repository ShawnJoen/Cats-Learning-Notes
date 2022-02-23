package com.chapter01.ex4

import cats.Eq
import cats.syntax.eq._

final case class Cat(name: String, age: Int, color: String)

// 给 Cats自带的 Eq type class, 实现自定义 Instances
object CatEqInstances {
  implicit val catEq: Eq[Option[Cat]] = Eq.instance[Option[Cat]] {
    (catA, catB) => {
      if (catA == None || catB == None) {
        if (catA == catB) true else false
      } else {
        catA.get.name === catB.get.name && catA.get.age === catB.get.age && catA.get.color === catB.get.color
      }
    }
  }
}