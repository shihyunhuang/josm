// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ExtensionFileFilterStubTest {

    /**
     * Subclass-based stub: override getDescription() to return a controlled value,
     * and record whether the stubbed method was invoked.
     */
    private static class StubExtensionFileFilter extends ExtensionFileFilter {
        private boolean getDescriptionCalled;

        StubExtensionFileFilter(String extension, String defaultExtension, String description) {
            super(extension, defaultExtension, description);
        }

        @Override
        public String getDescription() {
            getDescriptionCalled = true;
            return "STUBBED";
        }
    }

    @Test
    void testGetDescriptionUsesStubInsteadOfReal() {
        // The "real" description passed to super() should be ignored by our stub override
        StubExtensionFileFilter stub = new StubExtensionFileFilter("osm", "osm", "REAL_DESCRIPTION");

        // If the stubbed method is used, we will get the stubbed value, not "REAL_DESCRIPTION"
        assertEquals("STUBBED", stub.getDescription());

        // Proves the overridden (stubbed) method actually ran
        assertTrue(stub.getDescriptionCalled);
    }
}