package com.github.j5ik2o.messagecast.domain

import org.sisioh.dddbase.core.Repository
import scalaz.Identity
import com.github.j5ik2o.messagecast.infrastructure.model.{UserStatus => InfraUserStatus, UserStatusToUser, UserStatusToUserDao, UserStatusDao, Page}
import org.sisioh.dddbase.core.EntityNotFoundException
import anorm.Id
import java.util

trait UserStatusRepository extends Repository[UserStatus, util.UUID] {

  def findByFromUserIds(ids: Set[Identity[util.UUID]], page: Int, pageSize: Int): Page[UserStatus]

}

object UserStatusRepository {

  def apply(): UserStatusRepository = new DefaultUserStatusRepository()

}

private[domain] class DefaultUserStatusRepository extends UserStatusRepository {

  private val userStatusDao = UserStatusDao()
  private val userStatusToUserDao = UserStatusToUserDao()

  def store(entity: UserStatus) {
    userStatusDao.findById(entity.identity.value.toString).map {
      e =>
        userStatusDao.update(
          InfraUserStatus(
            id = Id(e.id.get),
            fromUserId = entity.fromUserId.value.toString,
            statusType = entity.statusType.id,
            status = entity.status,
            createDate = entity.createDate,
            updateDate = entity.updateDate,
            version = entity.version,
            deleted = entity.deleted
          )
        )
        entity.toUserIds.foreach {
          toUserId =>
            userStatusToUserDao.update(
              UserStatusToUser(
                id = Id(entity.identity.value + ":" + toUserId.value),
                userStatusId = entity.identity.value.toString,
                toUserId = toUserId.value.toString,
                createDate = entity.createDate,
                updateDate = entity.updateDate,
                version = entity.version,
                deleted = entity.deleted
              )
            )
        }
    }.getOrElse {
      userStatusDao.insert(
        InfraUserStatus(
          id = Id(entity.identity.value.toString),
          fromUserId = entity.fromUserId.value.toString,
          statusType = entity.statusType.id,
          status = entity.status,
          createDate = entity.createDate,
          updateDate = entity.updateDate,
          version = entity.version,
          deleted = entity.deleted
        )
      )
      entity.toUserIds.foreach {
        toUserId =>
          userStatusToUserDao.insert(
            UserStatusToUser(
              id = Id(entity.identity.value + ":" + toUserId.value),
              userStatusId = entity.identity.value.toString,
              toUserId = toUserId.value.toString,
              createDate = entity.createDate,
              updateDate = entity.updateDate,
              version = entity.version,
              deleted = entity.deleted
            )
          )
      }
    }
  }

  def delete(identity: Identity[util.UUID]) {
    userStatusDao.delete(identity.value.toString)
  }

  def delete(entity: UserStatus) {

  }

  def resolve(identifier: Identity[util.UUID]) =
    resolveOption(identifier).getOrElse(throw new EntityNotFoundException)

  def resolveOption(identity: Identity[util.UUID]) = {
    userStatusDao.findById(identity.value.toString).map {
      e =>
        val toUserIds = userStatusToUserDao.findByUserStatusId(Set(e.id.get)).
          map(e => Identity(util.UUID.fromString(e.toUserId)))
        UserStatus(
          identity = Identity(util.UUID.fromString(e.id.get)),
          fromUserId = Identity(util.UUID.fromString(e.fromUserId)),
          toUserIds = toUserIds,
          statusType = StatusType(e.statusType),
          status = e.status,
          createDate = e.createDate,
          updateDate = e.updateDate,
          version = e.version,
          deleted = e.deleted
        )
    }
  }

  def findByFromUserIds(ids: Set[Identity[util.UUID]], page: Int, pageSize: Int) = {
    val _page = userStatusDao.findByFromUserIds(ids.map(_.value.toString), page, pageSize, 1)
    val items = _page.items.map {
      e =>
        val toUserIds = userStatusToUserDao.findByUserStatusId(Set(e.id.get)).
          map(e => Identity(util.UUID.fromString(e.toUserId)))
        UserStatus(
          identity = Identity(util.UUID.fromString(e.id.get)),
          fromUserId = Identity(util.UUID.fromString(e.fromUserId)),
          toUserIds = toUserIds,
          statusType = StatusType(e.statusType),
          status = e.status,
          createDate = e.createDate,
          updateDate = e.updateDate,
          version = e.version,
          deleted = e.deleted
        )
    }
    Page(items, _page.page, _page.offset, _page.total)
  }


  def contains(identifier: Identity[util.UUID]) = resolveOption(identifier).isDefined

  def contains(entity: UserStatus) = contains(entity.identity)
}