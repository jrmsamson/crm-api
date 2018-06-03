package controllers;

import exceptions.TransactionException;
import org.jooq.DSLContext;
import play.Logger;
import play.mvc.Controller;
import services.BaseService;
import util.Constants;

public abstract class BaseController extends Controller {

    protected DSLContext dslContext;

    public BaseController() {
        this.dslContext = (DSLContext) ctx().args.get(Constants.REQUEST_TRANSACTION_DSL_CONTEXT);

        if (this.dslContext == null) {
            throw new TransactionException("Error trying to get the transaction configuration!");
        } else {
            Logger.debug("The transaction configuration has been set correctly");
        }
    }

    @SafeVarargs
    protected final <T extends BaseService> void init(T... services) {
        for (T service : services) {
            service.init(dslContext);
        }
    }

}
