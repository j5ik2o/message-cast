package com.github.j5ik2o.messagecast.infrastructure.model


import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import anorm.Id

import java.util

class UserStatusToUserDaoSpec extends Specification {

  sequential

  val dataSource = "test"

  "UserStatusToUserDao" should {
    "UserStatusToUserをINSERTできること" in {
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
        val userStatusToUser = UserStatusToUser(
          id = Id("1"),
          userStatusId = "1",
          toUserId = "1",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserStatusToUserDao(dataSource).insert(userStatusToUser) must_== 1
      }
    }

    "UserStatusToUserをUPDATEできること" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase(dataSource))) {
        val user1 = User(
          id = Id("1"),
          name = "1",
          password = "1",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 1
        )
        val user2 = User(
          id = Id("2"),
          name = "2",
          password = "2",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 1
        )
        UserDao(dataSource).insert(user1) must_== 1
        UserDao(dataSource).insert(user2) must_== 1
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
        val userStatusToUser1 = UserStatusToUser(
          id = Id("1"),
          userStatusId = "1",
          toUserId = "1",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserStatusToUserDao(dataSource).insert(userStatusToUser1) must_== 1
        val userStatusToUser2 = UserStatusToUser(
          id = Id("1"),
          userStatusId = "1",
          toUserId = "2",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserStatusToUserDao(dataSource).update(userStatusToUser2) must_== 1
      }
    }

    "UserStatusToUserをDELETEできること" in {
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
        val userStatusToUser = UserStatusToUser(
          id = Id("1"),
          userStatusId = "1",
          toUserId = "1",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserStatusToUserDao(dataSource).insert(userStatusToUser) must_== 1
        UserStatusToUserDao(dataSource).delete(userStatusToUser.id.get) must_== 1
      }
    }


  }
}
