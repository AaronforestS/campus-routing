package com.ogoqjl.campus.routing.model;

import com.ogoqjl.campus.routing.dataTransferObject.FloorVertices;
import com.ogoqjl.campus.routing.dataTransferObject.GraphPoint;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.misc.Triple;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Objects;

@Service
public class NavigationInstructions {
    private final PointsQuery points;

    public NavigationInstructions(PointsQuery points) {
        this.points = points;
    }

    public LinkedList<Pair<Integer, String>> calculateInstructions(LinkedList<FloorVertices<LinkedList<Triple<Double, Double, Integer>>>> shortestPath, double[] distances, int nodeTo) {
        LinkedList<Triple<Integer, Double, String>> instructions = new LinkedList<>();
        String previousType = null;
        GraphPoint previousNode = null;
        double currentAngle;
        Double previousAngle = null;
        double travelledDistance = 0.0;
        String currentDirection;
        for (FloorVertices<LinkedList<Triple<Double, Double, Integer>>> floorVertices : shortestPath) {
            for (Triple<Double, Double, Integer> floorVertex : floorVertices.edge()) {
                GraphPoint currentNode = points.getPoints()
                        .stream()
                        .filter(point -> Objects.equals(point.id(), floorVertex.c))
                        .findAny()
                        .orElseThrow();

                if (previousNode == null) {
                    previousNode = currentNode;
                    previousType = currentNode.type();
                    continue;
                }
                currentAngle = calculateNodeAngles(currentNode, previousNode);
                if (Objects.equals(previousType, currentNode.type()) && (Objects.equals(previousType, "elevator") || Objects.equals(previousType, "stairs"))) {
                    instructions.add(new Triple<>(currentNode.id(), distances[currentNode.id()], "floorChange"));
                    travelledDistance = distances[currentNode.id()];
                    previousNode = currentNode;
                    continue;
                }

                if(previousAngle == null){
                    instructions.add(new Triple<>(currentNode.id(), distances[currentNode.id()] - travelledDistance, "forward"));
                }else{
                    currentDirection = calculateRotation(currentAngle, previousAngle);
                    instructions.add(new Triple<>(currentNode.id(), distances[currentNode.id()] - travelledDistance, currentDirection));
                }
                travelledDistance = distances[currentNode.id()];
                previousNode = currentNode;
                previousType = currentNode.type();
                previousAngle = currentAngle;
            }
        }
        instructions = simplifyInstructions(instructions);
        return instructionsToText(instructions);
    }
    private LinkedList<Pair<Integer, String>> instructionsToText(LinkedList<Triple<Integer, Double, String>> instructions){
        LinkedList<Pair<Integer, String>> instructionsText = new LinkedList<>();
        String text;
        for(Triple<Integer, Double, String> instruction : instructions){
            text = "";
            if(Objects.equals(instruction.c, "floorChange")){
                int zCoord = points.getPoints()
                        .stream()
                        .filter(point -> Objects.equals(point.id(), instruction.a))
                        .findAny()
                        .orElseThrow().zCoord();
                text = text.concat(String.format("Menjen a %d. emeletre!\n", zCoord));
            }else{
                if(Objects.equals(instruction.c, "left")){
                    text = text.concat("Forduljon balra!\n");
                }else if(Objects.equals(instruction.c, "right")){
                    text = text.concat("Forduljon jobbra!\n");
                }
                text = text.concat(String.format("Menjen a %s. métert előre!\n", roundToOneDecimal(instruction.b)));
            }
            instructionsText.add(new Pair<>(instruction.a,text));
        }
        return instructionsText;
    }
    private LinkedList<Triple<Integer, Double, String>> simplifyInstructions(LinkedList<Triple<Integer, Double, String>> instructions){
        Triple<Integer, Double, String> currentInstruction;
        Triple<Integer, Double, String> nextInstruction;
        for(int i = 0; i < instructions.size()-1;){
            currentInstruction = instructions.get(i);
            nextInstruction = instructions.get(i+1);
            if(Objects.equals(nextInstruction.c, "forward") && !Objects.equals(currentInstruction.c, "floorChange")){
                instructions.set(i,new Triple<>(nextInstruction.a, nextInstruction.b + currentInstruction.b, currentInstruction.c));
                instructions.remove(i+1);
            }else{
                i++;
            }
        }
        return instructions;
    }
    private static double calculateNodeAngles(GraphPoint currentNode, GraphPoint previousNode) {
        double longitudeRadian1 = Math.toRadians(currentNode.xCoord());
        double latitudeRadian1 = Math.toRadians(currentNode.yCoord());
        double longitudeRadian2 = Math.toRadians(previousNode.xCoord());
        double latitudeRadian2 = Math.toRadians(previousNode.yCoord());
        double longitudeDifference = longitudeRadian1 - longitudeRadian2;
        double angle = Math.toDegrees(Math.atan2(Math.sin(longitudeDifference) * Math.cos(latitudeRadian1), Math.cos(latitudeRadian2) * Math.sin(latitudeRadian1) - Math.sin(latitudeRadian2) * Math.cos(latitudeRadian1) * Math.cos(longitudeDifference)));
        return (angle + 360) % 360;
    }
    private String calculateRotation(double currentAngle, double previousAngle) {
        double difference = currentAngle - previousAngle;
        difference = (difference + 360) % 360;

        if (difference > 200 && difference < 340) {
            return "left";
        } else if (difference > 20 && difference < 160) {
            return "right";
        } else {
            return "forward";
        }
    }
    private static String roundToOneDecimal(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value);
    }
}