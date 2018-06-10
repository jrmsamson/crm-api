package controllers;

import play.mvc.Result;

import static play.mvc.Results.ok;

public class HomeController {

    public Result welcome() {
        return ok("Congrats, everything is setup correctly");
    }
}
