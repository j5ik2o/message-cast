package com.github.j5ik2o.messagecast.domain

import org.sisioh.dddbase.core.Entity
import java.util
import scalaz.Identity

trait UserFollow extends Entity[util.UUID] {

  val fromUserId: Identity[util.UUID]
  val toUserId: Identity[util.UUID]
  val createDate: util.Date
  val updateDate: util.Date
  val version: Long
  val deleted: Boolean

}

object UserFollow {

  def apply(identity: Identity[util.UUID],
            fromUserId: Identity[util.UUID],
            toUserId: Identity[util.UUID],
            createDate: util.Date = new util.Date(),
            updateDate: util.Date = new util.Date(),
            version: Long = 0,
            deleted: Boolean = false) =
    new DefaultUserFollow(identity, fromUserId, toUserId, createDate, updateDate, version, deleted)

}

private[domain] class DefaultUserFollow
(val identity: Identity[util.UUID],
 val fromUserId: Identity[util.UUID],
 val toUserId: Identity[util.UUID],
 val createDate: util.Date,
 val updateDate: util.Date,
 val version: Long,
 val deleted: Boolean) extends UserFollow {

}
