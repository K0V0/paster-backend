package com.kovospace.paster.user.integration;

import com.kovospace.paster.KovoTest;
import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

public class UserControllerProfileTest extends KovoTest {

    protected String jwtToken;
    protected User user;

    @Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected JwtService jwtService;

    @Override
    protected String getEndpoint() { return "/user/profile"; }

    @Override
    protected String getApiPrefix() { return "/api/v1"; }

    @Transactional
    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        user = new User();
        user.setName("Anatoli Datlov");
        user.setEmail("datlov@chnpp.cccp");
        user.setPasword(bCryptPasswordEncoder.encode("AZ-5"));
        userRepository.save(user);
        user.setJwtToken(jwtService.generate(user));
        this.jwtToken = jwtService.getPrefix() + " " + user.getJwtToken();
    }
}
