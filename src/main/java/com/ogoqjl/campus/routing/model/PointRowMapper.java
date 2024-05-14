package com.ogoqjl.campus.routing.model;

import com.ogoqjl.campus.routing.dataTransferObject.GraphPoint;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PointRowMapper implements RowMapper<GraphPoint> {
    @Override
    public GraphPoint mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("id");
        double xCoord = resultSet.getDouble("xCoord");
        double yCoord = resultSet.getDouble("yCoord");
        int zCoord = resultSet.getInt("zCoord");
        String roomId = resultSet.getString("roomId");
        String type = resultSet.getString("type");
        String name = resultSet.getString("name");

        return new GraphPoint(id, xCoord, yCoord, zCoord, roomId, type, name);
    }
}
