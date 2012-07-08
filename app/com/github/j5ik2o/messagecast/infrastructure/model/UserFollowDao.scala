package com.github.j5ik2o.messagecast.infrastructure.model

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._
import java.util

object UserFollowDao {

  val simple = {
    get[Pk[String]]("user_follow.id") ~
      get[String]("user_follow.from_user_id") ~
      get[String]("user_follow.to_user_id") ~
      get[util.Date]("user_follow.create_date") ~
      get[util.Date]("user_follow.update_date") ~
      get[Long]("user_follow.version") ~
      get[Boolean]("user_follow.deleted") map {
      case id ~ fromUserId ~ toUserId ~ createDate ~ updateDate ~ version ~ deleted =>
        UserFollow(id, fromUserId, toUserId, createDate, updateDate, version, deleted)
    }
  }

  def apply(dataSource: String = "default") = new UserFollowDao(dataSource)

}

class UserFollowDao(dataSource: String) {

  def findById(id: String): Option[UserFollow] = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL("select * from `user_follow` where `id` = {id} and deleted = 0").
          on('id -> id).as(UserFollowDao.simple.singleOpt)
    }
  }

  def findByFromUserIdWithToUserId(fromId: String, toId: String): Option[UserFollow] = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL("select * from `user_follow` where `from_user_id` = {from_user_id} and `to_user_id` = {to_user_id}").
          on('fromId -> fromId, 'toId -> toId).as(UserFollowDao.simple.singleOpt)
    }
  }

  def findByFromUserId(ids: Set[String], pageNo: Long = 0, pageSize: Long = 10, orderBy: Int = 1): Page[UserFollow] = {
    val offest = pageSize * pageNo

    DB.withConnection(dataSource) {
      implicit connection =>

        val userFollows = SQL(
          """
            select * from `user_follow`
            where `user_follow`.`from_user_id` in ({ids}) and deleted = 0
            order by {orderBy}
            limit {pageSize} offset {offset}
          """
        ).on(
          'pageSize -> pageSize,
          'offset -> offest,
          'ids -> ids.mkString(","),
          'orderBy -> orderBy
        ).as(UserFollowDao.simple *)

        val totalRows = SQL(
          """
            select count(*) from `user_follow`
            where `user_follow`.`from_user_id` in ({ids}) and deleted = 0
          """
        ).on(
          'ids -> ids.mkString(",")
        ).as(scalar[Long].single)

        Page(userFollows, pageNo, offest, totalRows)
    }
  }


  def findByToUserId(ids: Set[String], pageNo: Long = 0, pageSize: Long = 10, orderBy: Int = 1): Page[UserFollow] = {
    val offest = pageSize * pageNo

    DB.withConnection(dataSource) {
      implicit connection =>

        val userFollows = SQL(
          """
            select * from `user_follow`
            where `user_follow`.`to_user_id` in ({ids}) and deleted = 0
            order by {orderBy}
            limit {pageSize} offset {offset}
          """
        ).on(
          'pageSize -> pageSize,
          'offset -> offest,
          'ids -> ids.mkString(","),
          'orderBy -> orderBy
        ).as(UserFollowDao.simple *)

        val totalRows = SQL(
          """
            select count(*) from `user_follow`
            where `user_follow`.`to_user_id` in ({ids}) and deleted = 0
          """
        ).on(
          'ids -> ids.mkString(",")
        ).as(scalar[Long].single)

        Page(userFollows, pageNo, offest, totalRows)
    }
  }

  def findFollows(userId: String): List[User] = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL(
          """
          select
            user.id,
            user.name,
            user.password,
            user.create_date,
            user.update_date,
            user.version,
            user.deleted
          from
            user
          where
            id = (select
                    to_user_id
                  from
                    user_follow
                  where
                    from_user_id = {user_id} and deleted = 0)
          """
        ).
          on('user_id -> userId).as(UserDao.simple *)
    }
  }

  def findFollowers(userId: String): List[User] = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL(
          """
          select
            user.id,
            user.name,
            user.password,
            user.create_date,
            user.update_date,
            user.version,
            user.deleted
          from
            user
          where
            id = (select
                    from_user_id
                  from
                    user_follow
                  where
                    to_user_id = {user_id} and deleted = 0)
          """
        ).
          on('user_id -> userId).as(UserDao.simple *)
    }
  }

  def update(userFollow: UserFollow) = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL(
          """
            update `user_follow`
              set `from_user_id` = {from_user_id},
                  `to_user_id` = {to_user_id},
                  `create_date` = {create_date},
                  `update_date` = {update_date},
                  `version` = `version` + 1
            where `id` = {id} and deleted = 0 and `version` = {version}
          """
        ).on(
          'id -> userFollow.id,
          'from_user_id -> userFollow.fromUserId,
          'to_user_id -> userFollow.toUserId,
          'create_date -> userFollow.createDate,
          'update_date -> userFollow.updateDate,
          'version -> userFollow.version
        ).executeUpdate()
    }
  }

  def insert(userFollow: UserFollow) = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL(
          """
            insert into `user_follow`(id, from_user_id, to_user_id, create_date, update_date, version, deleted) values (
              {id}, {from_user_id}, {to_user_id}, {create_date}, {update_date}, 0, 0
            )
          """
        ).on(
          'id -> userFollow.id,
          'from_user_id -> userFollow.fromUserId,
          'to_user_id -> userFollow.toUserId,
          'create_date -> userFollow.createDate,
          'update_date -> userFollow.updateDate
        ).executeUpdate()
    }
  }

  def delete(id: String) = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL("delete from `user_follow` where id = {id}").
          on('id -> id).executeUpdate()
    }
  }

}
