package controllers

import play.api.mvc._

trait ControllerSupport {
  this: Controller =>

  def AuthAction(f: Request[AnyContent] => Result): Action[AnyContent] = {
    Action {
      request =>
        request.session.get("userId") match {
          case None => Unauthorized("NEED AUTH")
          case Some(v) => f(request)
        }
    }
  }

}
