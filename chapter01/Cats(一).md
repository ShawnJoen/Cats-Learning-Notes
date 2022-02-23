# Type class
- Type class模式主要由3个部分组成:
1. Type class
2. Type class Instances
3. Type class interface

## Type class(接口)
- Type class可以看成一个接口, 用于定义功能
- 在 Cats中, Type class相当于至少带有一个类型参数的 trait. 比如, 以下定义将一个值转换 Json的行为:
```

sealed trait Json

final case class JsObject(get: Map[String, Json]) extends Json

final case class JsString(get: String) extends Json

final case class JsNumber(get: Double) extends Json

case object JsNull extends Json

// 定义 Type class
trait JsonWriter[A] {
  def write(value: A): Json
}

```

## Type Class Instances(隐式值)
- Type Class Instances是 Type Class的各种类型的隐式实现(自定义类型& Scala的基本类型). 也就是隐式值组合
> - Type Class Instances通常写在以下四个地方:
> 1. 一个单独的 object中, 如下面的 JsonWriterInstances
> 2. 一个单独的 trait中
> 3. Type class的伴生对象中
> 4. Type class的泛型, 所指的类型的伴生对象中, 如 JsonWriter[A]的泛型 A的伴生对象中
```

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

```

## Type Class Interface(带隐式参数的函数)
- Type Class Interface是接收 Type Class Instances(隐式值)的, 带隐式参数的函数. 也就是对外部暴露的功能
> - 通常创建 interface有两种方式:
> 1. Interface Objects
> 2. Interface Syntax(此种方式, 在 Cats中称作 syntax
```

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

```

### 使用 Type Class
- 编译器时, 会在 implicit 作用域内, 自动寻找对应类型的 type class instances
```

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

```
`*如果使用 Interface Syntax方式, 则需额外导入. 如 import JsonSyntax._`
`*如果 type class instances放到 Type class的伴生对象中, 则无需单独导入 instances`

### 练习: 实现一个Printable
- Scala对每个类型实现了 toString方法. 但该方式有缺陷, 不能对类型进行特定的实现
- 为解决以上问题, 声明 Printable type class, 并对各个类型自定义处理
```
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

```

# Meet Cats
> - 导入 Cats:
> import cats._  导入 Cats中所有的 type class
> import cats.instances.all._ 导入 Cats中所有的 instances
> import cats.syntax.all._ 导入 Cats中所有的 syntax
> import cats.implicits._  导入 Cats中所有的 instances和 syntax
>
> - Cats的默认 Instances: https://typelevel.org/cats/api/cats/instances/
> import cats.instances.int提供所有 Int的 instances
> import cats.instances.long提供所有 Long的 instances
> import cats.instances.string提供所有 Stirng的 instances
> import cats.instances.list提供所有 List的 instances
> import cats.instances.option提供所有 Option的 instances
> import cats.instances.all提供 Cats中的所有 instances

## cats.Show(type class)
- Show是 Cats自带的的 type class: http://typelevel.org/cats/api/cats/Show.html
- Show的功能和上面自定义的 type class Printable基本一致. 它的主要功能就是将数据以更友好的方式输出到控制台, 而不是通过 toString方法
- 通过 Show重写 Printable例子
```
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


package com.chapter01.ex3

import cats.syntax.show._

object Test3 {
  def main(args: Array[String]): Unit = {
    import CatShowInstances._

    println("Hello world!".show) // (Hello world! is String!)
    println(888.show) // (888 is Int!)
    println(999L.show) // 999 (未自定义 Instance
    println(Cat("Tom", 25, "White").show) // (Tom is String!) is a (25 is Int!) year-old (White is String!) cat.
  }
}

```

## cats.Eq(type class)
- Eq是 Cats自带的的 type class, 用于判断类型安全比较
- Scala内置的`==`操作符的问题:
```

以下代码中, filter里面的判断逻辑会一直返回 false, 因为 Int和 Option[Int]是不可能相等的, 应该用 Some(1)比较 item的, 而不是1
当然这是开发者犯的错. 然而技术上来讲, 语法上是没有错的, 因为`==`可以用于判断任意的两个对象, 而不用关心具体的类型
List(1, 2, 3).map(Option(_)).filter(item => item == 1)

```
`*cats.Eq可以解决以上问题, 通过 cats.Eq判断的两个值, 必须为相同类型`

- 比较 Int类型: 不同于`==`操作符, 比较不同类型的对象时, 编译会报错
```
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

```
- 使用 syntax语法(import cats.syntax.eq._):
```
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

```
- 比较 Option类型:
```
package com.chapter01.ex4

import cats.syntax.eq._

object Test6 {
  def main(args: Array[String]): Unit = {
    // println(Some(1) === None) 报错 value === is not a member of Some[Int]
    println((Some(1) : Option[Int]) === (None : Option[Int])) // false
    println(Option(1) === Option.empty[Int]) // false
    println(Option(1) =!= Option.empty[Int]) // true

    /**
     * 使用 cats.syntax.option
     * for some and none
     */
    import cats.syntax.option._
    println(1.some === none[Int]) // false
    println(1.some =!= none[Int]) // true
  }
}

```
- 比较自定义类型: 为自定义的类型创建一个 Eq的 instance
```
package com.chapter01.ex4

import java.util.Date
import java.util.concurrent.TimeUnit
import cats.Eq
import cats.syntax.eq._

object Test7 {
  def main(args: Array[String]): Unit = {
    /**
     * def instance[A](f: (A, A) => Boolean): Eq[A] = new Eq[A] {
     *    def eqv(x: A, y: A) = f(x, y)
     * }
     */
    implicit val dateEq: Eq[Date] = Eq.instance[Date] {
      (date1, date2) => date1.getTime === date2.getTime
    }

    val x = new Date() // now
    val y = new Date() // a bit later than now
    println(x === x) // true
    println(x === y) // true sometimes false
    TimeUnit.SECONDS.sleep(1)
    val z = new Date()
    println(z === y) // false
  }
}

```
- Cat类型实现自定义 Eq的 instance:
```
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


package com.chapter01.ex4

import cats.syntax.eq._

object Test8 {
  def main(args: Array[String]): Unit = {
    import CatEqInstances._

    val cat1 = Cat("Shawn", 35, "Blue")
    val cat2 = Cat("Mia", 30, "RED")
    println(Option(cat1) === Option(cat2)) // false
    val cat3 = Cat("Shawn", 35, "Blue")
    println(Option(cat1) === Option(cat3)) // true
    // println(Option(cat1) === Option(1)) // 类型不同报错
    println(Option(cat1) === Option.empty[Cat]) // false
    println(Option(cat1) =!= Option.empty[Cat]) // true
    println(Option.empty[Cat] === Option.empty[Cat]) // true
  }
}

```

# Controlling Instance Selection(控制 Instance的选择)
- Type class泛型设置协/逆变(Covariant/Contravariant), 对 type class instances(编译器自动选择), 也是有效果的
> - 协变: 若 B是 A的子类, 则 F[B]也是 F[A]的子类
>>  协变通过 `+` 符号表示: trait F[+A]
> - 逆变: 若 B是 A的子类, 则 F[A]是 F[B]的子类
>>  逆变通过 `-` 符号表示: trait F[-A]
```
package com.chapter01.ex5

sealed trait Json

final case class JsObject(get: Map[String, Json]) extends Json

final case class JsString(get: String) extends Json

final case class JsNumber(get: Double) extends Json

case object JsNull extends Json


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

```
