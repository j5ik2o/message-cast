package com.github.j5ik2o.messagecast.infrastructure.model

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import java.util
import anorm.Id

class UserDaoSpec extends Specification {

  sequential

  val dataSource = "test"

  "UserDao" should {

    "UserをINSERTできること" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase(dataSource))) {
        val user = User(
          id = Id("1"),
          name = "1",
          password = "1",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 1
        )
        UserDao(dataSource).insert(user) must_== 1
      }
    }

    "UserをUPDATEできること" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase("test"))) {
        val user = User(
          id = Id("1"),
          name = "1",
          password = "1",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user) must_== 1
        UserDao(dataSource).update(user) must_== 1
      }
    }

    "UserをDELETEできること" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase("test"))) {
        val user = User(
          id = Id("1"),
          name = "1",
          password = "1",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user) must_== 1
        UserDao(dataSource).delete(user.id.get) must_== 1
      }
    }
  }

}
