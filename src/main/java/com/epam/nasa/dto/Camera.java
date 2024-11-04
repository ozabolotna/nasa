package com.epam.nasa.dto;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class Camera {
    private int id;
    private String name;
    private int rover_id;
    private String full_name;
}