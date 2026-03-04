package org.openstreetmap.josm.actions.mapmode;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestableSplitModeBinderTest {

    private static class FakeMapViewPort implements MapViewPort {
        final AtomicInteger addML = new AtomicInteger();
        final AtomicInteger addMML = new AtomicInteger();
        final AtomicInteger rmML = new AtomicInteger();
        final AtomicInteger rmMML = new AtomicInteger();

        @Override public void addMouseListener(MouseListener l) { addML.incrementAndGet(); }
        @Override public void addMouseMotionListener(MouseMotionListener l) { addMML.incrementAndGet(); }
        @Override public void removeMouseListener(MouseListener l) { rmML.incrementAndGet(); }
        @Override public void removeMouseMotionListener(MouseMotionListener l) { rmMML.incrementAndGet(); }
    }

    @Test
    public void testEnterExitBindsAndUnbindsListeners() {
        FakeMapViewPort fake = new FakeMapViewPort();
        TestableSplitModeBinder binder = new TestableSplitModeBinder(fake);

        MouseListener ml = new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        };

        MouseMotionListener mml = new MouseMotionListener() {
            @Override public void mouseDragged(MouseEvent e) {}
            @Override public void mouseMoved(MouseEvent e) {}
        };

        binder.enter(ml, mml);
        assertEquals(1, fake.addML.get());
        assertEquals(1, fake.addMML.get());

        binder.exit(ml, mml);
        assertEquals(1, fake.rmMML.get());
        assertEquals(1, fake.rmML.get());
    }
}