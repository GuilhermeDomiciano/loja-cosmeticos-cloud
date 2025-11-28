package com.domi.loja.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class DynamoDbConfig {

    @Bean
    public DynamoDbClient dynamoDbClient() {
        // lê region e endpoint das variáveis de ambiente
        String region = System.getenv().getOrDefault("AWS_REGION", "us-east-1");
        String endpoint = System.getenv("AWS_ENDPOINT"); // ex: http://localstack:4566 ou http://localhost:4566

        DynamoDbClient.Builder builder = DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        System.getenv().getOrDefault("AWS_ACCESS_KEY_ID", "test"),
                                        System.getenv().getOrDefault("AWS_SECRET_ACCESS_KEY", "test")
                                )
                        )
                );

        if (endpoint != null && !endpoint.isBlank()) {
            builder = builder.endpointOverride(URI.create(endpoint));
        }

        return builder.build();
    }
}
