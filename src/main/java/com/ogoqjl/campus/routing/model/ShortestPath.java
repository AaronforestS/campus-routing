package com.ogoqjl.campus.routing.model;

import com.ogoqjl.campus.routing.dataTransferObject.*;
import org.antlr.v4.runtime.misc.Triple;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShortestPath {
    private final GraphQuery graph;
    private final PointsQuery points;
    private final NavigationInstructions instructions;

    public ShortestPath(JdbcTemplate template){
        this.graph = new GraphQuery(template);
        this.points = new PointsQuery(template);
        this.instructions = new NavigationInstructions(this.points);
    }

    public ShortestPathResult getRouteForTwoPoints(int nodeFrom, int nodeTo) {

        List<FromToNodes> nodes = graph.getGraph().stream()
                .map(node -> new FromToNodes(node.nodeFrom(), node.nodeTo()))
                .toList();

        int maxId = nodes.stream()
                .mapToInt(node -> node.nodeFrom())
                .max()
                .orElse(0);

        double[] distances = new double[maxId+1];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[nodeFrom] = 0.0;

        PriorityQueue<Integer> vertices = new PriorityQueue<>(Comparator.comparingDouble(vertex -> distances[vertex]));
        vertices.add(nodeFrom);

        Map<Integer, Integer> predecessors = new HashMap<>();

        calculateDistances(vertices, nodeTo, distances, predecessors, false);
        if(distances[nodeTo] == Double.POSITIVE_INFINITY){
            predecessors.clear();
            calculateDistances(vertices, nodeTo, distances, predecessors, true);
        }

        LinkedList<FloorVertices<LinkedList<Triple<Double, Double, Integer>>>> shortestPath = new LinkedList<>();
        int currentNode = nodeTo;
        Integer lastZCoord = null;
        while (predecessors.containsKey(currentNode)) {
            int finalCurrentNode = currentNode;
            GraphPoint graphPoint = getPointById(finalCurrentNode);

            if(!Objects.equals(graphPoint.zCoord(), lastZCoord)){
                lastZCoord = graphPoint.zCoord();
                shortestPath.addFirst(new FloorVertices<>(graphPoint.zCoord(), new LinkedList<>()));
            }
            shortestPath.getFirst().edge().addFirst(new Triple<>(graphPoint.xCoord(), graphPoint.yCoord(), graphPoint.id()));
            currentNode = predecessors.get(currentNode);
        }
        GraphPoint graphPoint = getPointById(nodeFrom);
        if(!Objects.equals(graphPoint.zCoord(), lastZCoord)){
            shortestPath.addFirst(new FloorVertices<>(graphPoint.zCoord(), new LinkedList<>()));
        }
        shortestPath.getFirst().edge().addFirst(new Triple<>(graphPoint.xCoord(), graphPoint.yCoord(), graphPoint.id()));

        return new ShortestPathResult(shortestPath, instructions.calculateInstructions(shortestPath, distances, nodeTo));
    }
    private void calculateDistances(PriorityQueue<Integer> vertices, int nodeTo, double[] distances, Map<Integer, Integer> predecessors, boolean isBehindNoentry){
        while(!vertices.isEmpty()){
            int currentVertex = vertices.poll();
            if(currentVertex == nodeTo)
                break;

            List<GraphEdge> adjecentVertices = graph.getGraph()
                    .stream()
                    .filter(node -> node.nodeFrom() == currentVertex)
                    .toList();

            for(GraphEdge adjacentVertex : adjecentVertices){
                double newDistance = Double.POSITIVE_INFINITY;
                if(isBehindNoentry || !Objects.equals(getPointType(adjacentVertex.nodeTo()), "noentry")){
                    newDistance = distances[currentVertex] + adjacentVertex.cost();
                }

                if(newDistance < distances[adjacentVertex.nodeTo()]){
                    distances[adjacentVertex.nodeTo()] = newDistance;
                    vertices.add(adjacentVertex.nodeTo());
                    predecessors.put(adjacentVertex.nodeTo(), currentVertex);
                }
            }
        }
    }
    private GraphPoint getPointById(int id){
        return points.getPoints()
                .stream()
                .filter(point -> point.id() == id)
                .findAny()
                .orElseThrow();

    }
    private String getPointType(int id){
        GraphPoint node = points.getPoints()
                .stream()
                .filter(point -> point.id() == id)
                .findAny()
                .orElseThrow();
        return node.type();
    }
}
