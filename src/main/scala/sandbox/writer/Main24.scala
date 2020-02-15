package sandbox.writer

object Main24 extends App {
  import cats.data.Writer
  import cats.instances.vector._
  import cats.syntax.applicative._

  Writer(
    Vector(
      "test",
      "test"
    ),
    1223
  )

  type Logged[A] = Writer[Vector[String], A]

  val value: Logged[Int] = 123.pure[Logged]
}
