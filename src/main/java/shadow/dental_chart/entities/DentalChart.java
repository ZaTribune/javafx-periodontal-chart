/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shadow.dental_chart.entities;

import java.util.List;
import java.util.Map;

/**
 *
 * @author muham
 */
public class DentalChart {
    private Map<Integer,MouthItem>superiorMap;
    private Map<Integer,MouthItem>inferiorMap;

    public Map<Integer,MouthItem> getSuperiorMap() {
        return superiorMap;
    }

    public void setSuperiorMap(Map<Integer,MouthItem> superiorMap) {
        this.superiorMap = superiorMap;
    }

    public Map<Integer,MouthItem> getInferiorMap() {
        return inferiorMap;
    }

    public void setInferiorMap(Map<Integer,MouthItem> inferiorMap) {
        this.inferiorMap = inferiorMap;
    }
    
    
    
}
