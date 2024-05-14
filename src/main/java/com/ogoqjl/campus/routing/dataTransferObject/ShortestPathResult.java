package com.ogoqjl.campus.routing.dataTransferObject;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.misc.Triple;

import java.util.LinkedList;

public record ShortestPathResult(LinkedList<FloorVertices<LinkedList<Triple<Double, Double, Integer>>>> shortestPath, LinkedList<Pair<Integer, String>> instructions) {
}
