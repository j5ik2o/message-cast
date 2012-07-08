package controllers

import play.api.mvc._
import scalaz.Identity
import play.api.libs.json.Json

import json.Formats._

import java.util


import com.github.j5ik2o.messagecast.domain.UserFollowRepository

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

  def addUserFollow() = Action {
    implicit request =>
      Ok
  }

  def updateUserFollow(id: String) = Action {
    Ok
  }

  def removeUserFollow(id: String) = Action {
    Ok
  }
}
