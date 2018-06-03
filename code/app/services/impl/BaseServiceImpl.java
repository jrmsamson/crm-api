package services.impl;

import org.jooq.DSLContext;
import services.BaseService;

public class BaseServiceImpl implements BaseService {

    protected DSLContext dslContext;

    public void init(DSLContext dslContext) {
        this.dslContext = dslContext;
    }
}
