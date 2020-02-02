package sandbox.cats.eq
import cats.Eq
import cats.instances.string._
import cats.instances.int._
import cats.syntax.eq._
import cats.instances.option._
import cats.syntax.option._
import java.util.Date

import cats.instances.long._

object Main extends App {

  // applyメソッドでインスタンス化している
  // instanceは、すでにimportされているデフォルトのものを使っている
  // 本当はEq[Int].apply(instance)
  // 以下のようなimplicit valがデフォルトで実装されている

//  implicit val eqInttt = new Eq[Int] {
//    override def eqv(x: Int, y: Int): Boolean = {
//      x == y
//    }
//  }

  val eqInt: Eq[Int] = Eq[Int]
  eqInt.eqv(2, 2)

  123 === 222

  (Some(1): Option[Int]) === None

  Option(1) === Option.empty[Int]

  1.some === none[Int]


  // 型クラスのDate型のEqを実装している
  // Date型でEqのメソッドを使う場合には、このインスタンスが使われる
  // String Intなどのデフォルト型のインスタンスはデフォルトで作成されているので、実装しなくても===が使える
  implicit val eqDate: Eq[Date] = new Eq[Date] {
    override def eqv(x: Date, y: Date): Boolean = {
      x.getTime === y.getTime
    }
  }

  val a: Date = new Date()
  val b: Date = new Date()

  // ===は内部的に、eqvを呼び出している
  a === a

  // ============================================
  // exercise
  val cat1 = Cat("Garfield", 38, "orange and black")
  val cat2 = Cat("Heathcliff", 33, "orange and black")

  val optionCat1 = Option(cat1)
  val optionCat2 = Option.empty[Cat]


  implicit val eqCat: Eq[Cat] = new Eq[Cat] {
    override def eqv(cat1Opt: Cat, cat2Opt: Cat): Boolean = {
      (cat1.name === cat2.name) && (cat1.age === cat2.age) && (cat1.color === cat2.color)
    }
  }

  cat1.===(cat2)

//  val catEq: Eq[Cat] = Eq[Cat](eqCat)
//  catEq.eqv(cat1, cat2)

  cat1 === cat2

  optionCat1 === optionCat2

  // applyメソッドは、implicitパラメーターを受けとる。Eqの型クラスをCat型で実装したインスタンスを受け取る。
  val eqqq: Eq[Cat] = Eq.apply[Cat]
  eqqq.eqv(cat1, cat2)

}


final case class Cat(name: String, age: Int, color: String)


