package com.github.j5ik2o.messagecast.domain

import org.sisioh.dddbase.core.Entity
import scalaz.Identity
import java.util
import com.github.j5ik2o.messagecast.infrastructure.model.Page

trait User extends Entity[util.UUID] {

  val name: String

  val password: String

  val bio: String

  val createDate: util.Date

  val updateDate: util.Date

  val version: Long
}

object User {

  def apply(identity: Identity[util.UUID],
            name: String,
            password: String,
            bio: String,
            createDate: util.Date = new util.Date(),
            updateDate: util.Date = new util.Date(),
            version: Long = 0,
            deleted: Boolean = false) =
    new DefaultUser(identity, name, password, bio, createDate, updateDate, version, deleted)

  def unapply(user: DefaultUser): Option[(Identity[util.UUID], String, String)] =
    Some(user.identity, user.name, user.password)

}


private[domain] class DefaultUser
(val identity: Identity[util.UUID],
 val name: String,
 val password: String,
 val bio: String,
 val createDate: util.Date,
 val updateDate: util.Date,
 val version: Long,
 val deleted: Boolean) extends User {

  override def toString = "DefaultUser(%s)".format(identity)


}
