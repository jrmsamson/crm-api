package controllers;

import exceptions.TransactionConfigurationException;
import org.jooq.DSLContext;
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

        if (this.dslContext == null)
            throw new TransactionConfigurationException();
    }

    private void getCurrentUserIdFromTheRequestContext() {
        this.currentUserId = (Long) ctx().args.get(Constants.REQUEST_CONTEXT_USER_ID);
    }


    @SafeVarargs
    protected final <T extends BaseService> void init(T... services) {
        for (T service : services) {
            service.setTransaction(dslContext);
            service.setCurrentUserId(currentUserId);
        }
    }

}
