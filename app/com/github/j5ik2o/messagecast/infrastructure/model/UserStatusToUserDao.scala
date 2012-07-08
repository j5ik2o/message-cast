package com.github.j5ik2o.messagecast.infrastructure.model

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util


object UserStatusToUserDao {

  val simple = {
    get[Pk[String]]("user_status_to_user.id") ~
      get[String]("user_status_to_user.user_status_id") ~
      get[String]("user_status_to_user.to_user_id") ~
      get[util.Date]("user_status_to_user.create_date") ~
      get[util.Date]("user_status_to_user.update_date") ~
      get[Long]("user_status_to_user.version") ~
      get[Boolean]("user_status_to_user.deleted") map {
      case id ~ userStatusId ~ toUserId ~ createDate ~ updateDate ~ version ~ deleted =>
        UserStatusToUser(id, userStatusId, toUserId, createDate, updateDate, version, deleted)
    }
  }

  def apply(dataSource: String = "default") = new UserStatusToUserDao(dataSource)

}

class UserStatusToUserDao(dataSource: String) {

  def findById(id: String): Option[UserStatusToUser] = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL("select * from `user_status_to_user` where `id` = {id} and `deleted` = 0").on('id -> id).as(UserStatusToUserDao.simple.singleOpt)
    }
  }

  def findByUserStatusId(userStatusIds: Set[String]): List[UserStatusToUser] = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL(
          """
            select * from `user_status_to_user` where `user_status_id` in ({userStatusIds}) and `deleted` = 0
          """).on('userStatusIds -> userStatusIds.mkString(",")).as(UserStatusToUserDao.simple *)
    }
  }

  def update(userStatusToUser: UserStatusToUser) = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL(
          """
            update `user_status_to_user`
              set
                `user_status_id` = {user_status_id},
                `to_user_id` = {from_user_id},
                `create_date` = {create_date},
                `update_date` = {update_date},
                `version` = `version` + 1,
                `deleted` = {deleted}
            where `id` = {id} and `deleted` = 0 and `version` = {version}
          """
        ).on(
          'id -> userStatusToUser.id,
          'user_status_id -> userStatusToUser.userStatusId,
          'from_user_id -> userStatusToUser.toUserId,
          'create_date -> userStatusToUser.createDate,
          'update_date -> userStatusToUser.updateDate,
          'version -> userStatusToUser.version,
          'deleted -> userStatusToUser.deleted
        ).executeUpdate()
    }
  }

  def insert(userStatusToUser: UserStatusToUser) = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL(
          """
            insert into `user_status_to_user`(id, user_status_id, to_user_id, create_date, update_date, version, deleted) values (
              {id}, {user_status_id}, {to_user_id}, {create_date}, {update_date}, {version}, {deleted}
            )
          """
        ).on(
          'id -> userStatusToUser.id,
          'user_status_id -> userStatusToUser.userStatusId,
          'to_user_id -> userStatusToUser.toUserId,
          'create_date -> userStatusToUser.createDate,
          'update_date -> userStatusToUser.updateDate,
          'version -> userStatusToUser.version,
          'deleted -> userStatusToUser.deleted
        ).executeUpdate()
    }
  }

  def delete(id: String) = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL("delete from `user_status_to_user` where id = {id}").on('id -> id).executeUpdate()
    }
  }


}
