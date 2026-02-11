package org.openstreetmap.josm;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openstreetmap.josm.command.Command;
import org.openstreetmap.josm.data.UndoRedoHandler;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.testutils.JOSMTestRules;

public class UndoRedoFsmTest {

    // JOSM test environment init (no manual call needed)
    @RegisterExtension
    static JOSMTestRules rules = new JOSMTestRules()
            .preferences()
            .projection()
            .main();

    private UndoRedoHandler ur;
    private DataSet ds;

    /**
     * It just toggles an internal flag on execute/undo so we can assert behavior.
     */
    private static final class NoOpCommand extends Command {

        NoOpCommand(DataSet ds) {
            super(ds);
        }

        @Override
        public boolean executeCommand() {
            return true;
        }

        @Override
        public void undoCommand() {
        }

        @Override
        public String getDescriptionText() {
            return "NoOpCommand";
        }

        // Required by your JOSM Command abstraction
        @Override
        public void fillModifiedData(Collection<OsmPrimitive> modified,
                                     Collection<OsmPrimitive> deleted,
                                     Collection<OsmPrimitive> added) {
            // no-op: this command doesn't change dataset primitives
        }
    }

    @BeforeEach
    void setUp() {
        ur = UndoRedoHandler.getInstance();
        ur.clean(); // reset stacks
        ds = new DataSet();

        // FSM state S0: EMPTY
        assertFalse(ur.hasUndoCommands());
        assertFalse(ur.hasRedoCommands());
    }

    /**
     * S0 (EMPTY) --ADD--> S1 (UNDO_ONLY) --UNDO-->  S2 (REDO_ONLY) --REDO--> S1 (UNDO_ONLY)
     */
    @Test
    void add_then_undo_sets_redo() {
        ur.add(new NoOpCommand(ds));
        // FSM state S1: UNDO_ONLY
        assertTrue(ur.hasUndoCommands());
        assertFalse(ur.hasRedoCommands());

        ur.undo();
        // FSM state S2: REDO_ONLY
        assertFalse(ur.hasUndoCommands());
        assertTrue(ur.hasRedoCommands());

        ur.redo();
        // FSM state S1: UNDO_ONLY
        assertTrue(ur.hasUndoCommands());
        assertFalse(ur.hasRedoCommands());
    }

    /** 
     * S0 (EMPTY) --ADD--> S1 (UNDO_ONLY) --UNDO-->  S2 (REDO_ONLY) --ADD--> S1 (UNDO_ONLY)
    */
    @Test
    void add_from_redoOnly_clears_redo() {
        ur.add(new NoOpCommand(ds)); 

        ur.undo();                   

        ur.add(new NoOpCommand(ds)); 
        // FSM state S1: UNDO_ONLY
        assertTrue(ur.hasUndoCommands());
        assertFalse(ur.hasRedoCommands());
    }

    /**
     * S0 (EMPTY) --ADD--> S1 (UNDO_ONLY) --UNDO-->  S3 (UNDO_AND_REDO_AVAILABLE) --ADD--> S1 (UNDO_ONLY)
     */
    @Test
    void two_adds_then_undo_sets_undo_and_redo() {
        ur.add(new NoOpCommand(ds));
        ur.add(new NoOpCommand(ds));

        ur.undo();
        // FSM state S3: UNDO_AND_REDO_AVAILABLE
        assertTrue(ur.hasUndoCommands());
        assertTrue(ur.hasRedoCommands());

        ur.add(new NoOpCommand(ds));
        // FSM state S1: UNDO_ONLY
        assertTrue(ur.hasUndoCommands());
        assertFalse(ur.hasRedoCommands());

    }


    /**
     * CLEAN always returns to EMPTY.
     */
    @Test
    void clean_to_s0() {
        // From S0 (EMPTY) to S1 (UNDO_ONLY) to S2 (REDO_ONLY) and back to S0 (EMPTY)
        ur.add(new NoOpCommand(ds));
        ur.undo();
        ur.clean();
        // FSM state S0: EMPTY
        assertFalse(ur.hasUndoCommands());
        assertFalse(ur.hasRedoCommands());

        // From S0 (EMPTY) to S1 (UNDO_ONLY) and back to S0 (EMPTY)
        ur.add(new NoOpCommand(ds));
        ur.clean();
        // FSM state S0: EMPTY
        assertFalse(ur.hasUndoCommands());
        assertFalse(ur.hasRedoCommands());
    }
}
