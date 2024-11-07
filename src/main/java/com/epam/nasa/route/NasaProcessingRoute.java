package com.epam.nasa.route;

import com.epam.nasa.dto.Photos;
import com.epam.nasa.processor.NasaProcessor;
import org.apache.camel.Processor;
import org.apache.camel.builder.endpoint.LambdaEndpointRouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NasaProcessingRoute {
    private static final String NASA_URL = "api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=14&api_key=HXupDX6FRObkIlVbdLbI1oLZxR5VIJk0Ow4LhcKA";

    @Bean
    public Processor nasaProcessor() {
        return new NasaProcessor();
    }

    @Bean
    @Autowired
    public LambdaEndpointRouteBuilder nasaRouteBuilder(Processor nasaProcessor) {
        return rb -> rb.from(rb.direct("start"))
                .routeId("nasa-processing-route")
                .log("Call to Nasa")
                .to(rb.https(NASA_URL))
                .log("${body}")
                .log("Converting JSON to DTO")
                .unmarshal().json(JsonLibrary.Jackson, Photos.class)
                .process(nasaProcessor)
                .log("Sending Message to camera-processing-route ")
                .to(rb.direct("processCamera"));
    }
}
