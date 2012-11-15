package controllers

import play.api.mvc._
import scalaz.Identity
import play.api.libs.json.Json

import json.Formats._
import play.api.data._
import play.api.data.Forms._

import java.util

import com.github.j5ik2o.messagecast.domain.{StatusType, UserStatus, UserStatusRepository}

object UserStatusApiController extends UserStatusApiController

class UserStatusApiController extends Controller with ControllerSupport {

  private val userStatusRepository = newUserStatusRepository

  protected def newUserStatusRepository = UserStatusRepository()

  def getUserStatusById(id: String) = Action {
    userStatusRepository.resolveOption(Identity(util.UUID.fromString(id))).map(e => Ok(Json.toJson(e))).getOrElse(NotFound)
  }

  def getUserStatusByUserId(userId: String, pageNo: Int, pageSize: Int) = Action {
    val page = userStatusRepository.
      findByFromUserIds(Set(Identity(util.UUID.fromString(userId))), pageNo, pageSize)
    Ok(Json.toJson(page))
  }

  private val userStatusForm = Form(
    tuple(
      "toUserIds" -> list(text),
      "statusType" -> optional(number),
      "status" -> nonEmptyText
    )
  )

  def addUserStatus() = AuthAction {
    implicit request =>
      userStatusForm.bindFromRequest.fold(
        formWithErrors => BadRequest,
        form => {
          val fromUserId = session.get("userId").get
          val (toUserIds, statusType, status) = form
          userStatusRepository.store(
            UserStatus(
              identity = Identity(util.UUID.randomUUID()),
              fromUserId = Identity(util.UUID.fromString(fromUserId)),
              toUserIds = toUserIds.map(e => Identity(util.UUID.fromString(e.toString))),
              statusType = StatusType(statusType.getOrElse(0)),
              status = status
            )
          )
          Ok
        }
      )
  }

  def updateUserStatus(userStatusId: String) = AuthAction {
    implicit request =>
      userStatusForm.bindFromRequest.fold(
        formWithErrors => BadRequest,
        form => {
          val fromUserId = session.get("userId").get
          val (toUserIds, statusType, status) = form
          userStatusRepository.store(
            UserStatus(
              identity = Identity(util.UUID.fromString(userStatusId)),
              fromUserId = Identity(util.UUID.fromString(fromUserId)),
              toUserIds = toUserIds.map(e => Identity(util.UUID.fromString(e.toString))),
              statusType = StatusType(statusType.getOrElse(0)),
              status = status
            )
          )
          Ok
        }
      )
  }

  def removeUserStatus(userStatusId: String) = AuthAction {
    request =>
      userStatusRepository.delete(Identity(util.UUID.fromString(userStatusId)))
      Ok
  }

}

