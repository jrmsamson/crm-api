package services;

import org.jooq.DSLContext;

public interface BaseService {

    void setTransaction(DSLContext dslContext);
    void setCurrentUserId(Long id);
}
