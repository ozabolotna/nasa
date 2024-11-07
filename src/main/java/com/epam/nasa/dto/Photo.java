package com.epam.nasa.dto;

import lombok.Data;

@Data
public class Photo {
    private int id;
    private int sol;
    private Camera camera;
    private String img_src;
    private String earth_date;
    private Rover rover;
}
