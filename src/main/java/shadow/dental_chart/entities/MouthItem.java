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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;

/**
 *
 * @author muham
 */
public class MouthItem {

    public static void connectSet1CirclesListeners(MouthItem item, ChangeListener set1Listener) {
        item.margin1Property.addListener(set1Listener);
        item.margin2Property.addListener(set1Listener);
        item.margin3Property.addListener(set1Listener);
        item.depth1Property.addListener(set1Listener);
        item.depth2Property.addListener(set1Listener);
        item.depth3Property.addListener(set1Listener);
    }
        public static void connectSet2CirclesListeners(MouthItem item, ChangeListener set1Listener) {
        item.margin4Property.addListener(set1Listener);
        item.margin5Property.addListener(set1Listener);
        item.margin6Property.addListener(set1Listener);
        item.depth4Property.addListener(set1Listener);
        item.depth4Property.addListener(set1Listener);
        item.depth6Property.addListener(set1Listener);

    }

    Boolean available = Boolean.TRUE;
    Boolean implant = Boolean.FALSE;
    Integer mobility;
    Integer fork1 = 0;
    Integer bleeding1 = 0;
    Integer bleeding2 = 0;
    Integer bleeding3 = 0;
    Integer bleeding4 = 0;
    Integer bleeding5 = 0;
    Integer bleeding6 = 0;
    Integer plaque1 = 0;
    Integer plaque2 = 0;
    Integer plaque3 = 0;
    Integer plaque4 = 0;
    Integer plaque5 = 0;
    Integer plaque6 = 0;
    Double gum;
    String margin1;
    String margin2;
    String margin3;
    String margin4;
    String margin5;
    String margin6;
    String depth1;
    String depth2;
    String depth3;
    String depth4;
    String depth5;
    String depth6;
    Integer fork2 = 0;
    Integer fork3 = 0;
    String note;

    //for gingival margin and probing depth lines
    Integer selectedCircle;

    //for calculating the Plaque percentage
    @JsonIgnore
    Integer totalPlaque = 0;
    @JsonIgnore
    Integer totalProbingDepth = 0;
    @JsonIgnore
    Integer totalGingivalMargin = 0;
    @JsonIgnore
    Integer totalBleedingOnProbing = 0;

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
    @JsonIgnore
    final StringProperty margin1Property;
    @JsonIgnore
    final StringProperty margin2Property;
    @JsonIgnore
    final StringProperty margin3Property;
    @JsonIgnore
    final StringProperty margin4Property;
    @JsonIgnore
    final StringProperty margin5Property;
    @JsonIgnore
    final StringProperty margin6Property;
    @JsonIgnore
    final StringProperty depth1Property;
    @JsonIgnore
    final StringProperty depth2Property;
    @JsonIgnore
    final StringProperty depth3Property;
    @JsonIgnore
    final StringProperty depth4Property;
    @JsonIgnore
    final StringProperty depth5Property;
    @JsonIgnore
    final StringProperty depth6Property;

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
        margin1Property = new SimpleStringProperty(margin1);
        margin2Property = new SimpleStringProperty(margin2);
        margin3Property = new SimpleStringProperty(margin3);
        margin4Property = new SimpleStringProperty(margin4);
        margin5Property = new SimpleStringProperty(margin5);
        margin6Property = new SimpleStringProperty(margin6);
        depth1Property = new SimpleStringProperty(depth1);
        depth2Property = new SimpleStringProperty(depth2);
        depth3Property = new SimpleStringProperty(depth3);
        depth4Property = new SimpleStringProperty(depth4);
        depth5Property = new SimpleStringProperty(depth5);
        depth6Property = new SimpleStringProperty(depth6);

    }

    public static void resetForks(MouthItem item) {
        item.setFork1(0);//to remove the fork symobol painted on the pic
        item.setFork2(0);
        item.setFork3(0);
    }

    public static void resetValues(MouthItem item) {
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
        item.setDepth1("0");//for later when calculating the mean
        item.setDepth2("0");
        item.setDepth3("0");
        item.setDepth4("0");
        item.setDepth5("0");
        item.setDepth6("0");
        item.setMargin1("0");
        item.setMargin2("0");
        item.setMargin3("0");
        item.setMargin4("0");
        item.setMargin5("0");
        item.setMargin6("0");
        item.setTotalPlaque(0);
        item.setTotalGingivalMargin(0);
        item.setTotalProbingDepth(0);
        item.setTotalBleedingOnProbing(0);
    }

    public static void calculateTotalPlaque(MouthItem item) {
        item.setTotalPlaque(item.getPlaque1() + item.getPlaque2() + item.getPlaque3() + item.getPlaque4() + item.getPlaque5() + item.getPlaque6());
    }

    public static void calculateTotalProbingDepth(MouthItem item) {
        item.setTotalProbingDepth(
                Integer.parseInt(item.getDepth1()) + Integer.parseInt(item.getDepth2()) + Integer.parseInt(item.getDepth3())
                + Integer.parseInt(item.getDepth4()) + Integer.parseInt(item.getDepth5()) + Integer.parseInt(item.getDepth6()));
    }

    public static void calculateTotalGingivalMargin(MouthItem item) {
        item.setTotalGingivalMargin(
                Integer.parseInt(item.getMargin1()) + Integer.parseInt(item.getMargin2()) + Integer.parseInt(item.getMargin3())
                + Integer.parseInt(item.getMargin4()) + Integer.parseInt(item.getMargin5()) + Integer.parseInt(item.getMargin6()));

    }

    public static void calculateTotalBleedingOnProbing(MouthItem item) {
        item.setTotalBleedingOnProbing(
                Boolean.compare(item.getBleeding1() == 1 || item.getBleeding1() == 2, false)
                + Boolean.compare(item.getBleeding2() == 1 || item.getBleeding2() == 2, false)
                + Boolean.compare(item.getBleeding3() == 1 || item.getBleeding3() == 2, false)
                + Boolean.compare(item.getBleeding4() == 1 || item.getBleeding4() == 2, false)
                + Boolean.compare(item.getBleeding5() == 1 || item.getBleeding5() == 2, false)
                + Boolean.compare(item.getBleeding6() == 1 || item.getBleeding6() == 2, false)
        );

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

    public Double getGum() {
        return gum;
    }

    public void setGum(Double gum) {
        this.gum = gum;
    }

    public StringProperty margin1Property() {
        return margin1Property;
    }

    public StringProperty margin2Property() {
        return margin2Property;
    }

    public StringProperty margin3Property() {
        return margin3Property;
    }

    public StringProperty margin4Property() {
        return margin4Property;
    }

    public StringProperty margin5Property() {
        return margin5Property;
    }

    public StringProperty margin6Property() {
        return margin6Property;
    }

    public StringProperty depth1Property() {
        return depth1Property;
    }

    public StringProperty depth2Property() {
        return depth2Property;
    }

    public StringProperty depth3Property() {
        return depth3Property;
    }

    public StringProperty depth4Property() {
        return depth4Property;
    }

    public StringProperty depth5Property() {
        return depth5Property;
    }

    public StringProperty depth6Property() {
        return depth6Property;
    }

    public String getMargin1() {
        return margin1Property.get();
    }

    public void setMargin1(String margin1) {
        this.margin1Property.set(margin1);
    }

    public String getMargin2() {
        return margin2Property.get();
    }

    public void setMargin2(String margin2) {
        this.margin2Property.set(margin2);
    }

    public String getMargin3() {
        return margin3Property.get();
    }

    public void setMargin3(String margin3) {
        this.margin3Property.set(margin3);
    }

    public String getMargin4() {
        return margin4Property.get();
    }

    public void setMargin4(String margin4) {
        this.margin4Property.set(margin4);
    }

    public String getMargin5() {
        return margin5Property.get();
    }

    public void setMargin5(String margin5) {
        this.margin5Property.set(margin5);
    }

    public String getMargin6() {
        return margin6Property.get();
    }

    public void setMargin6(String margin6) {
        this.margin6Property.set(margin6);
    }

    public String getDepth1() {
        return depth1Property.get();
    }

    public void setDepth1(String depth1) {
        this.depth1Property.set(depth1);
    }

    public String getDepth2() {
        return depth2Property.get();
    }

    public void setDepth2(String depth2) {
        this.depth2Property.set(depth2);
    }

    public String getDepth3() {
        return depth3Property.get();
    }

    public void setDepth3(String depth3) {
        this.depth3Property.set(depth3);
    }

    public String getDepth4() {
        return depth4Property.get();
    }

    public void setDepth4(String depth4) {
        this.depth4Property.set(depth4);
    }

    public String getDepth5() {
        return depth5Property.get();
    }

    public void setDepth5(String depth5) {
        this.depth5Property.set(depth5);
    }

    public String getDepth6() {
        return depth6Property.get();
    }

    public void setDepth6(String depth6) {
        this.depth6Property.set(depth6);
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getSelectedCircle() {
        return selectedCircle;
    }

    public void setSelectedCircle(Integer selectedCircle) {
        this.selectedCircle = selectedCircle;
    }

    public Integer getTotalPlaque() {
        return totalPlaque;
    }

    public void setTotalPlaque(Integer totalPlaque) {
        this.totalPlaque = totalPlaque;
    }

    public Integer getTotalProbingDepth() {
        return totalProbingDepth;
    }

    public void setTotalProbingDepth(Integer totalProbingDepth) {
        this.totalProbingDepth = totalProbingDepth;
    }

    public Integer getTotalGingivalMargin() {
        return totalGingivalMargin;
    }

    public void setTotalGingivalMargin(Integer totalGingivalMargin) {
        this.totalGingivalMargin = totalGingivalMargin;
    }

    public Integer getTotalBleedingOnProbing() {
        return totalBleedingOnProbing;
    }

    public void setTotalBleedingOnProbing(Integer totalBleedingOnProbing) {
        this.totalBleedingOnProbing = totalBleedingOnProbing;
    }

}
