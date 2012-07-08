package com.github.j5ik2o.messagecast.domain

import org.sisioh.dddbase.core.{EntityNotFoundException, Repository}
import scalaz.Identity
import com.github.j5ik2o.messagecast.infrastructure.model.{Page, UserDao}
import anorm.Id
import java.util

trait UserRepository extends Repository[User, util.UUID] {

  def findAll(pageNo: Long, pageSize: Long): Page[User]

}

object UserRepository {

  def apply(): UserRepository = new DefaultUserRepository

}

private[domain] class DefaultUserRepository extends UserRepository {

  private val userDao = newUserDao

  protected def newUserDao: UserDao = UserDao()

  def store(entity: User) {
    userDao.findById(entity.identity.value.toString).map {
      e =>
        import com.github.j5ik2o.messagecast.infrastructure.model.{User => InfraUser}
        val user = InfraUser(
          id = e.id,
          name = entity.name,
          password = entity.password,
          createDate = entity.createDate,
          updateDate = entity.updateDate,
          version = entity.version
        )
        userDao.update(user)
    }.getOrElse {
      import com.github.j5ik2o.messagecast.infrastructure.model.{User => InfraUser}
      val user = InfraUser(
        id = Id(entity.identity.value.toString),
        name = entity.name,
        password = entity.password,
        createDate = entity.createDate,
        updateDate = entity.updateDate,
        version = entity.version
      )
      userDao.insert(user)
    }
  }

  def delete(identity: Identity[util.UUID]) {
    resolveOption(identity).foreach {
      delete(_)
    }
  }

  def delete(entity: User) {
    userDao.delete(entity.identity.value.toString)
  }

  def resolve(identifier: Identity[util.UUID]) = resolveOption(identifier).
    getOrElse(throw new EntityNotFoundException())

  def resolveOption(identifier: Identity[util.UUID]) = {
    userDao.findById(identifier.value.toString).map {
      e =>
        val id = Identity(util.UUID.fromString(e.id.get))
        User(id, e.name, e.password, e.createDate, e.updateDate, e.version)
    }
  }

  def contains(identifier: Identity[util.UUID]) = resolveOption(identifier).isDefined

  def contains(entity: User) = contains(entity.identity)

  def findAll(pageNo: Long, pageSize: Long) = {
    val page = userDao.findAll(pageNo, pageSize)
    val items = page.items.map{
      e =>
        val id = Identity(util.UUID.fromString(e.id.get))
        User(id, e.name, e.password, e.createDate, e.updateDate, e.version)
    }
    Page(items, page.page, page.offset, page.total)
  }
}
