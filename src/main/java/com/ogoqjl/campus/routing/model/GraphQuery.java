package com.ogoqjl.campus.routing.model;

import com.ogoqjl.campus.routing.dataTransferObject.GraphEdge;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GraphQuery {
    private final List<GraphEdge> graph;

    private final JdbcTemplate jdbcTemplate;

    public GraphQuery(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        graph = jdbcTemplate.query("select * from fullRoute union select cost, nodeTo, nodeFrom from fullRoute", new GraphRowMapper());
    }

    public List<GraphEdge> getGraph() {
        return graph;
    }
}
