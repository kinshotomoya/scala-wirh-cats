package sandbox.either
import scala.collection.immutable

object Main21 extends App {
  val either1 = Right(11)
  val either2 = Right(33)
  val test: Either[Nothing, Int] = for {
    a <- either1
    b <- either2
  } yield a + b

  import cats.syntax.either._

  val a: Either[String, Int] = 3.asRight[String]
  val b = 4.asRight[String]

  val result: Either[String, Int] = for {
    x <- a
    y <- b
  } yield x + y

  def count(num: List[Int]) = {
    // Either型を返したいので、Right(0)ではなくて 0.asRightを使う
    num.foldLeft(0.asRight[String]){(acc: Either[String, Int], num: Int) => {
      if(num > 0) {
        acc.map(_ + 1)
      } else {
        Left("noooooooo")
      }
    }}
  }

  for {
    a <- 1.asRight[String]
    b <- 0.asRight[String]
    c <- if(b == 0) "Error".asLeft[Int] else (a/b).asRight[String]
  } yield c * 100

}

object Main22 extends App {
  import cats.syntax.either._

  sealed trait LoginError

  // case classを定義すると、自動的にtrait Product trait Serializableがミックスインされる
  final case class UserNameError(userName: String) extends LoginError
  final case class PasswordError(password: String) extends LoginError
  case object UnexpectedError extends LoginError

  case class User(name: String, password: String)

  // 自前で型を作ってあげている
  // わかりやすいように
  type LoginResult = Either[LoginError, User]

  def errorHandling(error: LoginError): Unit = {
    error match {
      case UserNameError(u) => println("username is not found")
      case PasswordError(_) => println("password is not correct")
      case UnexpectedError => println("unexpected error")

    }

    val result1: LoginResult = User("tomoya", "kinsho").asRight[LoginError]
    // either.foldは、Leftの場合は第一引数が実行、Rightの場合は第二引数が実行される
    result1.fold(errorHandling, println)

    val result2: LoginResult = UserNameError("tomoya").asLeft[User]
    result2.fold(
      _ => errorHandling(_),
      _ => println(_)
    )
  }

}

