/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shadow.dental_chart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import shadow.dental_chart.entities.MouthItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import shadow.dental_chart.entities.DentalChart;

/**
 *
 * @author muham
 */
public class TestJsonSerialize {

    public static void main(String[] args) {

        String s = DentalChartUtils.initializeDentalChart();
        System.out.println(s);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //List<MouthItem> items = objectMapper.readValue(s, new TypeReference<List<MouthItem>>(){});
            //items.forEach(item->{
           //System.out.println(""+item.getColumn());
           //});
            DentalChart chart = objectMapper.readValue(s, DentalChart.class);
            System.out.println(""+chart.getSuperiorMap());
            System.out.println(""+chart.getInferiorMap());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        
//        MouthItem code =mapper.writeValue(g, mapper);
    }
}
