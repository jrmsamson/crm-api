package unit.services;

import enums.Role;
import org.junit.Test;
import repositories.RepositoryFactory;
import repositories.RoleRepository;
import services.RoleService;
import services.impl.RoleServiceImpl;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RoleServiceTest {

    private RoleRepository roleRepository;
    private RoleService roleService;

    public RoleServiceTest() {
        roleRepository = mock(RoleRepository.class);
        RepositoryFactory repositoryFactory = mock(RepositoryFactory.class);
        when(repositoryFactory.getRoleRepository()).thenReturn(roleRepository);
        roleService = new RoleServiceImpl(repositoryFactory);
    }

    @Test
    public void shouldGetRoleId() {
        when(roleRepository.getRoleId(Role.USER)).thenReturn(Optional.of(1));
        roleService.getRoleId(Role.USER);
        verify(roleRepository).getRoleId(Role.USER);
    }
}
