package sandbox.monad

import cats.{Id, Monad}

object Main13 {

  // monadは、pureファンクションと、faltMapファンクションを持つ
  // あと、モナド則を満たす
  // なぜ使うのか
  // 文脈に依存せずに中身のデータ（本質）を処理できる
  // ex
  // List(1, 2, 3).map(x*4)
  // Listは文脈。1, 2, 3を処理したい
  // 文脈は他のものもある。
  // Option Eitherなどもそう。これらは標準で実装されている。



  def parseInt(str: String): Option[Int] = {
    scala.util.Try(str.toInt).toOption
  }

  def divide(a: Int, b: Int): Option[Int] = {
    if(b == 0) None else Some(a / b)
  }

  def stringDividedBy(aStr: String, bStr: String): Option[Int] = {
//    parseInt(aStr).flatMap(aNum => parseInt(bStr).flatMap(bNum => divide(aNum, bNum)))
    for {
      a <- parseInt(aStr)
      b <- parseInt(bStr)
      result <- divide(a, b)
    } yield result
  }
}


object Main14 {
  import scala.language.higherKinds

  trait Monad[F[_]] {
    def pure[A](value: A): F[A]

    def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

    // mapファンクションも、pureとflatMapを組み合わせて実装できる
    def map[A, B](value: F[A])(func: A => B): F[B] = {
      flatMap(value)(a => pure(func(a)))
    }
  }
}


object Main15 {
  import cats.Monad
  import cats.instances.option._
  import cats.instances.list._

  val ints: List[Int] = Monad[List].pure(222)

  val opt1: Option[Int] = Monad[Option].pure(2222)

  val opt2: Option[Int] = Monad[Option].flatMap(opt1)(x => Some(x * 3))

  Monad[List].flatMap(List(1, 2, 3))(a => List(a * 2))
}

object Main16 extends App {
  import cats.Monad
  import cats.instances.future._
  import scala.concurrent._
  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global


  val futureMon: Monad[Future] = Monad[Future]
  val future: Future[String] = futureMon.flatMap(futureMon.pure(234))(x => futureMon.pure(x.toString))
  println(Await.result(future, 1.second))

}

object Main17 extends App {
  import cats.instances.option._
  import cats.syntax.applicative._

  val maybeInt: Option[Int] = 1.pure[Option]
}


object Main18 extends App {
  import cats.Monad
  import cats.syntax.functor._
  import cats.syntax.flatMap._
  import cats.instances.option._
  import cats.instances.list._

  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] = {
    for {
      x <- a
      y <- b
    } yield x*x + y*y
  }

  sumSquare(Option(1), Option(33))
  sumSquare(List(111), List(333))
}

object Main19 extends App {
  // IDは、型を１つしか持たないモナドになれる
  // 例えば、"test": Id[String]とすると、モナドになりmap flatMapが使える
  import cats.Id
  import cats.syntax.functor._
  import cats.syntax.flatMap._
  import cats.Monad

  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] = {
    for {
      x <- a
      y <- b
    } yield x*x + y*y
  }

  sumSquare(3: Id[Int], 4: Id[Int])

  val aaa = "test": Id[String]
  List(333): Id[List[Int]]

  val value: Id[Int] = Monad[Id].pure(333)
  val value2: Id[Int] = Monad[Id].flatMap(value)(x => x*2)

}

object Main20 extends App {
  import cats.Id

  def pure[A](value: A): A = {
    value
  }

  def map[A, B](value: Id[A])(func: A => B): Id[B] = {
    func(value)
  }

  def flatMap[A, B](value: Id[A])(func: A => Id[B]): Id[B] = {
    func(value)
  }

  map(123)(x => x*2)
  flatMap(345)(x => x*44)
}

