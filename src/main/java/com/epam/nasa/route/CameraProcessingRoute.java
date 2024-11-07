package com.epam.nasa.route;

import org.apache.camel.builder.endpoint.LambdaEndpointRouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CameraProcessingRoute {
    @Bean
    public LambdaEndpointRouteBuilder cameraRouteBuilder() {
        return rb -> rb.from("direct:processCamera")
                .routeId("camera-processing-route")
                .log("${body}")
                .log("Converting DTO to JSON")
                .marshal().json(JsonLibrary.Jackson)
                .log("Sending Message to ActiveMQ")
                .to("activemq6:queue:cameraQueue")
                .log("Message is send");
    }
}
