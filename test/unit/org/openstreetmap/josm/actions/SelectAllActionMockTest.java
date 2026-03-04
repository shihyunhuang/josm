package org.openstreetmap.josm.actions;

import mockit.Expectations;
import mockit.Verifications;
import mockit.Mocked;
import org.junit.jupiter.api.Test;
import org.openstreetmap.josm.data.osm.DataSet;

class SelectAllActionMockTest {

    @Test
    void testSelectAllInvokesGetNodes(@Mocked DataSet dataSet) {
        // when getnodes has been called, return emptylist (stubbing)
        new Expectations() {{
            dataSet.getNodes();
            result = java.util.Collections.emptyList();
        }};

        TestableSelectAllAction action =
                new TestableSelectAllAction(dataSet);

        action.selectAll();

        new Verifications() {{
            dataSet.getNodes();
            times = 1;
        }};
    }
}