package com.ogoqjl.campus.routing.model;

import com.ogoqjl.campus.routing.dataTransferObject.GraphPoint;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PointsQuery {
    private final List<GraphPoint> points;

    private final JdbcTemplate jdbcTemplate;

    public PointsQuery(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        points = jdbcTemplate.query("select id, xCoord, yCoord, zCoord, roomId, type, name from graphPoints", new PointRowMapper());
    }

    public List<GraphPoint> getPoints() {
        return points;
    }

}
