package services.impl;

import org.jooq.DSLContext;
import repositories.RepositoryFactory;
import services.BaseService;

public class BaseServiceImpl implements BaseService {

    protected RepositoryFactory repositoryFactory;

    public BaseServiceImpl(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    public void init(DSLContext dslContext) {
        this.repositoryFactory.setDslContext(dslContext);
    }
}
