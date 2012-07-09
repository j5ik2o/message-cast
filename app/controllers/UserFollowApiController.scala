package controllers

import play.api.mvc._
import scalaz.Identity
import play.api.libs.json.Json
import play.api.data._
import play.api.data.Forms._

import json.Formats._

import java.util


import com.github.j5ik2o.messagecast.domain.{UserFollow, UserFollowRepository}

object UserFollowApiController extends UserFollowApiController

class UserFollowApiController extends Controller {

  private val userFollowRepository = newUserFollowRepository

  protected def newUserFollowRepository = UserFollowRepository()

  def getUserFollowById(id: String) = Action {
    userFollowRepository.resolveOption(Identity(util.UUID.fromString(id))).
      map(e => Ok(Json.toJson(e))).getOrElse(NotFound)
  }

  def getUserFollowByFromUserId(id: String, pageNo: Int, pageSize: Int) = Action {
    val page = userFollowRepository.findByFromUserId(Set(Identity(util.UUID.fromString(id))), pageNo, pageSize)
    Ok(Json.toJson(page))
  }

  def getUserFollowByToUserId(id: String, pageNo: Int, pageSize: Int) = Action {
    val page = userFollowRepository.findByToUserId(Set(Identity(util.UUID.fromString(id))), pageNo, pageSize)
    Ok(Json.toJson(page))
  }

  /**
   *
  val fromUserId: Identity[util.UUID]
  val toUserId: Identity[util.UUID]
  val createDate: util.Date
  val updateDate: util.Date
  val version: Long
  val deleted: Boolean   *
   *
   */


  private val userFollowForm = Form(
    tuple(
      "fromUserId" -> text,
      "toUserId" -> text
    )
  )

  def addUserFollow() = Action {
    implicit request =>
      userFollowForm.bindFromRequest.fold(
        formWithErrors => BadRequest,
        form => {
          val (fromUserId, toUserId) = form
          val userFollow = UserFollow(
            identity = Identity(util.UUID.randomUUID()),
            fromUserId = Identity(util.UUID.fromString(fromUserId)),
            toUserId = Identity(util.UUID.fromString(toUserId))
          )
          userFollowRepository.store(userFollow)
          Ok
        }
      )
  }

  def updateUserFollow(id: String) = Action {
    implicit request =>
      userFollowForm.bindFromRequest.fold(
        formWithErrors => BadRequest,
        form => {
          val (fromUserId, toUserId) = form
          val userFollow = UserFollow(
            identity = Identity(util.UUID.fromString(id)),
            fromUserId = Identity(util.UUID.fromString(fromUserId)),
            toUserId = Identity(util.UUID.fromString(toUserId))
          )
          userFollowRepository.store(userFollow)
          Ok
        }
      )
  }

  def removeUserFollow(id: String) = Action {
    userFollowRepository.delete(Identity(util.UUID.fromString(id)))
    Ok
  }
}
