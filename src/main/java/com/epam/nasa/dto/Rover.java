package com.epam.nasa.dto;

import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@ToString
public class Rover {
    private int id;
    private String name;
    private String landing_date;
    private String launch_date;
    private String status;
    private int max_sol;
    private String max_date;
    private int total_photos;
    private List<Camera> cameras;
}
