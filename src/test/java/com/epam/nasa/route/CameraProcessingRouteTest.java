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
class CameraProcessingRouteTest {
    private static final Object BODY = "[{\"id\":20,\"name\":\"FHAZ\",\"rover_id\":5,\"full_name\":\"Front Hazard Avoidance Camera\"}," +
            "{\"id\":22,\"name\":\"MAST\",\"rover_id\":5,\"full_name\":\"Mast Camera\"}]";

    @Autowired
    private CamelContext context;

    @Autowired
    private ProducerTemplate producerTemplate;

    @EndpointInject("mock:activemq6:queue:cameraQueue")
    private MockEndpoint activemq6;

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
        camera2.setName("MAST");
        camera2.setRover_id(5);
        camera2.setFull_name("Mast Camera");

        cameras.add(camera1);
        cameras.add(camera2);
    }


    @Test
    public void shouldProcessCameraRoute() throws Exception {
        AdviceWith.adviceWith("camera-processing-route", context, getRouteBuilder("activemq6:queue:cameraQueue"));
        activemq6.expectedBodiesReceived(BODY);
        activemq6.expectedMessageCount(1);

        producerTemplate.sendBody("direct:processCamera", cameras);

        activemq6.assertIsSatisfied();
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