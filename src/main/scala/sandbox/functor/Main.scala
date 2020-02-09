package sandbox.functor

import cats.Functor

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Random
import cats.instances.function._
import cats.syntax.functor._

import scala.language.higherKinds
import cats.instances.list._
import cats.instances.option._


object Main extends App {
  val future = Future(123).map(n => n + 1).map(n => n*2).map(_.toString)
  Await.result(future, 1.second)
}


object Main2 extends App {
  val number = new Random(0L)
  val future1 =  {
    val next = Future(number.nextInt())
    for {
      a: Int <- next
      b <- next
    } yield (a, b)

  }

  val future2 = {
    for {
      // nextIntは副作用である。
      // 実行するたびにアウトプットが変わる
      a <- Future(number.nextInt())
      b <- Future(number.nextInt())
    } yield (a, b)
  }

  println(Await.result(future1, 1.second))
  println(Await.result(future2, 1.second))
}

object Main3 extends App {
  val func1: Int => Double = (x: Int) => x.toDouble
  val func2: Double => Double = (x: Double) => x * 2

  println(func1.map(n => func2(n))(2))
  println(func1.map(func2)(2))
  func1.andThen(func2)(33)
  func2(func1(44))



  val func4 = ((x: Int) => x.toDouble).map(x => x + 1).map(x => x * 2).map(x => x + "!")

  val stringList: List[String] = List("string")
  val intList: List[Int] = List(1)


  new Hoge[Map]

  new Huga[List]

}

// functor
// 第一引数を内部に
// map関数を提供する

// 高カインド型の宣言
// Hogeクラスは、Listなどの型コンストラクタを引数に取る

// 引数が２つの型をパラメータとして取る
// Mapなど
class Hoge[F[_, _]]

// 引数が１つの型をパラメータとして取る
// List Optionなど
class Huga[F[_]]

// 型コンストラクタ
// 型を１つ取る
// Aのことを型パラメータという
// List[A]

// Intなどは型パラメータは存在しない


object Main4 extends App {
  val list1 = List(1, 2, 3)
  val list2 = Functor[List].map(list1)(n => n * 2)
  list1.map(n => n * 2)

  val option1 = Option(1)
  Functor[Option].map(option1)(_.toString)
}


object Main5 extends App {
  val func = (x: Int) => x + 1
  val liftedFunc = Functor[Option].lift(func)
  liftedFunc(Option(222))
}

object Main6 extends App {
  val func1 = (x: Int) => x + 1
  val func2 = (x: Int) => x * 2
  func1.map(func2)(22)
}

object Main7 extends App {

  // 型として１つの引数をとる型をdoMathメソッドは引数にとる。
  def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]) = {
    start.map(n => n + 2)
  }

  println(doMath[Option](Option(33)))
  // なので、以下のようにMapは引数にとることはできない
  // doMath(Map("ss" -> "a"))
  println(doMath[List](List(1, 2, 3)))

}


object Main8 extends App {
  final case class Box[A](value: A)

  // カスタムタイプを実装する
  // implicit valでbox型のmapメソッドを実装する
  implicit val boxFunctor: Functor[Box] = new Functor[Box] {
    override def map[A, B](box: Box[A])(f: A => B): Box[B] = {
      Box(f(box.value))
    }
  }

  val box = Box(2)
  println(Functor[Box](boxFunctor).map(box)(_ * 33))
  box.map(_ * 2)
}

object Main9 extends App {
  implicit val optionFunctor: Functor[Option] = new Functor[Option] {
    override def map[A, B](value: Option[A])(f: A => B): Option[B] = {
      value.map(f)
    }
  }

  Functor[Option].map(Option(1))(_ * 44)
}

