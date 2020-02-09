package sandbox.functor

import cats.Functor
import cats.syntax.functor._

object Main10 extends App {
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit def futureFunctor: Functor[Future] = new Functor[Future] {
    override def map[A, B](future: Future[A])(func: A => B): Future[B] = {
      future.map(func)
    }
  }

   Functor[Future].map(Future(11))(_ * 44)
}

object Main11 extends App {

  sealed trait Tree[+A]

  final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  final case class Leaf[A](value: A) extends Tree[A]


  implicit val branchFunctor: Functor[Tree] = new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = {
      fa match {
        case Branch(left, right) => Branch(map(left)(f), map(right)(f))
        case Leaf(v) => Leaf(f(v))
      }
    }
  }

  println(Functor[Tree].map(Branch(Leaf(12), Leaf(33)))(_ * 3))

  object Tree {
    def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = {
      Branch(left, right)
    }

    def leaf[A](value: A): Tree[A] = {
      Leaf(value)
    }
  }

  println(Tree.leaf("wwww").map(_ + "!!!!!"))

}
