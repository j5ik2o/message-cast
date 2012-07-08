package com.github.j5ik2o.messagecast.domain

import org.sisioh.dddbase.core.Entity
import scalaz.Identity
import java.util

trait UserStatus extends Entity[util.UUID] {

  val fromUserId: Identity[util.UUID]

  val toUserIds: List[Identity[util.UUID]]

  val statusType: StatusType.Value

  val status: String

  val createDate: util.Date

  val updateDate: util.Date

  val version: Long

  val deleted: Boolean

}

object UserStatus {

  def apply(identity: Identity[util.UUID],
            fromUserId: Identity[util.UUID],
            toUserIds: List[Identity[util.UUID]],
            statusType: StatusType.Value,
            status: String,
            createDate: util.Date = new util.Date(),
            updateDate: util.Date = new util.Date(),
            version: Long = 0,
            deleted: Boolean = false) = new DefaultUserStatus(
    identity,
    fromUserId,
    toUserIds,
    statusType,
    status,
    createDate,
    updateDate,
    version,
    deleted
  )

}

private[domain] class DefaultUserStatus
(val identity: Identity[util.UUID],
 val fromUserId: Identity[util.UUID],
 val toUserIds: List[Identity[util.UUID]],
 val statusType: StatusType.Value,
 val status: String,
 val createDate: util.Date,
 val updateDate: util.Date,
 val version: Long,
 val deleted: Boolean)
  extends UserStatus {

}
