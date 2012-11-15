package com.github.j5ik2o.messagecast.infrastructure.model


import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import anorm.Id

import java.util

class UserStatusDaoSpec  extends Specification{

  sequential

  val dataSource = "test"


  "UserStatusDao" should {
    "UserStatusをINSERTできること" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase(dataSource))) {
        val user = User(
          id = Id("1"),
          name = "1",
          password = "1",
          bio = "aaa",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 1
        )
        UserDao(dataSource).insert(user) must_== 1
        val userStatus = UserStatus(
          id = Id("1"),
          fromUserId = "1",
          statusType = 1,
          status = "test",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserStatusDao(dataSource).insert(userStatus) must_== 1
      }
    }

    "UserStatusをUPDATEできること" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase(dataSource))) {
        val user = User(
          id = Id("1"),
          name = "1",
          password = "1",
          bio = "aaa",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user) must_== 1
        val userStatus1 = UserStatus(
          id = Id("1"),
          fromUserId = "1",
          statusType = 1,
          status = "test",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserStatusDao(dataSource).insert(userStatus1) must_== 1
        val userStatus2 = UserStatus(
          id = Id("1"),
          fromUserId = "1",
          statusType = 1,
          status = "test2",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserStatusDao(dataSource).update(userStatus2) must_== 1
      }
    }

    "UserStatusをDELETEできること" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase(dataSource))) {
        val user = User(
          id = Id("1"),
          name = "1",
          password = "1",
          bio = "aaa",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user) must_== 1
        val userStatus = UserStatus(
          id = Id("1"),
          fromUserId = "1",
          statusType = 1,
          status = "test",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserStatusDao(dataSource).insert(userStatus) must_== 1
        UserStatusDao(dataSource).delete(userStatus.id.get) must_== 1
      }
    }

  }


}
