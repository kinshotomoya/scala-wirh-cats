package sandbox.monoid.set

object Main extends App {

  implicit def setUnionMonoid[A]: Monoid[Set[A]] = {
    new Monoid[Set[A]] {
      override def empty: Set[A] = {
        Set.empty[A]
      }

      override def combine(x: Set[A], y: Set[A]): Set[A] = {
        x union y
      }
    }
  }

  val intSetMonoid: Monoid[Set[Int]] = Monoid[Set[Int]]
  intSetMonoid.combine(Set(1, 2), Set(4, 2))

  implicit def setIntersectSemi = new Semigroup[Set[Int]] {
    override def combine(x: Set[Int], y: Set[Int]): Set[Int] = {
      x intersect y
    }
  }

}



trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A]{
  def empty: A
}

object Monoid {
  def apply[A](implicit monoid: Monoid[A]): Monoid[A] = monoid
}

