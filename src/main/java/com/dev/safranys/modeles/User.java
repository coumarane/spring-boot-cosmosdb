package com.dev.safranys.modeles;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;

@Container(containerName = "users")
public record User(
        @Id
        String id,

        @NotBlank
        String email,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        int age,

        @PartitionKey
        @NotBlank
        String city
) {
    // Compact constructor (Java 17+).
    // Auto-generate ID if it's null or blank.
    public User {
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
    }
}
