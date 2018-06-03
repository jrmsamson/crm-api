package repositories.impl;

import org.jooq.DSLContext;
import repositories.BaseRepository;

public class BaseRepositoryImpl implements BaseRepository {

    protected DSLContext create;

    public void setDslContext(DSLContext dslContext) {
        this.create = dslContext;
    }
}
