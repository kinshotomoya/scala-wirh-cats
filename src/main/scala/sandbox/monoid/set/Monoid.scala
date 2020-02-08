package sandbox.monoid.set


import cats.instances.string._
import cats.instances.int._
import cats.instances.option._
import cats.syntax.semigroup._
import cats.kernel

object Test extends App {

  cats.Monoid[String].combine("s", "ww")
  cats.Monoid.apply[String].combine("s", "ww")

  cats.Monoid[Int].combine(22, 4)

  cats.Monoid[Option[Int]].combine(Some(1), Some(33))

  "ss" |+| "ww"

  val result: Option[Int] = SuperAdder.addMonoid2(List(Some(22), Some(33)))
  println(result)

}

object SuperAdder {
  def add(items: List[Int]): Int = {
    items.foldLeft(0)((_: Int) + (_: Int))
  }

  def addMonoid(items: List[Int]): Int = {
    items.foldLeft(cats.Monoid[Int].empty)((_: Int).combine(_: Int))
  }

  def addMonoid2(items: List[Option[Int]]): Option[Int] = {
    items.foldLeft(cats.Monoid[Option[Int]].empty)(_.combine(_))
  }

  import OrderInstance._
  def addOrder(orderA: Order, orderB: Order): Order = {
    cats.Monoid[Order].combine(Order(1, 1), Order(2, 2))
  }
}

object OrderInstance {
  implicit val orderInstance: kernel.Monoid[Order] = new cats.Monoid[Order] {
    override def combine(x: Order, y: Order): Order = {
      Order(
        x.totalCost + y.totalCost,
        x.quantity + y.quantity
      )
    }

    override def empty: Order = Order(0.0, 0.0)
  }
}


case class Order(totalCost: Double, quantity: Double)

