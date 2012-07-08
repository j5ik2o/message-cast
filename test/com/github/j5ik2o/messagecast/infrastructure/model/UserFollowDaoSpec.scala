package com.github.j5ik2o.messagecast.infrastructure.model

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import anorm.Id

import java.util

class UserFollowDaoSpec extends Specification {

  sequential

  val dataSource = "test"

  "UserFollowDao" should {
    "followsを取得できること" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase(dataSource))) {
        val user1 = User(
          id = Id("1"),
          name = "1",
          password = "1",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user1) must_== 1
        val user2 = User(
          id = Id("2"),
          name = "2",
          password = "2",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user2) must_== 1
        val userFollow = UserFollow(
          id = Id("1->2"),
          fromUserId = "1",
          toUserId = "2",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserFollowDao(dataSource).insert(userFollow) must_== 1
        val follows = UserFollowDao(dataSource).findFollows("1")
        follows.size must_== 1
        follows(0).id.get must_== "2"
      }
    }
    "followersを取得できること" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase(dataSource))) {
        val user1 = User(
          id = Id("1"),
          name = "1",
          password = "1",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user1) must_== 1
        val user2 = User(
          id = Id("2"),
          name = "2",
          password = "2",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user2) must_== 1
        val userFollow = UserFollow(
          id = Id("1->2"),
          fromUserId = "1",
          toUserId = "2",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserFollowDao(dataSource).insert(userFollow) must_== 1
        val followers = UserFollowDao(dataSource).findFollowers("2")
        followers.size must_== 1
        followers(0).id.get must_== "1"
      }
    }


    "UserFollowをINSERTできること" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase(dataSource))) {
        val user1 = User(
          id = Id("1"),
          name = "1",
          password = "1",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user1) must_== 1
        val user2 = User(
          id = Id("2"),
          name = "2",
          password = "2",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user2) must_== 1
        val userFollow = UserFollow(
          id = Id("1->2"),
          fromUserId = "1",
          toUserId = "2",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserFollowDao(dataSource).insert(userFollow) must_== 1
      }
    }
    "UserFollowをUPDATEできること" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase(dataSource))) {
        val user1 = User(
          id = Id("1"),
          name = "1",
          password = "1",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user1) must_== 1
        val user2 = User(
          id = Id("2"),
          name = "2",
          password = "2",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user2) must_== 1
        val userFollow1 = UserFollow(
          id = Id("1:2"),
          fromUserId = "1",
          toUserId = "2",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserFollowDao(dataSource).insert(userFollow1) must_== 1
        val userFollow2 = UserFollow(
          id = Id("1:2"),
          fromUserId = "2",
          toUserId = "1",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserFollowDao(dataSource).update(userFollow2) must_== 1
      }
    }
    "UserFollowをDELETEできること" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase(dataSource))) {
        val user1 = User(
          id = Id("1"),
          name = "1",
          password = "1",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user1) must_== 1
        val user2 = User(
          id = Id("2"),
          name = "2",
          password = "2",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserDao(dataSource).insert(user2) must_== 1
        val userFollow = UserFollow(
          id = Id("1->2"),
          fromUserId = "1",
          toUserId = "2",
          createDate = new util.Date(),
          updateDate = new util.Date(),
          version = 0
        )
        UserFollowDao(dataSource).insert(userFollow) must_== 1
        UserFollowDao(dataSource).delete(userFollow.id.get) must_== 1
      }
    }
  }

}
