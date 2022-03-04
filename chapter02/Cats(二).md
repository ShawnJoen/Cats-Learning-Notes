# 单位元(Identity Law)
- 单位元又称幺元, 群的任何元素和它进行运算, 元素值不变 
> `*例 1: 整数进行加法运算时 0是单位元, 因为对任意整数 x + 0结果还是 x`
> `*例 2: 整数进行乘法运算时 1是单位元, 因为对任意整数 x * 1结果还是 x`
> `*例 3: 字符串的单位元是空字符串""`

# 结合律(Associative Law)
- 运算顺序不会对运算结果有影响
> `*例: 加法结合律: 三个数相加, 先把前两个数相加, 或先把后两个数相加, 和不变 (a+b)+c=a+(b+c)`

# Monoid(幺半群)& Semigroup(半群)
- 主要用于累加或拼接, 也就是 addition或 combination的抽象

## Monoid(type class)
- 一个针对类型 A的 monoid是:
1) 二元操作 combine, 类型为 (A, A) => A
2) 单位元 empty, 类型为 A
```

// 定义 Monoid
trait Monoid[A] {
  def combine(x: A, y: A): A
  def empty: A
}

```
- 定义 Monoid时, combine和 empty方法外, 还需要满足 2个法则:
```

// combine必须满足结合律法则:
def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean = 
	m.combine(m.combine(x, y), z) == m.combine(x, m.combine(y, z))

// empty必须满足单位元法则:
def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean =
  m.combine(x, m.empty) == x && m.combine(m.empty, x) == x

```
`*实践中, 只有在使用自定义的 Monoid instances时, 才需要考虑以上法则, 而使用 Cats的 instances时, 无需考虑`

## Semigroup(type class)
- semigroup是 monoid中, 除去 empty单位元的 combine部分
- 很多 semigroup也是 monoid, 但一些数据类型是, 没办法定义有意义的 empty单位元 例: 非空序列拼接, 正整数相加等
```

// 定义 Semigroup
trait Semigroup[A] {
  def combine(x: A, y: A): A
}

// Monoid可视为 Semigroup的子类
trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

```

- 自定义示例 1:
```
package com.chapter02.ex1

object CustomMonoid {
  // 判断是否满足结合律法则:
  def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean =
    m.combine(m.combine(x, y), z) == m.combine(x, m.combine(y, z))

  // 判断是否满足单位元法则:
  def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean =
    m.combine(x, m.empty) == x && m.combine(m.empty, x) == x

  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

  object Monoid {
    // 该方法返回 type class instance
    def apply[A](implicit m: Monoid[A]): Monoid[A] = m
  }
}


package com.chapter02.ex1

object Test1 {
  def main(args: Array[String]): Unit = {
    import CustomMonoid._

    // Boolean的 and可以定义一个 monoid, 其操作符 &&(逻辑运算符), 单位元为 true
    implicit val andMonoid: Monoid[Boolean] = new Monoid[Boolean] {
      override def empty: Boolean = true
      override def combine(x: Boolean, y: Boolean): Boolean = x && y
    }
    println("单位元 1: " + identityLaw(true)(andMonoid)) // 单位元 1: true
    println("单位元 2: " + identityLaw(false)) // 单位元 2: true
    println("true, false, true: " + associativeLaw(true, false, true)(andMonoid)) // true
    println("false, false, true: " + associativeLaw(false, false, true)) // true
    println("true, false, false: " + associativeLaw(true, false, false)) // true
    println("false, false, false: " + associativeLaw(false, false, false)) // true
    println("true, true, true: " + associativeLaw(true, true, true)) // true
    println(andMonoid.combine(true, false)) // false
    println(andMonoid.combine(false, true)) // false
    println(andMonoid.combine(false, false)) // false
    println(andMonoid.combine(true, true)) // true
  }
}


package com.chapter02.ex1

object Test2 {
  def main(args: Array[String]): Unit = {
    import CustomMonoid._

    // Boolean的 or可以定义一个 monoid, 其操作符 ||(逻辑运算符), 单位元为 false
    implicit val orMonoid: Monoid[Boolean] = new Monoid[Boolean] {
        override def empty: Boolean = false
        override def combine(x: Boolean, y: Boolean): Boolean = x || y
      }
    println("单位元 1: " + identityLaw(true)(orMonoid)) // 单位元 1: true
    println("单位元 2: " + identityLaw(false)) // 单位元 2: true
    println("true, false, true: " + associativeLaw(true, false, true)(orMonoid)) // true
    println("false, false, true: " + associativeLaw(false, false, true)) // true
    println("true, false, false: " + associativeLaw(true, false, false)) // true
    println("false, false, false: " + associativeLaw(false, false, false)) // true
    println("true, true, true: " + associativeLaw(true, true, true)) // true
    println(orMonoid.combine(true, false)) // true
    println(orMonoid.combine(false, true)) // true
    println(orMonoid.combine(false, false)) // false
    println(orMonoid.combine(true, true)) // true
  }
}


package com.chapter02.ex1

object Test3 {
  def main(args: Array[String]): Unit = {
    import CustomMonoid._

    // Boolean的 exclusive or可以定义一个 monoid, 单位元为 false
    implicit val eitherMonoid: Monoid[Boolean] = new Monoid[Boolean] {
        override def empty: Boolean = false
        override def combine(x: Boolean, y: Boolean): Boolean = (x && !y) || (!x && y)
      }
    println("单位元 1: " + identityLaw(true)(eitherMonoid)) // 单位元 1: true
    println("单位元 2: " + identityLaw(false)) // 单位元 2: true
    println("true, false, true: " + associativeLaw(true, false, true)(eitherMonoid)) // true
    println("false, false, true: " + associativeLaw(false, false, true)) // true
    println("true, false, false: " + associativeLaw(true, false, false)) // true
    println("false, false, false: " + associativeLaw(false, false, false)) // true
    println("true, true, true: " + associativeLaw(true, true, true)) // true
    println(eitherMonoid.combine(true, false)) // true
    println(eitherMonoid.combine(false, true)) // true
    println(eitherMonoid.combine(false, false)) // false
    println(eitherMonoid.combine(true, true)) // false
  }
}


package com.chapter02.ex1

object Test4 {
  def main(args: Array[String]): Unit = {
    import CustomMonoid._

    // Boolean的 exclusive nor可以定义一个 monoid, 单位元为 true
    implicit val norMonoid: Monoid[Boolean] = new Monoid[Boolean] {
        override def empty: Boolean = true
        override def combine(x: Boolean, y: Boolean): Boolean = (!x || y) && (x || !y)
      }
    println("单位元 1: " + identityLaw(true)(norMonoid)) // 单位元 1: true
    println("单位元 2: " + identityLaw(false)) // 单位元 2: true
    println("true, false, true: " + associativeLaw(true, false, true)(norMonoid)) // true
    println("false, false, true: " + associativeLaw(false, false, true)) // true
    println("true, false, false: " + associativeLaw(true, false, false)) // true
    println("false, false, false: " + associativeLaw(false, false, false)) // true
    println("true, true, true: " + associativeLaw(true, true, true)) // true
    println(norMonoid.combine(true, false)) // false
    println(norMonoid.combine(false, true)) // false
    println(norMonoid.combine(false, false)) // true
    println(norMonoid.combine(true, true)) // true
  }
}

```

- 自定义示例 2:
> - 为 Set集合的 union(取并集), intersect(取交集), diff(取差) + complement(取补集)操作, 定义 Monoid& Semigroup
```
package com.chapter02.ex2

import com.chapter02.ex1.CustomMonoid._

object Test1 {
  def main(args: Array[String]): Unit = {
    // union(取并集), 可以定义为 Monoid, 单位元为空集合
	// 使用 def定义 unionMonoid, 而非 val, 原因是 def定义可以接受类型参数
    implicit def unionMonoid[A]: Monoid[Set[A]] = new Monoid[Set[A]] {
        override def empty: Set[A] = Set.empty[A]
        override def combine(x: Set[A], y: Set[A]): Set[A] = x union y
      }
    println(identityLaw(Set(1, 2, 3))(unionMonoid)) // true
    println(identityLaw(Set(9))) // true
    println(associativeLaw(Set(1, 2, 3), Set(4, 5), Set(6))(unionMonoid)) // true
    println(associativeLaw(Set(9), Set(10, 20), Set(10))) // true
    println(unionMonoid.combine(Set(10, 20), Set(10, 20))) // Set(10, 20)
    println(unionMonoid.combine(Set(), Set(10, 20))) // Set(10, 20)
    println(unionMonoid.combine(Set(20), Set(10, 20))) // Set(20, 10)
  }
}


package com.chapter02.ex2

object Test2 {
  // 判断是否满足结合律法则:
  def associativeLaw[A](x: A, y: A, z: A)(implicit m: Semigroup[A]): Boolean =
    m.combine(m.combine(x, y), z) == m.combine(x, m.combine(y, z))

  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  def main(args: Array[String]): Unit = {
    // intersect取交集只能定义为 Semigroup, 因为无法为它找到有意义的单位元
    implicit def insersectSemigroup[A]: Semigroup[Set[A]] = (x, y) => x intersect y
    println(associativeLaw(Set(1, 2, 3), Set(4, 5), Set(6))(insersectSemigroup)) // true
    println(associativeLaw(Set(9), Set(10, 20), Set(10))) // true
    println(insersectSemigroup.combine(Set(10, 20), Set(10, 20))) // Set(10, 20)
    println(insersectSemigroup.combine(Set(), Set(10, 20))) // Set()
    println(insersectSemigroup.combine(Set(20), Set(10, 20))) // Set(20)
  }
}

```
`*union(取并集), 可以定义为 Monoid, 单位元为空集合`
`*intersect取交集只能定义为 Semigroup, 因为无法为它找到有意义的单位元`
`*diff(取差) + complement(取补集)不满足结合律, 因此不能为其定义 Monoid或 Semigroup实例`

# cats.Monoid& cats.Semigroup(type class)
import cats.Semigroup
import cats.Monoid
import cats.syntax.semigroup._ // or cats.syntax.monoid._

## cats: Monoid Instances
- 在 Cats中, type class是通过伴生对象, 获取 type class instance 例: Monoid[String]返回 Monoid[String]的实例
```
package com.chapter02.ex3

import cats.Monoid
import cats.instances.string._

object Test2 {
  def main(args: Array[String]): Unit = {
    println(Monoid[String].combine("hello", "world!")) // helloworld!
    println(Monoid[String].empty)
    // 下面的例子与上面的完全等价
    println(Monoid.apply[String].combine("hello", "world!")) // helloworld!
    println(Monoid.apply[String].empty)
  }
}


package com.chapter02.ex3

import cats.Monoid
import cats.instances.int._
import cats.instances.option._

object Test4 {
  def main(args: Array[String]): Unit = {
    val a = Option(1)
    val b = Option(2)
    println(Monoid[Option[Int]].combine(a, b)) // Some(3)
  }
}

```
`*因为 Monoid继承自 Semigroup, 因此如果不需要单位元, 则可以导入 Semigroup type class`
```
package com.chapter02.ex3

import cats.Semigroup
import cats.instances.string._

object Test3 {
  def main(args: Array[String]): Unit = {
    println(Semigroup.apply[String].combine("hello", "world!")) // helloworld!
  }
}

```

## cats: Monoid Syntax
- Cats为 combine函数定义了操作符 |+|, 定义在 cats.syntax.semigroup中, 但因为 cats.syntax.monoid继承了前者, 所以导入二者任一即可
```
package com.chapter02.ex3

import cats.instances.int._
import cats.instances.option._
import cats.syntax.semigroup._ // or cats.syntax.monoid._

object Test1 {
  def main(args: Array[String]): Unit = {
    val a = Option(1)
    val b = Option(2)
    println(a |+| b) // Some(3)
    println(a combine b) // Some(3)

    println(1 combine 2) // 3
    println(1 |+| 2) // 3

    val c = Option(Set(1,2,3))
    val d = Option(Set(3,4,5))
    println(c |+| d) // Some(Set(5, 1, 2, 3, 4))

    val e = List(1,2,3)
    val f = List(3,4,5)
    println(e combine f) // List(1, 2, 3, 3, 4, 5)
  }
}

```

### cats: Exercise: Adding All The Things
```
package com.chapter02.ex4

object Test1 {
  def main(args: Array[String]): Unit = {
    /**
     * 两种实现
     * 1. 使用 Monoid, 2. 未使用 Monoid
     */
    def add1(items: List[Int]): Int = items.foldLeft(0)(_ + _)
    println(add1(List(10, 20, 30, 40, 50, 60))) // 210

    def add2(items: List[Int]): Int = {
      import cats.Monoid
      import cats.instances.int._
      import cats.syntax.monoid._
      items.foldLeft(Monoid[Int].empty)(_ |+| _)
    }
    println(add2(List(10, 20, 30, 40, 50, 60))) // 210
  }
}


package com.chapter02.ex4

import cats.Monoid
import cats.syntax.semigroup._
import cats.instances.option._

object Test2 {
  def main(args: Array[String]): Unit = {
    /**
     * 需求 add函数, 需兼容 Int和 Option[Int]类型入参, 此时只能使用 Monoid来实现
     */
    def add1[A](items: List[A])(implicit m: Monoid[A]): A = items.foldLeft(m.empty)(_ |+| _)
    println(add1(List(10, 20, 30, 40, 50, 60))) // 210
    println(add1(List(Option(10), Option(20), Option(30), Option(40), Option(50), Option(60)))) // Some(210)

    /**
     * 可以将上面的函数简化: 通过 Scala的 context bound:
     *
     * 1. 去掉第二个参数列表 (implicit m: Monoid[A]),
     * 2. 将 def add1[A]改写为 def add2[A: Monoid], 这里的 Monoid是描述 implicit值, 是通过伴生对象 Monoid.apply[A]来获取 Monoid的 instance的
	 * * 这个函数非常通用, 只要提供合适的 Monoid实例, 便能能实现任意类型的累加
     */
    def add2[A: Monoid](items: List[A]): A = items.foldLeft(Monoid[A].empty)(_ |+| _)
    println(add2(List(10, 20, 30, 40, 50, 60))) // 210
    println(add2(List(Option(10), Option(20), Option(30), Option(40), Option(50), Option(60)))) // Some(210)
  }
}


package com.chapter02.ex4

import cats.Monoid
import cats.syntax.semigroup._

object Test3 {
  // 自定义样例类
  case class Order(totalCost: Double, quantity: Double)
  object Order {
    // Order单例对象中定义隐式值
    implicit val orderMonoid = new Monoid[Order] {
        override def empty: Order = Order(0, 0)
        override def combine(x: Order, y: Order): Order = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
      }
  }

  def add[A: Monoid](items: List[A]): A = items.foldLeft(Monoid[A].empty)(_ |+| _)

  def main(args: Array[String]): Unit = {
    /*实现对 Order类型的累加:
     * 1. 依赖 Monoid实例实现累加的操作, 所以不管什么类型, 只要提供该类型的 Monoid实例
     * 2. 将 orderMonoid 放在 Order 伴生对象中, 省去 import的麻烦
     */
    println(add(List(Order(1.0, 2.0), Order(3.0, 4.0), Order(2.0, 3.0)))) // Order(6.0,9.0)
  }
}

```

- 只要作用域中有合适的 type class instances, 便可以使用 Monoid实现 addition操作:
```
package com.chapter02.ex4

import cats.syntax.semigroup._

object Test4 {
  def main(args: Array[String]): Unit = {
    import cats.instances.set._
    println(Set(1, 2, 3) |+| Set(1, 2, 4)) // Set(1, 2, 3, 4)

    import cats.instances.map._
    println(Map("a" → 1) |+| Map("b" → 2)) // Map(b -> 2, a -> 1)

    import cats.instances.string._
    println("Hello" |+| " world!") // Hello world!

    import cats.instances.int._
    println(1 |+| 2) // 3
  }
}

```
