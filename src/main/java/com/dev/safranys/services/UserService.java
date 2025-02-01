package com.dev.safranys.services;

import com.dev.safranys.dtos.UserDto;
import com.dev.safranys.exceptions.UserNotFoundException;
import com.dev.safranys.modeles.User;
import com.dev.safranys.repositories.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Save user
     */
    public Mono<User> saveUser(UserDto userDto) {
        User newUser = new User(
                null,                 // let the record generate a UUID if null
                userDto.email(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.age(),
                userDto.city()
        );
        return userRepository.saveUser(newUser);
    }

    /**
     * Updates the existing User with new values (full update).
     */
    public Mono<User> updateUser(String userId, UserDto userDto) {
        return userRepository.findById(userId)
                .flatMap(existingUser -> {
                    User updatedUser = new User(
                            existingUser.id(),
                            userDto.email(),
                            userDto.firstName(),
                            userDto.lastName(),
                            userDto.age(),
                            userDto.city()
                    );
                    return userRepository.save(updatedUser);
                });
    }

    public Mono<Void> deleteUserById(String userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("No user found for id: " + userId)))
                .flatMap(userRepository::delete);
    }

    public Flux<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public Mono<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Mono<List<String>> getUserIds() {
        return userRepository.getUserIds()
                .collectList();
    }
}
