package com.andrew121410.mc.world16trafficlights.enums;

public enum TrafficSystemType {
    ONE_LANE_ROAD,

    TWO_LANE_ROAD,
    TWO_LANE_ROAD_TURNING_LANE,

    THREE_LANE_ROAD,
    THREE_LANE_ROAD_TURNING_LANE,

    FOUR_LANE_ROAD,
    FOUR_LANE_ROAD_TURNING_LANE;

    public boolean hasTurningLane() {
        return switch (this) {
            case TWO_LANE_ROAD_TURNING_LANE, THREE_LANE_ROAD_TURNING_LANE, FOUR_LANE_ROAD_TURNING_LANE -> true;
            default -> false;
        };
    }
}