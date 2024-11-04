package com.epam.nasa.dto;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class Photo {
    private int id;
    private int sol;
    private Camera camera;
    private String img_src;
    private String earth_date;
    private Rover rover;
}
