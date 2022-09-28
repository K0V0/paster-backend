package com.kovospace.paster.user.integration;

import com.kovospace.paster.KovoTest;

public class UserControllerChangePassTest extends KovoTest {

    @Override
    protected String getEndpoint() { return "/user/login"; }

    @Override
    protected String getApiPrefix() { return "/api/v1"; }

}
