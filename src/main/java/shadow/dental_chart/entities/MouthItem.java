/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shadow.dental_chart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author muham
 */
public class MouthItem {

    Boolean available = Boolean.TRUE;
    Boolean implant = Boolean.FALSE;
    Integer mobility;
    Integer fork1 = 0;
    Integer bleeding1 = 0;
    Integer bleeding2 = 0;
    Integer bleeding3 = 0;
    Integer plaque1 = 0;
    Integer plaque2 = 0;
    Integer plaque3 = 0;
    Integer gum;
    Integer margin1;
    Integer margin2;
    Integer margin3;
    Integer depth1;
    Integer depth2;
    Integer depth3;

    //inferior
    Integer depth4;
    Integer depth5;
    Integer depth6;
    Integer margin4;
    Integer margin5;
    Integer margin6;
    Integer plaque4 = 0;
    Integer plaque5 = 0;
    Integer plaque6 = 0;
    Integer bleeding4 = 0;
    Integer bleeding5 = 0;
    Integer bleeding6 = 0;
    Integer fork2 = 0;
    Integer fork3 = 0;
    String note;

    //red nerve line
    Boolean nerve1;
    Boolean nerve2;
    Boolean nerve3;

    @JsonIgnore
    final BooleanProperty availableProperty;
    @JsonIgnore
    final BooleanProperty implantProperty;
    @JsonIgnore
    final IntegerProperty fork1Property;
    @JsonIgnore
    final IntegerProperty fork2Property;
    @JsonIgnore
    final IntegerProperty fork3Property;
    @JsonIgnore
    final IntegerProperty bleeding1Property;
    @JsonIgnore
    final IntegerProperty bleeding2Property;
    @JsonIgnore
    final IntegerProperty bleeding3Property;
    @JsonIgnore
    final IntegerProperty bleeding4Property;
    @JsonIgnore
    final IntegerProperty bleeding5Property;
    @JsonIgnore
    final IntegerProperty bleeding6Property;
    @JsonIgnore
    final IntegerProperty plaque1Property;
    @JsonIgnore
    final IntegerProperty plaque2Property;
    @JsonIgnore
    final IntegerProperty plaque3Property;
    @JsonIgnore
    final IntegerProperty plaque4Property;
    @JsonIgnore
    final IntegerProperty plaque5Property;
    @JsonIgnore
    final IntegerProperty plaque6Property;

    public MouthItem() {
        availableProperty = new SimpleBooleanProperty(available);
        implantProperty = new SimpleBooleanProperty(implant);
        fork1Property = new SimpleIntegerProperty(fork1);
        fork2Property = new SimpleIntegerProperty(fork2);
        fork3Property = new SimpleIntegerProperty(fork3);
        bleeding1Property = new SimpleIntegerProperty(bleeding1);
        bleeding2Property = new SimpleIntegerProperty(bleeding2);
        bleeding3Property = new SimpleIntegerProperty(bleeding3);
        bleeding4Property = new SimpleIntegerProperty(bleeding4);
        bleeding5Property = new SimpleIntegerProperty(bleeding5);
        bleeding6Property = new SimpleIntegerProperty(bleeding6);
        plaque1Property = new SimpleIntegerProperty(plaque1);
        plaque2Property = new SimpleIntegerProperty(plaque2);
        plaque3Property = new SimpleIntegerProperty(plaque3);
        plaque4Property = new SimpleIntegerProperty(plaque4);
        plaque5Property = new SimpleIntegerProperty(plaque5);
        plaque6Property = new SimpleIntegerProperty(plaque6);
    }

    public static void resetForks(MouthItem item) {
        item.setFork1(0);//to remove the fork symobol painted on the pic
        item.setFork2(0);
        item.setFork3(0);
    }

    public static void resetBleedingAndPlaque(MouthItem item) {
        item.setBleeding1(0);
        item.setBleeding2(0);
        item.setBleeding3(0);
        item.setBleeding4(0);
        item.setBleeding5(0);
        item.setBleeding6(0);
        item.setPlaque1(0);
        item.setPlaque2(0);
        item.setPlaque3(0);
        item.setPlaque4(0);
        item.setPlaque5(0);
        item.setPlaque6(0);
    }

    public Boolean getAvailable() {
        return availableProperty.get();
    }

    public void setAvailable(Boolean available) {
        availableProperty.set(available);
    }

    public BooleanProperty availableProperty() {
        return availableProperty;
    }

    public BooleanProperty implantProperty() {
        return implantProperty;
    }

    public IntegerProperty fork1Property() {
        return fork1Property;
    }

    public IntegerProperty fork2Property() {
        return fork2Property;
    }

    public IntegerProperty fork3Property() {
        return fork3Property;
    }

    public Boolean getImplant() {
        return implantProperty.get();
    }

    public void setImplant(Boolean implant) {
        implantProperty.set(implant);
    }

    public Integer getMobility() {
        return mobility;
    }

    public void setMobility(Integer mobility) {
        this.mobility = mobility;
    }

    public Integer getFork1() {
        return fork1Property.get();
    }

    public void setFork1(Integer fork1) {
        fork1Property.set(fork1);
    }

    public Integer getFork2() {
        return fork2Property.get();
    }

    public void setFork2(Integer fork2) {
        fork2Property.set(fork2);
    }

    public Integer getFork3() {
        return fork3Property.get();
    }

    public void setFork3(Integer fork3) {
        fork3Property.set(fork3);
    }

    public Integer getBleeding1() {
        return bleeding1Property.get();
    }

    public void setBleeding1(Integer bleeding1) {
        if (bleeding1 == 3)
            bleeding1 = 0;
        this.bleeding1Property.set(bleeding1);
    }

    public Integer getBleeding2() {
        return bleeding2Property.get();
    }

    public void setBleeding2(Integer bleeding2) {
        if (bleeding2 == 3)
            bleeding2 = 0;
        this.bleeding2Property.set(bleeding2);
    }

    public Integer getBleeding3() {
        return bleeding3Property.get();
    }

    public void setBleeding3(Integer bleeding3) {
        if (bleeding3 == 3)
            bleeding3 = 0;
        this.bleeding3Property.set(bleeding3);
    }

    public Integer getBleeding4() {
        return bleeding4Property.get();
    }

    public void setBleeding4(Integer bleeding4) {
        if (bleeding4 == 3)
            bleeding4 = 0;
        this.bleeding4Property.set(bleeding4);
    }

    public Integer getBleeding5() {
        return bleeding5Property.get();
    }

    public void setBleeding5(Integer bleeding5) {
        if (bleeding5 == 3)
            bleeding5 = 0;
        this.bleeding5Property.set(bleeding5);
    }

    public Integer getBleeding6() {
        return bleeding6Property.get();
    }

    public void setBleeding6(Integer bleeding6) {
        if (bleeding6 == 3)
            bleeding6 = 0;
        this.bleeding6Property.set(bleeding6);
    }

    public IntegerProperty bleeding1Property() {
        return bleeding1Property;
    }

    public IntegerProperty bleeding2Property() {
        return bleeding2Property;
    }

    public IntegerProperty bleeding3Property() {
        return bleeding3Property;
    }

    public IntegerProperty bleeding4Property() {
        return bleeding4Property;
    }

    public IntegerProperty bleeding5Property() {
        return bleeding5Property;
    }

    public IntegerProperty bleeding6Property() {
        return bleeding6Property;
    }

    public Integer getPlaque1() {
        return plaque1Property.get();
    }

    public void setPlaque1(Integer plaque1) {
        if (plaque1 == 2)
            plaque1 = 0;
        plaque1Property.set(plaque1);
    }

    public Integer getPlaque2() {
        return plaque2Property.get();
    }

    public void setPlaque2(Integer plaque2) {
        if (plaque2 == 2)
            plaque2 = 0;
        plaque2Property.set(plaque2);
    }

    public Integer getPlaque3() {
        return plaque3Property.get();
    }

    public void setPlaque3(Integer plaque3) {
        if (plaque3 == 2)
            plaque3 = 0;
        plaque3Property.set(plaque3);
    }

    public Integer getPlaque4() {
        return plaque4Property.get();
    }

    public void setPlaque4(Integer plaque4) {
        if (plaque4 == 2)
            plaque4 = 0;
        plaque4Property.set(plaque4);
    }

    public Integer getPlaque5() {
        return plaque5Property.get();
    }

    public void setPlaque5(Integer plaque5) {
        if (plaque5 == 2)
            plaque5 = 0;
        plaque5Property.set(plaque5);
    }

    public Integer getPlaque6() {
        return plaque6Property.get();
    }

    public void setPlaque6(Integer plaque6) {
        if (plaque6 == 2)
            plaque6 = 0;
        plaque6Property.set(plaque6);
    }

    public IntegerProperty plaque1Property() {
        return plaque1Property;
    }

    public IntegerProperty plaque2Property() {
        return plaque2Property;
    }

    public IntegerProperty plaque3Property() {
        return plaque3Property;
    }

    public IntegerProperty plaque4Property() {
        return plaque4Property;
    }

    public IntegerProperty plaque5Property() {
        return plaque5Property;
    }

    public IntegerProperty plaque6Property() {
        return plaque6Property;
    }

    public Integer getGum() {
        return gum;
    }

    public void setGum(Integer gum) {
        this.gum = gum;
    }

    public Integer getMargin1() {
        return margin1;
    }

    public void setMargin1(Integer margin1) {
        this.margin1 = margin1;
    }

    public Integer getMargin2() {
        return margin2;
    }

    public void setMargin2(Integer margin2) {
        this.margin2 = margin2;
    }

    public Integer getMargin3() {
        return margin3;
    }

    public void setMargin3(Integer margin3) {
        this.margin3 = margin3;
    }

    public Integer getDepth1() {
        return depth1;
    }

    public void setDepth1(Integer depth1) {
        this.depth1 = depth1;
    }

    public Integer getDepth2() {
        return depth2;
    }

    public void setDepth2(Integer depth2) {
        this.depth2 = depth2;
    }

    public Integer getDepth3() {
        return depth3;
    }

    public void setDepth3(Integer depth3) {
        this.depth3 = depth3;
    }

    public Integer getDepth4() {
        return depth4;
    }

    public void setDepth4(Integer depth4) {
        this.depth4 = depth4;
    }

    public Integer getDepth5() {
        return depth5;
    }

    public void setDepth5(Integer depth5) {
        this.depth5 = depth5;
    }

    public Integer getDepth6() {
        return depth6;
    }

    public void setDepth6(Integer depth6) {
        this.depth6 = depth6;
    }

    public Integer getMargin4() {
        return margin4;
    }

    public void setMargin4(Integer margin4) {
        this.margin4 = margin4;
    }

    public Integer getMargin5() {
        return margin5;
    }

    public void setMargin5(Integer margin5) {
        this.margin5 = margin5;
    }

    public Integer getMargin6() {
        return margin6;
    }

    public void setMargin6(Integer margin6) {
        this.margin6 = margin6;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getNerve1() {
        return nerve1;
    }

    public void setNerve1(Boolean nerve1) {
        this.nerve1 = nerve1;
    }

    public Boolean getNerve2() {
        return nerve2;
    }

    public void setNerve2(Boolean nerve2) {
        this.nerve2 = nerve2;
    }

    public Boolean getNerve3() {
        return nerve3;
    }

    public void setNerve3(Boolean nerve3) {
        this.nerve3 = nerve3;
    }

}
