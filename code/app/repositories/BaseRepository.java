package repositories;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;


public abstract class BaseRepository {

    protected DSLContext create;

    public void setConfiguration(Configuration configuration) {
        create = DSL.using(configuration);
    }
}
