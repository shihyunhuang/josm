package org.openstreetmap.josm.actions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.openstreetmap.josm.testutils.annotations.BasicPreferences;
import org.openstreetmap.josm.testutils.annotations.Main;
import org.openstreetmap.josm.testutils.annotations.Projection;

/**
 * Demonstrates subclass-based stubbing by overriding actionPerformed.
 */
@BasicPreferences
@Main
@Projection
class SelectAllActionStubTest {

    static class StubSelectAllAction extends SelectAllAction {

        boolean stubUsed = false;

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            
            // we simulate controlled behavior.
            // make it predictable
            stubUsed = true;
        }
    }

    @Test
    void testStubbedActionPerformedIsUsed() {
        StubSelectAllAction action = new StubSelectAllAction();

        action.actionPerformed(null);

        assertTrue(action.stubUsed,
                "Expected stubbed actionPerformed to be executed.");
    }
}