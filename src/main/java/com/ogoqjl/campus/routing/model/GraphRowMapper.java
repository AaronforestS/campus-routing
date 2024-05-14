package com.ogoqjl.campus.routing.model;

import com.ogoqjl.campus.routing.dataTransferObject.GraphEdge;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GraphRowMapper implements RowMapper<GraphEdge> {
    @Override
    public GraphEdge mapRow(ResultSet resultSet, int i) throws SQLException {
        Integer nodeFrom = resultSet.getInt("nodeFrom");
        Integer nodeTo = resultSet.getInt("nodeTo");
        double cost = resultSet.getDouble("cost");

        return new GraphEdge(nodeFrom, nodeTo, cost);
    }
}
