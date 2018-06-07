package controllers;

import exceptions.TransactionException;
import org.jooq.DSLContext;
import play.Logger;
import play.mvc.Controller;
import services.BaseService;
import util.Constants;

public abstract class BaseController extends Controller {

    protected DSLContext dslContext;
    protected Long currentUserId;

    public BaseController() {
        getDslContextFromTheRequestTransaction();
        getCurrentUserIdFromTheRequestContext();
    }

    private void getDslContextFromTheRequestTransaction() {
        this.dslContext = (DSLContext) ctx().args.get(Constants.REQUEST_TRANSACTION_DSL_CONTEXT);

        if (this.dslContext == null) {
            throw new TransactionException("Error trying to get the transaction configuration!");
        } else {
            Logger.debug("The transaction configuration has been set correctly");
        }
    }

    private void getCurrentUserIdFromTheRequestContext() {
        this.currentUserId = (Long) ctx().args.get(Constants.REQUEST_CONTEXT_USER_ID);
        Logger.info("" + currentUserId);
    }


    @SafeVarargs
    protected final <T extends BaseService> void init(T... services) {
        for (T service : services) {
            service.setTransaction(dslContext);
            service.setCurrentUserId(currentUserId);
        }
    }

}
