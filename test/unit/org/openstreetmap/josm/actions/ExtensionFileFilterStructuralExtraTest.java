package org.openstreetmap.josm.actions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.openstreetmap.josm.actions.ExtensionFileFilter.AddArchiveExtension;

class ExtensionFileFilterStructuralExtraTest {

    @Test
    void testGetImportExtensionFileFilters() {
        List<ExtensionFileFilter> filters =
                ExtensionFileFilter.getImportExtensionFileFilters();

        assertNotNull(filters);
        assertFalse(filters.isEmpty());

        // Should be sorted using comparator
        for (int i = 1; i < filters.size(); i++) {
            assertTrue(
                filters.get(i - 1).getDescription()
                        .compareTo(filters.get(i).getDescription()) <= 0
                || filters.get(i).getDescription().contains("All")
            );
        }
    }

    @Test
    void testGetExportExtensionFileFilters() {
        List<ExtensionFileFilter> filters =
                ExtensionFileFilter.getExportExtensionFileFilters();

        assertNotNull(filters);
    }

    @Test
    void testDefaultImportNonNull() {
        ExtensionFileFilter filter =
                ExtensionFileFilter.getDefaultImportExtensionFileFilter("osm");

        assertNotNull(filter);
    }

    @Test
    void testDefaultExportNonNullAndFallback() {

        // Known extension
        ExtensionFileFilter known =
                ExtensionFileFilter.getDefaultExportExtensionFileFilter("osm");
        assertNotNull(known);

        // Unknown extension → fallback branch
        ExtensionFileFilter fallback =
                ExtensionFileFilter.getDefaultExportExtensionFileFilter("unknownext");
        assertNotNull(fallback);
    }

    @Test
    void testUpdateAllFormatsImporter() {
        // Should replace existing AllFormatsImporter
        ExtensionFileFilter.updateAllFormatsImporter();

        List<ExtensionFileFilter> filters =
                ExtensionFileFilter.getImportExtensionFileFilters();

        assertNotNull(filters);
    }

    

    // ----------------------
    // Dummy helper classes
    // ----------------------

    static class DummyImporter extends org.openstreetmap.josm.gui.io.importexport.FileImporter {

        DummyImporter(ExtensionFileFilter filter) {
            super(filter);
        }

        @Override
        public void importData(java.io.File file, org.openstreetmap.josm.gui.progress.ProgressMonitor progressMonitor) {
            // no-op
        }
    }

    
}


