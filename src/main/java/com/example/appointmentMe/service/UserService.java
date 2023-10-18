package com.example.appointmentMe.service;

import com.example.appointmentMe.model.User;
import com.example.appointmentMe.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByNickname(String nickname) {
        return userRepository.findByTelegramNick(nickname).orElse(new User());
    }

    public boolean userExists(User user) {
        return userRepository.exists(Example.of(user));
    }

    public boolean userWithNicknameExists(String nickname) {
        return userExists(getUserByNickname(nickname));
    }

    public String getUserCityByNickname(String nickname) {
        return userRepository.findCityByTelegramNick(nickname).orElse(User.DEFAULT_CITY);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
