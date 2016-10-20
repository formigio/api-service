package io.commitr.idenity;

import io.commitr.identity.Identity;
import io.commitr.identity.IdentityRepository;
import io.commitr.identity.IdentityService;
import io.commitr.identity.IdentityServiceImpl;
import io.commitr.team.TeamRepository;
import io.commitr.team.TeamService;
import io.commitr.team.TeamServiceImpl;
import io.commitr.util.DTOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Created by peterfdouglas on 10/19/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser
public class IdentityServiceTest {

    @MockBean
    IdentityRepository repository;

    @Autowired
    IdentityService service;

    @Before
    public void setUp() throws Exception {
        given(repository.save(Identity.of(null, "user", null)))
                .willReturn(Identity.of(null, "user", DTOUtils.VALID_UUID));

    }

    @Test
    public void testMeAuthenticatedNewUser() throws Exception {

        given(repository.findByPrincipleId("user"))
                .willReturn(null);

        Identity i = service.getIdentity();

        assertThat(i).isNotNull();
        verify(repository, times(1)).findByPrincipleId("user");
        verify(repository, times(1)).save(Identity.of(null, "user", null));
    }

    @Test
    public void testMeAuthenticatedExistingUser() throws Exception {
        given(repository.findByPrincipleId("user"))
                .willReturn(Identity.of(null, "user", DTOUtils.VALID_UUID));

        Identity i = service.getIdentity();

        assertThat(i).isNotNull();
        assertThat(i.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        verify(repository, times(1)).findByPrincipleId("user");
        verify(repository, times(0)).save(Identity.of(null, "user", null));
    }

    @Test
    @WithAnonymousUser
    public void testMeAnonymous() throws Exception {
        Identity i = service.getIdentity();


    }

    @Configuration
    static class ServiceConfig {
        @Mock
        IdentityRepository repository;

        @Primary
        @Bean
        public IdentityRepository identityRepository() {
            return repository;
        }

        @Bean
        public IdentityService identityService() { return new IdentityServiceImpl();
        }
    }
}
