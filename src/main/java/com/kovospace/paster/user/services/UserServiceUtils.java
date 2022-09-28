package com.kovospace.paster.user.services;

import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;

public class UserServiceUtils {

    public static final User getUser(UserRepository userRepository, String identification) {
        User user = userRepository.getFirstByName(identification);
        if (user != null) { return user; }
        user = userRepository.getFirstByEmail(identification);
        return user;
    }

}
