package repositories;

import org.jooq.DSLContext;

public interface BaseRepository {

    void setDslContext(DSLContext dslContext);

}
