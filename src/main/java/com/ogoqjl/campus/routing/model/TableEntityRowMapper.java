package com.ogoqjl.campus.routing.model;

import com.ogoqjl.campus.routing.model.TableEntity;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableEntityRowMapper implements RowMapper<TableEntity> {
    @Override
    public TableEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        TableEntity entity = new TableEntity();
        entity.setRouteId(resultSet.getInt("routeId"));
        entity.setNodeFrom(resultSet.getInt("nodeFrom"));
        entity.setNodeTo(resultSet.getInt("nodeTo"));
        entity.setCost(resultSet.getDouble("cost"));
        entity.setDestination(resultSet.getString("destination"));
        return entity;
    }
}
