package sandbox.monad

object Main13 {

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

  // monadは、pureファンクションと、faltMapファンクションを持つ
  trait Monad[F[_]] {
    def pure[A](value: A): F[A]

    def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

    // mapファンクションも、pureとflatMapを組み合わせて実装できる
    def map[A, B](value: F[A])(func: A => B): F[B] = {
      flatMap(value)(a=> pure(func(a)))
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
