package sandbox.monoid.bool

//***
// Monoidとは
// 演算が集合に対して閉じている状態
// ex)
// hoge型 + hoge型 = fuga型 NG
// Int + Int = Int OK!!!!!!!!

// 以下の２つの規則がある
// 1. 結合則
// combine(x, combine(y, z)) = combine(combine(x, y), z)

// 2. empty
// combine(x, empty) = combine(empty, x) = x
//***






object Main extends App {
  val value: Monoid[Boolean] = Monoid[Boolean]
}


trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  def apply[A](implicit monoid: Monoid[A]) = monoid
}

object MonoidInstance {
  // Booleanは４つのMonoidを持っている
  implicit val monoidBoolInsAnd: Monoid[Boolean] = new Monoid[Boolean] {
    override def combine(x: Boolean, y: Boolean): Boolean = {
      x && y
    }

    override def empty: Boolean = {
      true
    }
  }

  implicit val monoidBoolInsOr: Monoid[Boolean] = new Monoid[Boolean] {
    override def combine(x: Boolean, y: Boolean): Boolean = {
      x || y
    }

    override def empty: Boolean = {
      false
    }
  }

  implicit val monoidBoolInsEither: Monoid[Boolean] = new Monoid[Boolean] {
    override def combine(x: Boolean, y: Boolean): Boolean = {
      (x && !y) || (!x && y)
    }

    override def empty: Boolean = {
      false
    }
  }

  implicit val monoidBoolInsNor: Monoid[Boolean] =  new Monoid[Boolean] {
    override def empty: Boolean = true

    override def combine(x: Boolean, y: Boolean): Boolean = {
      (!x || y) && (x || !y)
    }
  }

}