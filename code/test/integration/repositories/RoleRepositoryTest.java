package integration.repositories;

import model.pojos.Role;
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

public class RoleRepositoryTest {

    private static final String ADMIN_ROLE = "Admin";
    private static final String USER_ROLE = "User";

    private Database database;
    private RoleRepository roleRepository;

    public RoleRepositoryTest() {
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

    @After
    public void tearDown() {
        Evolutions.cleanupEvolutions(database);
        this.database.shutdown();
    }

    @Test
    public void shouldGetRoleById() {
        Optional<Role> roleId = roleRepository.getById(1);
        assertTrue(roleId.isPresent());
    }

    @Test

    public void shouldGetRoleByName() {
        Optional<Role> adminRole = roleRepository.getByName(ADMIN_ROLE);
        Optional<Role> userRole = roleRepository.getByName(USER_ROLE);
        assertTrue(adminRole.isPresent());
        assertTrue(userRole.isPresent());
    }
}
