package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import com.github.j5ik2o.messagecast.domain.UserRepository

object AuthController extends AuthController

class AuthController extends Controller {

  private val userRepository = newUserRepository

  protected def newUserRepository = UserRepository()

  private val loginForm = Form(
    tuple(
      "name" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )

  def login = Action {
    implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => BadRequest("INVALID PARAMETERS"),
        form => {
          val (name, password) = form
          userRepository.findByName(name) match {
            case None => NotFound("NOT FOUND USER")
            case Some(user) if user.password == password =>
              Ok("LOGGED IN").withSession("userId" -> user.identity.value.toString)
            case _ => Unauthorized("INVALID PASSWORD")
          }
        }
      )
  }

  def logout = Action {
    implicit request =>
      session.get("userId") match {
        case None => NotFound("NOT FOUND USER")
        case Some(session) =>
          Ok("LOGGED OUT").withNewSession
      }
  }

}
