package com.dev.safranys.repositories;

import com.dev.safranys.modeles.User;
import reactor.core.publisher.Mono;

public interface CustomUserRepository {
    Mono<User> saveUser(User user);
}

