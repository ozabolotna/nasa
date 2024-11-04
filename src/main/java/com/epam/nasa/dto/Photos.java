package com.epam.nasa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class Photos {
    private List<Photo> photos;
}
