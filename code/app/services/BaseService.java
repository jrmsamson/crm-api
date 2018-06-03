package services;

import org.jooq.DSLContext;

public interface BaseService {

    public void init(DSLContext dslContext);
}
