package com.ogoqjl.campus.routing.dataAccess;

import com.ogoqjl.campus.routing.config.JDBCTemplateConfig;
import com.ogoqjl.campus.routing.dataTransferObject.FromToNodes;
import com.ogoqjl.campus.routing.model.Graph;
import com.ogoqjl.campus.routing.model.TableEntity;
import com.ogoqjl.campus.routing.model.TableEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DataAccessObject {
    private Graph graph;

    public DataAccessObject(JdbcTemplate template){
        this.graph = new Graph(template);
    }

    public double getRouteForTwoPoints(int nodeFrom, int nodeTo) {

        List<FromToNodes> nodes = graph.getGraph().stream()
                .map(node -> new FromToNodes(node.getNodeFrom(), node.getNodeTo()))
                .toList();
        FromToNodes maxNodeIds = nodes.stream()
                .max(Comparator.comparing(node -> (Math.max(node.nodeFrom(), node.nodeTo()))))
                .orElseThrow();
        int maxNodeId =  Math.max(maxNodeIds.nodeFrom(), maxNodeIds.nodeTo());

        double[] distances = new double[maxNodeId + 1];
        Arrays.fill(distances, Double.MAX_VALUE);
        distances[nodeFrom] = 0;

        PriorityQueue<Integer> vertices = new PriorityQueue<>(Comparator.comparingDouble(vertex -> distances[vertex]));
        vertices.add(nodeFrom);

        while(!vertices.isEmpty()){
            int currentVertex = vertices.poll();
            if(currentVertex == nodeTo)
                break;

            List<TableEntity> adjecentVertices = graph.getGraph()
                    .stream()
                    .filter(node -> node.getNodeFrom() == currentVertex)
                    .toList();

            for(TableEntity adjacentVertex : adjecentVertices){
                double newDistance = distances[currentVertex] + adjacentVertex.getCost();
                if(newDistance < distances[adjacentVertex.getNodeTo()]){
                    distances[adjacentVertex.getNodeTo()] = newDistance;
                    vertices.add(adjacentVertex.getNodeTo());
                }else{
                    break;
                }

                System.out.println(currentVertex + "/////" + adjacentVertex.getNodeTo() + "/////" + distances[adjacentVertex.getNodeTo()] + "/////" + distances[currentVertex] + "\n");
            }
        }
        return distances[nodeTo];
    };
}
