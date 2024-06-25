package com.study.studychatgpt.dto;


import lombok.Data;

import java.util.List;

@Data
public class ActorsFilms {

    private String actor;

    private List<String> movies;
}
