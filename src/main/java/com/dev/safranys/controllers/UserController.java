package com.dev.safranys.controllers;

import com.dev.safranys.dtos.UserDto;
import com.dev.safranys.exceptions.UserNotFoundException;
import com.dev.safranys.modeles.User;
import com.dev.safranys.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController() throws Exception { }

    // Create new user
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<User>> createUser(@RequestBody UserDto userDto) {
        System.out.println("add user: " + userDto);

        return userService.saveUser(userDto)
                .map(savedUser -> ResponseEntity.status(HttpStatus.CREATED).body(savedUser))
                // Optionally handle errors:
                .onErrorResume(e -> {
                    System.err.println("Error creating user: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null));
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(
            @PathVariable("id") String id,
            @Valid @RequestBody UserDto userDto
    ) {
        return userService.updateUser(id, userDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteUserById(@PathVariable("id") String id) {
        return userService.deleteUserById(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(UserNotFoundException.class, ex ->
                        Mono.just(
                                ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(Map.of("error", ex.getMessage()))
                        )
                );
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<User> getAllUsers() {
        System.out.println("listing all users...");
        return userService.getAllUsers();
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<User>> getUserBy(@PathVariable("id") String id) {
        System.out.println("get user by id...");

        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(Exception.class, ex -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping(value = "/user-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<String>>> getUserIds() {
        return userService.getUserIds()
                .map(ResponseEntity::ok)
                .onErrorResume(Exception.class, ex -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Collections.emptyList()));
                });
    }
}
