package json

import play.api.libs.json._
import com.github.j5ik2o.messagecast.domain.{UserFollow, User, UserStatus}
import play.api.libs.json.JsString
import play.api.libs.json.JsObject
import com.github.j5ik2o.messagecast.infrastructure.model.Page

object Formats {

  class PageJsonFormat[A](implicit fjs: Writes[A]) extends Writes[Page[A]] {

    def writes(o: Page[A]) = {
      require(o != null)
      JsObject(
        Seq(
          "page" -> JsNumber(o.page),
          "next" -> o.next.map(e => JsNumber(e)).getOrElse(JsNull),
          "prev" -> o.prev.map(e => JsNumber(e)).getOrElse(JsNull),
          "offset" -> JsNumber(o.offset),
          "total" -> JsNumber(o.total),
          "items" -> Json.toJson(o.items)
        )
      )
    }
  }


  implicit object UserStatusJsonFormat extends Writes[UserStatus] {
    def writes(o: UserStatus) = {
      JsObject(
        Seq(
          "id" -> JsString(o.identity.value.toString),
          "fromUserId" -> JsString(o.fromUserId.value.toString),
          "toUserIds" -> JsArray(o.toUserIds.map(e => JsString(e.value.toString))),
          "statusType" -> JsNumber(o.statusType.id),
          "status" -> JsString(o.status),
          "createDate" -> JsString(o.createDate.toString),
          "updateDate" -> JsString(o.updateDate.toString)
        )
      )
    }
  }

  implicit object UserStatusPageJsonFormat extends PageJsonFormat[UserStatus]

  implicit object UserFollowJsonFormat extends Writes[UserFollow] {
    def writes(o: UserFollow) = {
      JsObject(
        Seq(
          "id" -> JsString(o.identity.value.toString),
          "fromUserId" -> JsString(o.fromUserId.value.toString),
          "toUserId" -> JsString(o.toUserId.value.toString),
          "createDate" -> JsString(o.createDate.toString),
          "updateDate" -> JsString(o.updateDate.toString)
        )
      )
    }
  }

  implicit object UserFollowPageJsonFormat extends PageJsonFormat[UserFollow]

  implicit object UserJsonFormat extends Writes[User] {
    def writes(o: User) = {
      JsObject(
        Seq(
          "id" -> JsString(o.identity.value.toString),
          "name" -> JsString(o.name),
          "password" -> JsString(o.password),
          "createDate" -> JsString(o.createDate.toString),
          "updateDate" -> JsString(o.updateDate.toString)
        )
      )
    }
  }

  implicit object UserPageJsonFormat extends PageJsonFormat[User]


}
