package controllers

import play.api.mvc._
import scalaz.Identity
import play.api.libs.json.Json
import play.api.data._
import play.api.data.Forms._

import json.Formats._

import java.util

import com.github.j5ik2o.messagecast.domain.{User, UserRepository}

object UserApiController extends UserApiController

class UserApiController extends Controller with ControllerSupport {

  private val userRepository = newUserRepository

  protected def newUserRepository = UserRepository()

  def getUserById(id: String) = Action {
    userRepository.resolveOption(Identity(util.UUID.fromString(id)))
      .map(e => Ok(Json.toJson(e))).getOrElse(NotFound)
  }

  def getUsers(pageNo: Long, pageSize: Long) = Action {
    Ok(Json.toJson(userRepository.findAll(pageNo, pageSize)))
  }

  private val userForm = Form(
    tuple(
      "name" -> nonEmptyText,
      "password" -> nonEmptyText,
      "bio" -> nonEmptyText
    )
  )

  def addUser() = AuthAction {
    implicit request =>
      userForm.bindFromRequest.fold(
        formWithErrors => BadRequest,
        form => {
          val (name, password, bio) = form
          val user = User(
            identity = Identity(util.UUID.randomUUID()),
            name = name,
            password = password,
            bio = bio
          )
          userRepository.store(user)
          Ok
        }
      )
  }

  def updateUser(userId: String) = AuthAction {
    implicit request =>
      userForm.bindFromRequest.fold(
        formWithErrors => BadRequest,
        form => {
          val (name, password, bio) = form
          val user = User(
            identity = Identity(util.UUID.fromString(userId)),
            name = name,
            password = password,
            bio = bio
          )
          userRepository.store(user)
          Ok
        }
      )
  }

  def removeUser(userId: String) = AuthAction {
    implicit request =>
      userRepository.delete(Identity(util.UUID.fromString(userId)))
      Ok
  }

}
