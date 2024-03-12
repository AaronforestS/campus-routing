package com.ogoqjl.campus.routing.model;

public class TableEntity {
    private int routeId;
    private int nodeFrom;
    private int nodeTo;
    private double cost;
    private String destination;

    public int getNodeFrom() {
        return nodeFrom;
    }

    public void setNodeFrom(int nodeFrom) {
        this.nodeFrom = nodeFrom;
    }

    public int getNodeTo() {
        return nodeTo;
    }

    public void setNodeTo(int nodeTo) {
        this.nodeTo = nodeTo;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }
}
