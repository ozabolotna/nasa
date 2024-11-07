package com.epam.nasa.dto;

import lombok.Data;

@Data
public class Camera {
    private int id;
    private String name;
    private int rover_id;
    private String full_name;
}