package com.tribune.dental_chart.entity;

import java.util.Map;


public class DentalChart {

    private Map<Integer, MouthItem> superiorMap;
    private Map<Integer, MouthItem> inferiorMap;

    public Map<Integer, MouthItem> getSuperiorMap() {
        return superiorMap;
    }

    public void setSuperiorMap(Map<Integer, MouthItem> superiorMap) {
        this.superiorMap = superiorMap;
    }

    public Map<Integer, MouthItem> getInferiorMap() {
        return inferiorMap;
    }

    public void setInferiorMap(Map<Integer, MouthItem> inferiorMap) {
        this.inferiorMap = inferiorMap;
    }
}
