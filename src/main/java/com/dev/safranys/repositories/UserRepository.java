package com.dev.safranys.repositories;

import com.azure.spring.data.cosmos.repository.Query;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.dev.safranys.modeles.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveCosmosRepository<User, String>, CustomUserRepository {
    // Return a Flux of string IDs
    @Query("SELECT VALUE c.id FROM c")
    Flux<String> getUserIds();

    @Query("SELECT * FROM c")
    Flux<User> getAllUsers();
}
