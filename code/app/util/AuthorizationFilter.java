package util;

import akka.stream.Materializer;
import play.Logger;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class AuthorizationFilter extends Filter {

    @Inject
    public AuthorizationFilter(Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> request, Http.RequestHeader rh) {
        Logger.info("APPLY");


        rh.cookies().forEach(cookie -> Logger.info("COOKIE " + cookie.name()));
        return request.apply(rh).thenApply(result -> {
            return result;
        });
    }
}
