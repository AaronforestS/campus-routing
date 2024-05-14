package com.ogoqjl.campus.routing.model;

import com.ogoqjl.campus.routing.dataTransferObject.SearchBarRow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchBar {
    private final PointsQuery points;

    public SearchBar(JdbcTemplate template) {
        this.points = new PointsQuery(template);
    }

    public List<SearchBarRow> getSearchBarResult(String searchTerm) {
        return points.getPoints().stream()
                .map(point -> ((point.name() != null && point.name().toLowerCase().contains(searchTerm.toLowerCase()))
                        || (point.roomId() != null && point.roomId().toLowerCase().contains(searchTerm.toLowerCase())))
                        ? (new SearchBarRow(point.id(), point.name(), point.roomId(), point.zCoord()))
                        : null)
                .filter(point -> point != null)
                .toList();
    }
}