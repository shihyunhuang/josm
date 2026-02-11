package org.openstreetmap.josm.gui.layer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.testutils.annotations.BasicPreferences;
import org.openstreetmap.josm.testutils.annotations.Main;
import org.openstreetmap.josm.testutils.annotations.Projection;

@BasicPreferences
@Main
@Projection
class LayerStateFSMTest {

    @AfterEach
    void cleanup() {
        MainApplication.getLayerManager().getLayers().forEach(
            layer -> MainApplication.getLayerManager().removeLayer(layer)
        );
    }

    @Test
    void testSetActiveLayer() {
        DataSet ds = new DataSet();
        OsmDataLayer layer = new OsmDataLayer(ds, "test", null);

        MainApplication.getLayerManager().addLayer(layer);
        MainApplication.getLayerManager().setActiveLayer(layer);

        assertEquals(layer, MainApplication.getLayerManager().getActiveLayer());
    }

    @Test
    void testSwitchActiveLayer() {
    DataSet ds = new DataSet();
    OsmDataLayer layer1 = new OsmDataLayer(ds, "layer1", null);
    OsmDataLayer layer2 = new OsmDataLayer(ds, "layer2", null);

    MainApplication.getLayerManager().addLayer(layer1);
    MainApplication.getLayerManager().addLayer(layer2);

    MainApplication.getLayerManager().setActiveLayer(layer1);
    assertEquals(layer1, MainApplication.getLayerManager().getActiveLayer());

    MainApplication.getLayerManager().setActiveLayer(layer2);
    assertEquals(layer2, MainApplication.getLayerManager().getActiveLayer());
}

    @Test
    void testHideLayerKeepsActive() {
        DataSet ds = new DataSet();
        OsmDataLayer layer = new OsmDataLayer(ds, "test", null);

        MainApplication.getLayerManager().addLayer(layer);
        MainApplication.getLayerManager().setActiveLayer(layer);

        layer.setVisible(false);

        assertFalse(layer.isVisible());
        assertEquals(layer, MainApplication.getLayerManager().getActiveLayer());
    }

    @Test
    void testHiddenLayerCanBeActive() {
        DataSet ds = new DataSet();
        OsmDataLayer layer = new OsmDataLayer(ds, "test", null);

        MainApplication.getLayerManager().addLayer(layer);
        layer.setVisible(false);

        MainApplication.getLayerManager().setActiveLayer(layer);

        assertEquals(layer, MainApplication.getLayerManager().getActiveLayer());
    }

    @Test
    void testHideLayerFromVisibleInactive() {
        DataSet ds = new DataSet();
        OsmDataLayer layer1 = new OsmDataLayer(ds, "layer1", null);
        OsmDataLayer layer2 = new OsmDataLayer(ds, "layer2", null);

        MainApplication.getLayerManager().addLayer(layer1);
        MainApplication.getLayerManager().addLayer(layer2);

        MainApplication.getLayerManager().setActiveLayer(layer2);

        layer1.setVisible(false);

        assertFalse(layer1.isVisible());
        assertNotNull(MainApplication.getLayerManager().getActiveLayer());
    }

    @Test
    void testShowHiddenLayer() {
        DataSet ds = new DataSet();
        OsmDataLayer layer = new OsmDataLayer(ds, "test", null);

        MainApplication.getLayerManager().addLayer(layer);

        layer.setVisible(false);
        layer.setVisible(true);

        assertTrue(layer.isVisible());
        assertNotNull(MainApplication.getLayerManager().getActiveLayer());
    }
}
