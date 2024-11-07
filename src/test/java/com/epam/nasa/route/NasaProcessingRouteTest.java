package com.epam.nasa.route;

import com.epam.nasa.dto.Camera;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@CamelSpringBootTest
@SpringBootTest(classes = com.epam.nasa.NasaApplication.class)
class NasaProcessingRouteTest {
    private static final Object BODY = "{\"photos\": [{\"id\": 726, \"sol\": 14, \"camera\": {\"id\": 20, \"name\": \"FHAZ\", \"rover_id\": 5, \"full_name\": \"Front Hazard Avoidance Camera\"}, \"earth_date\": \"2012-08-20\", \"rover\": {\"id\": 5, \"name\": \"Curiosity\", \"landing_date\": \"2012-08-06\", \"launch_date\": \"2011-11-26\", \"status\": \"active\", \"max_sol\": 4102, \"max_date\": \"2024-02-19\", \"total_photos\": 695670, \"cameras\": [{\"name\": \"FHAZ\", \"full_name\": \"Front Hazard Avoidance Camera\"}]}}, {\"id\": 9717, \"sol\": 14, \"camera\": {\"id\": 22, \"name\": \"Mast\", \"rover_id\": 5, \"full_name\": \"Mast Camera\"}, \"earth_date\": \"2012-08-20\", \"rover\": {\"id\": 5, \"name\": \"Curiosity\", \"landing_date\": \"2012-08-06\", \"launch_date\": \"2011-11-26\", \"status\": \"active\", \"max_sol\": 4102, \"max_date\": \"2024-02-19\", \"total_photos\": 695670, \"cameras\": [{\"name\": \"FHAZ\", \"full_name\": \"Front Hazard Avoidance Camera\"}]}}]}";

    private static final String NASA_URL = "api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=14&api_key=HXupDX6FRObkIlVbdLbI1oLZxR5VIJk0Ow4LhcKA";

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @EndpointInject("mock:direct:processCamera")
    private MockEndpoint cameraEndpoint;

    @EndpointInject("mock:https:api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos")
    private MockEndpoint nasaEndpoint;

    private final List<Camera> cameras = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Camera camera1 = new Camera();
        camera1.setId(20);
        camera1.setName("FHAZ");
        camera1.setRover_id(5);
        camera1.setFull_name("Front Hazard Avoidance Camera");
        Camera camera2 = new Camera();
        camera2.setId(22);
        camera2.setName("Mast");
        camera2.setRover_id(5);
        camera2.setFull_name("Mast Camera");

        cameras.add(camera1);
        cameras.add(camera2);
    }

    @Test
    public void shouldProcessNasaRoute() throws Exception {
        AdviceWith.adviceWith("nasa-processing-route", camelContext, getRouteBuilder("https:" + NASA_URL));
        AdviceWith.adviceWith("nasa-processing-route", camelContext, getRouteBuilder("direct:processCamera"));

        nasaEndpoint.whenAnyExchangeReceived(exchange -> exchange.getMessage().setBody(BODY));
        cameraEndpoint.expectedBodiesReceived(List.of(cameras));

        producerTemplate.sendBody("direct:start", "");

        cameraEndpoint.assertIsSatisfied(500);
    }

    private static AdviceWithRouteBuilder getRouteBuilder(final String endpointPath) {
        return new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                mockEndpointsAndSkip(endpointPath);
            }
        };
    }
}