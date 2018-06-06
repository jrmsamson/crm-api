package integration.repositories;

import enums.Role;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import repositories.RoleRepository;
import repositories.impl.RoleRepositoryImpl;
import java.util.Optional;
import static org.junit.Assert.assertTrue;

public class RoleTest {

    private Database database;
    private RoleRepository roleRepository;

    public RoleTest() {
        Application application = new GuiceApplicationBuilder().build();
        database = application.injector().instanceOf(Database.class);
        DSLContext dslContext = DSL.using(this.database.getConnection());
        roleRepository = new RoleRepositoryImpl();
        roleRepository.setDslContext(dslContext);
    }

    @Before
    public void setUp() {
        Evolutions.applyEvolutions(database);
    }

    @Test
    public void shouldGetRoleIdByName() {
        Optional<Integer> roleId = roleRepository.getRoleId(Role.USER);
        assertTrue(roleId.isPresent());
    }

    @After
    public void tearDown() {
        Evolutions.cleanupEvolutions(database);
        this.database.shutdown();
    }
}
