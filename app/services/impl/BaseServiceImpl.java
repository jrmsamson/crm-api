package services.impl;

import org.jooq.DSLContext;
import repositories.RepositoryFactory;
import services.BaseService;

public class BaseServiceImpl implements BaseService {

    protected RepositoryFactory repositoryFactory;
    protected Long currentUserId;

    public BaseServiceImpl(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    public void setTransaction(DSLContext dslContext) {
        this.repositoryFactory.setDslContext(dslContext);
    }

    public void setCurrentUserId(Long id) {
        this.currentUserId = id;
    }
}
