package com.github.j5ik2o.messagecast.infrastructure.model

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util

object UserDao {

  val simple = {
    get[Pk[String]]("user.id") ~
      get[String]("user.name") ~
      get[String]("user.password") ~
      get[util.Date]("user.create_date") ~
      get[util.Date]("user.update_date") ~
      get[Long]("user.version") ~
      get[Boolean]("user.deleted") map {
      case id ~ name ~ password ~ createDate ~ updateDate ~ version ~ deleted =>
        User(id, name, password, createDate, updateDate, version, deleted)
    }
  }

  def apply(dataSource: String = "default") = new UserDao(dataSource)

}

class UserDao(dataSource: String) {

  def findByName(name: String): Option[User] = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL("select id, name, password, create_date, update_date, version, deleted from `user` where `name` = {name}").
          on('name -> name).as(UserDao.simple.singleOpt)
    }
  }

  def findById(id: String): Option[User] = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL("select id, name, password, create_date, update_date, version, deleted from `user` where `id` = {id}").
          on('id -> id).as(UserDao.simple.singleOpt)
    }
  }

  def findAll(pageNo: Long, pageSize: Long): Page[User] = {
    DB.withConnection(dataSource) {
      implicit connection =>
        val offset = pageNo * pageSize
        val users = SQL(
          """
          select
            id, name, password, create_date, update_date, version, deleted from `user`
          order by {orderBy}
          limit {pageSize} offset {offset}
          """).
          on(
          'offset -> offset,
          'pageSize -> pageSize,
          'orderBy -> 1
        ).as(UserDao.simple *)

        val totalRows = SQL(
          """
            select count(*) from `user`
          """
        ).as(scalar[Long].single)
        Page(users, pageNo, offset, totalRows)
    }
  }

  def update(user: User) = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL(
          """
            update `user`
              set `name` = {name},
                `create_date` = {create_date},
                `update_date` = {update_date},
                `version` = `version` + 1,
                `deleted` = {deleted}
            where `id` = {id} and `version` = {version}
          """
        ).on(
          'id -> user.id,
          'name -> user.name,
          'create_date -> user.createDate,
          'update_date -> user.updateDate,
          'version -> user.version,
          'deleted -> user.deleted
        ).executeUpdate()
    }
  }

  def insert(user: User) = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL(
          """
            insert into `user`(id, name, password, create_date, update_date, version, deleted) values (
              {id}, {name}, {password}, {create_date}, {update_date}, 0, 0
            )
          """
        ).on(
          'id -> user.id,
          'name -> user.name,
          'password -> user.password,
          'create_date -> user.createDate,
          'update_date -> user.updateDate
        ).executeUpdate()
    }
  }

  def delete(id: String) = {
    DB.withConnection(dataSource) {
      implicit connection =>
        SQL("delete from `user` where id = {id}").
          on('id -> id).executeUpdate()
    }
  }

}
