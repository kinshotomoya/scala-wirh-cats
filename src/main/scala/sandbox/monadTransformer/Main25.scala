package sandbox.monadTransformer

import sandbox.either.Main22.User


// ２つのモナドが現れた際に、合成して処理を簡単にしたい。
// デフォルトではモナドは合成できない
object Main25 extends App {

  def lookupUserName(id: Long): Either[Error, Option[String]] =
  // 以下のようにuserを取得するには EitherとOptionがネストしているので、二回forを回さないといけない
    for {
      optUser <- lookupUser(id)
    } yield {
      for { user <- optUser } yield user
    }


  // DBからuser情報を取得する
  def lookupUser(id: Long): Either[Error, Option[String]] = ???


  // ２つの型がどちらもわからない場合は、結合できない

  import cats.Monad
  import cats.syntax.applicative._ // for pure
  import cats.syntax.flatMap._     // for flatMap
  import scala.language.higherKinds

  def compose[M1[_]: Monad, M2[_]: Monad] = {
    type Composed[A] = M1[M2[A]]

//    new Monad[Composed] {
//      override def pure[A](a: A): Composed[A] = {
//        a.pure[M2].pure[M1]
//      }
//
//      override def flatMap[A, B](fa: Composed[A])(f: A => Composed[B]): Composed[B] = {
//        // ２つの型がわからんかったら実装できない
//        ???
//      }
//    }
//  }
//
//  // もし１つがOptionと決まっていたらflatMapも実装できる
//
//  def compose2[M1[_]: Monad, M2[_]: Monad] = {
//    type Composed[A] = M[]
//  }
}

object Main26 extends App {
  import cats.data.OptionT

  // 外側のモナド型であるListを内側のモナドトランスフォーマーであるOptionTに渡している
  // ListとOptionというモナドを合成して、ListOptionというモナド型を作っている。
  // そうすることで、ネスト処理などがなくなり簡潔にかける
  type ListOption[A] = OptionT[List, A]
  // List[Option[A]]と同じ

  import cats.Monad
  import cats.instances.list._     // for Monad
  import cats.syntax.applicative._

  val result1: ListOption[Int] = OptionT(List(Option(123)))
  val result2: ListOption[Int] = 32.pure[ListOption]

  for {
    a: Int <- result1
    b: Int <- result2
  } yield a + b

  // ほんまやったら、以下のようにネストさせないといけない
  val result3 = List(Option(123))
  val result4 = List(Option(32))
  for {
    a: Option[Int] <- result3
    b: Option[Int] <- result4
  } yield for {
    c: Int <- a
    d: Int <- b
  } yield c + d

}
