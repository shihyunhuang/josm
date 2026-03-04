package org.openstreetmap.josm.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.coor.LatLon;

class TestableSelectAllActionTest {

    @Test
    void testSelectAllWithoutMainApplication() {

        DataSet ds = new DataSet();

        Node n1 = new Node(new LatLon(0,0));
        Node n2 = new Node(new LatLon(1,1));
        Node n3 = new Node(new LatLon(2,2));

        ds.addPrimitive(n1);
        ds.addPrimitive(n2);
        ds.addPrimitive(n3);

        TestableSelectAllAction action = new TestableSelectAllAction(ds);

        int selectedCount = action.selectAll();

        assertEquals(3, selectedCount);
    }
}
