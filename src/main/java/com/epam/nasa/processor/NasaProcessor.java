package com.epam.nasa.processor;

import com.epam.nasa.dto.Photos;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class NasaProcessor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void process(Exchange exchange) {
        if (exchange.getMessage().getBody() instanceof Photos photos) {
            LOG.info("Photos size : {}", photos.getPhotos().size());
        }
    }
}
