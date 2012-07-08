package com.github.j5ik2o.messagecast.domain

import org.sisioh.dddbase.core.{EntityNotFoundException, Repository}
import scalaz.Identity
import com.github.j5ik2o.messagecast.infrastructure.model.{Page, UserFollowDao}
import anorm.Id

import java.util

trait UserFollowRepository extends Repository[UserFollow, util.UUID] {

  def findByFromUserId(userId: Set[Identity[util.UUID]], pageNo: Int, pageSize: Int): Page[UserFollow]

  def findByToUserId(userId: Set[Identity[util.UUID]], pageNo: Int, pageSize: Int): Page[UserFollow]

  def findByFromUserIdWithToUserId(fromUserId: Identity[util.UUID], toUserId: Identity[util.UUID]): Option[UserFollow]

}

object UserFollowRepository {

  def apply(): UserFollowRepository = new DefaultUserFollowRepository

}

private[domain] class DefaultUserFollowRepository extends UserFollowRepository {

  def findByFromUserIdWithToUserId(fromUserId: Identity[util.UUID], toUserId: Identity[util.UUID]): Option[UserFollow] = {
    UserFollowDao().findByFromUserIdWithToUserId(fromUserId.value.toString, toUserId.value.toString).map {
      e =>
        UserFollow(
          Identity(util.UUID.fromString(e.id.get)),
          Identity(util.UUID.fromString(e.fromUserId)),
          Identity(util.UUID.fromString(e.toUserId)),
          e.createDate,
          e.updateDate,
          e.version
        )
    }
  }


  def store(userFollow: UserFollow) {
    import com.github.j5ik2o.messagecast.infrastructure.model.{UserFollow => InfraUserFollow}
    UserFollowDao().findById(userFollow.identity.value.toString).map {
      e =>
        UserFollowDao().update(
          InfraUserFollow(
            Id(userFollow.identity.value.toString),
            userFollow.fromUserId.value.toString,
            userFollow.toUserId.value.toString,
            userFollow.createDate,
            userFollow.updateDate,
            userFollow.version,
            userFollow.deleted
          )
        )
    }.getOrElse {
      UserFollowDao().insert(
        InfraUserFollow(
          Id(userFollow.identity.value.toString),
          userFollow.fromUserId.value.toString,
          userFollow.toUserId.value.toString,
          userFollow.createDate,
          userFollow.updateDate,
          userFollow.version,
          userFollow.deleted
        )
      )
    }
  }

    def delete(identity: Identity[util.UUID]) {
    UserFollowDao().delete(identity.value.toString)
  }

  def delete(entity: UserFollow) {
    delete(entity.identity)
  }

  def resolve(identifier: Identity[util.UUID]) =
    resolveOption(identifier).getOrElse(throw new EntityNotFoundException)

  def resolveOption(identifier: Identity[util.UUID]): Option[UserFollow] = {
    UserFollowDao().findById(identifier.value.toString).map {
      e =>
        UserFollow(
          Identity(util.UUID.fromString(e.id.get)),
          Identity(util.UUID.fromString(e.fromUserId)),
          Identity(util.UUID.fromString(e.toUserId)),
          e.createDate,
          e.updateDate,
          e.version
        )
    }
  }

  def contains(identifier: Identity[util.UUID]) = resolveOption(identifier).isDefined

  def contains(entity: UserFollow) = contains(entity.identity)

  def findByFromUserId(userIds: Set[Identity[util.UUID]], pageNo: Int, pageSize: Int) = {
    val page = UserFollowDao().findByFromUserId(userIds.map(_.value.toString), pageNo, pageSize)
    val items = page.items.map {
      e =>
        UserFollow(
          Identity(util.UUID.fromString(e.id.get)),
          Identity(util.UUID.fromString(e.fromUserId)),
          Identity(util.UUID.fromString(e.toUserId)),
          e.createDate,
          e.updateDate,
          e.version)
    }.toList
    Page(items, page.page, page.offset, page.total)
  }

  def findByToUserId(userIds: Set[Identity[util.UUID]], pageNo: Int, pageSize: Int) = {
    val page = UserFollowDao().findByToUserId(userIds.map(_.value.toString), pageNo, pageSize)
    val items = page.items.map {
      e =>
        UserFollow(
          Identity(util.UUID.fromString(e.id.get)),
          Identity(util.UUID.fromString(e.fromUserId)),
          Identity(util.UUID.fromString(e.toUserId)),
          e.createDate,
          e.updateDate,
          e.version)
    }.toList
    Page(items, page.page, page.offset, page.total)
  }

}
