package com.dev.safranys.configs;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties(CosmosProperties.class)
@EnableCosmosRepositories(basePackages = "com.dev.safranys.repositories")
@EnableReactiveCosmosRepositories(basePackages = "com.dev.safranys.repositories")
@PropertySource("classpath:application.properties")
public class CosmosDBConfig extends AbstractCosmosConfiguration {

    private final CosmosProperties cosmosProperties;

    public CosmosDBConfig(CosmosProperties cosmosProperties) {
        this.cosmosProperties = cosmosProperties;
    }

    @Bean
    public CosmosClientBuilder cosmosClientBuilder() {
        String connectionString = cosmosProperties.getConnectionString();

        if (connectionString == null || connectionString.isEmpty()) {
            throw new IllegalArgumentException("Azure Cosmos DB connection string is missing!");
        }

        return new CosmosClientBuilder()
                .endpoint(getValueFromConnectionString(connectionString, "AccountEndpoint"))
                .key(getValueFromConnectionString(connectionString, "AccountKey"));
    }

    @Bean
    public CosmosClient cosmosClient(CosmosClientBuilder cosmosClientBuilder) {
        return cosmosClientBuilder.buildClient();
    }

    @Override
    public String getDatabaseName() {
        return cosmosProperties.getDatabase(); // Get database separately
    }

    // Helper method to extract values from the connection string
    private String getValueFromConnectionString(String connectionString, String key) {
        for (String part : connectionString.split(";")) {
            if (part.startsWith(key + "=")) {
                return part.split("=")[1];
            }
        }
        throw new IllegalArgumentException("Missing " + key + " in connection string");
    }
}