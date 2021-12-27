package com.solr.clientwrapper.usecase.authority;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.solr.clientwrapper.domain.dto.AdminUserDTO;
import com.solr.clientwrapper.domain.port.api.AuthorityServicePort;
import com.solr.clientwrapper.domain.port.spi.UserPersistencPort;
import com.solr.clientwrapper.infrastructure.entity.User;
import com.solr.clientwrapper.mapper.UserMapper;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ReadAuthorityTest {
	
	private static final String DEFAULT_LOGIN = "johndoe";
	
    private UserMapper userMapper;
    private User user;
    private AdminUserDTO userDto;
    
//    @Autowired
//    @MockBean
//    private UserServicePort userServicePort;
    
    @Autowired
    @MockBean
    private AuthorityServicePort authorityServicePort;
    
    @MockBean
    private UserPersistencPort userPersistencePort;
    
//    @MockBean
//    private Pageable pageable;
    
    @InjectMocks
    private ReadAuthority readAuth;

	@BeforeEach
    public void init() {
        userMapper = new UserMapper();
        user = new User();
        user.setLogin(DEFAULT_LOGIN);
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail("johndoe@localhost");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setImageUrl("image_url");
        user.setLangKey("en");

        userDto = new AdminUserDTO(user);
        readAuth = new ReadAuthority(authorityServicePort);
    }
    
	@Test
	void contextLoads() {
		assertThat(authorityServicePort).isNotNull();
	}
	
	@Test
	void readAuthoritiesTest() {
		List<String> authorities = new ArrayList<String>();
		
		Mockito.when(authorityServicePort.getAuthorities().size() > 0)
				.thenReturn(null);
		
		authorities = readAuth.getAuthorities();
		// testing
		// System.out.println("Auths: "+authorities);
		
		assertNull(authorities);
	}
    
}
