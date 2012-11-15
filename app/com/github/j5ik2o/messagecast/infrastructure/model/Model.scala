package com.github.j5ik2o.messagecast.infrastructure.model

import anorm.{NotAssigned, Pk}
import java.util

case class Page[A](items: Seq[A], page: Long, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

case class User(id: Pk[String] = NotAssigned,
                name: String,
                password: String,
                bio: String,
                createDate: util.Date,
                updateDate: util.Date,
                version: Long,
                deleted: Boolean = false)

case class UserFollow(id: Pk[String] = NotAssigned,
                      fromUserId: String,
                      toUserId: String,
                      createDate: util.Date,
                      updateDate: util.Date,
                      version: Long,
                      deleted: Boolean = false)

case class UserStatus(id: Pk[String] = NotAssigned,
                      fromUserId: String,
                      statusType: Int,
                      status: String,
                      createDate: util.Date,
                      updateDate: util.Date,
                      version: Long,
                      deleted: Boolean = false)

case class UserStatusToUser(id: Pk[String] = NotAssigned,
                            userStatusId: String,
                            toUserId: String,
                            createDate: util.Date,
                            updateDate: util.Date,
                            version: Long,
                            deleted: Boolean = false)
