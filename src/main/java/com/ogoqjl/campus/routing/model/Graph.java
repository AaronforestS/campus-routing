package com.ogoqjl.campus.routing.model;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
public class Graph {
    private final List<TableEntity> graph;

    private final JdbcTemplate jdbcTemplate;

    public Graph(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        graph = jdbcTemplate.query("SELECT * FROM fullRoute", new TableEntityRowMapper());
    }

    public List<TableEntity> getGraph() {
        return graph;
    }
}
