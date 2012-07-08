package com.github.j5ik2o.messagecast.infrastructure.model

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util

object UserStatusDao {

  val simple = {
    get[Pk[String]]("user_status.id") ~
      get[String]("user_status.from_user_id") ~
      get[Int]("user_status.status_type") ~
      get[String]("user_status.status") ~
      get[util.Date]("user_status.create_date") ~
      get[util.Date]("user_status.update_date") ~
      get[Long]("user_status.version") ~
      get[Boolean]("user_status.deleted") map {
      case id ~ fromUserId ~ statusType ~ status ~ createDate ~ updateDate ~ version ~ deleted =>
        UserStatus(id, fromUserId, statusType, status, createDate, updateDate, version, deleted)
    }
  }

  def apply(dataSource: String = "default") = new UserStatusDao(dataSource)

}


class UserStatusDao(dataSource: String) {

  def findById(id: String): Option[UserStatus] = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL("select * from `user_status` where `id` = {id} and `deleted` = 0").on('id -> id).as(UserStatusDao.simple.singleOpt)
    }
  }

  def findByFromUserIds(toUserIds: Set[String], pageNo: Long = 0, pageSize: Long = 10, orderBy: Int = 1): Page[UserStatus] = {
    val offest = pageSize * pageNo

    DB.withConnection(dataSource) {
      implicit connection =>

        val userStatuses = SQL(
          """
            select * from `user_status`
              where `user_status`.`from_user_id` in ({userIds}) and `deleted` = 0
              order by {orderBy}
              limit {pageSize} offset {offset}
          """
        ).on(
          'pageSize -> pageSize,
          'offset -> offest,
          'userIds -> toUserIds.mkString(","),
          'orderBy -> orderBy
        ).as(UserStatusDao.simple *)

        val totalRows = SQL(
          """
            select count(*) from `user_status`
              where `user_status`.`from_user_id` in ({userIds}) and `deleted` = 0
          """
        ).on(
          'userIds -> toUserIds.mkString(",")
        ).as(scalar[Long].single)

        Page(userStatuses, pageNo, offest, totalRows)
    }
  }

  def update(userStatus: UserStatus) = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL(
          """
            update `user_status`
              set
                `from_user_id` = {from_user_id},
                `status_type` = {status_type},
                `status` = {status},
                `create_date` = {create_date},
                `update_date` = {update_date},
                `version` = `version` + 1,
                `deleted` = {deleted}
            where `id` = {id} and `deleted` = 0 and `version` = {version}
          """
        ).on(
          'id -> userStatus.id,
          'from_user_id -> userStatus.fromUserId,
          'status_type -> userStatus.statusType,
          'status -> userStatus.status,
          'create_date -> userStatus.createDate,
          'update_date -> userStatus.updateDate,
          'version -> userStatus.version,
          'deleted -> userStatus.deleted
        ).executeUpdate()
    }
  }

  def insert(userStatus: UserStatus) = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL(
          """
            insert into `user_status`(id, from_user_id, status_type, status, create_date, update_date, version, deleted) values (
              {id}, {from_user_id}, {status_type}, {status}, {create_date}, {update_date}, {version}, {deleted}
            )
          """
        ).on(
          'id -> userStatus.id,
          'from_user_id -> userStatus.fromUserId,
          'status_type -> userStatus.statusType,
          'status -> userStatus.status,
          'create_date -> userStatus.createDate,
          'update_date -> userStatus.updateDate,
          'version -> userStatus.version,
          'deleted -> userStatus.deleted
        ).executeUpdate()
    }
  }


  def delete(id: String) = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL("delete from `user_status` where id = {id}").on('id -> id).executeUpdate()
    }
  }

}

