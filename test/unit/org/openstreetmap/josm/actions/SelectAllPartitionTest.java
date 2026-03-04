// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.testutils.annotations.BasicPreferences;
import org.openstreetmap.josm.testutils.annotations.Main;
import org.openstreetmap.josm.testutils.annotations.Projection;
import org.openstreetmap.josm.data.coor.LatLon;

/**
 * Partition tests for {@link SelectAllAction}.
 */
@BasicPreferences
@Main
@Projection
final class SelectAllActionPartitionTest {

    /**
     * Partition P1: Empty dataset
     * Expected behavior: No object should be selected.
     */
    @Test
    void testSelectAllWithEmptyDataSet() {
        DataSet empty = new DataSet();
        MainApplication.getLayerManager().addLayer(new OsmDataLayer(empty, "empty", null));

        assertEquals(0, empty.getSelected().size());

        new SelectAllAction().actionPerformed(null);

        assertEquals(0, empty.getSelected().size());
    }

    /**
     * Partition P2: Non-empty dataset
     * Expected behavior: All primitives should be selected.
     */
    @Test
    void testSelectAllWithNonEmptyDataSet() {
        DataSet ds = new DataSet();

        Node n1 = new Node(new LatLon(0,0));
        Node n2 = new Node(new LatLon(1,1));

        ds.addPrimitive(n1);
        ds.addPrimitive(n2);

        MainApplication.getLayerManager().addLayer(new OsmDataLayer(ds, "test", null));

        assertEquals(0, ds.getSelected().size());

        new SelectAllAction().actionPerformed(null);

        assertEquals(2, ds.getSelected().size());
    }
}