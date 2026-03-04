package org.openstreetmap.josm.actions;

import org.openstreetmap.josm.data.osm.DataSet;

public class TestableSelectAllAction {

    private final DataSet dataSet;

    public TestableSelectAllAction(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public int selectAll() {
    if (dataSet == null) {
        return 0;
    }

    int count = dataSet.getNodes().size();

    
    return count;

}
}
