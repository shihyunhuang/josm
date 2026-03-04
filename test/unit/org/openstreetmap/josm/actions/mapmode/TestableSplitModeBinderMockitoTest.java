package org.openstreetmap.josm.actions.mapmode;

import static org.mockito.Mockito.*;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.junit.Test;
import org.mockito.InOrder;

public class TestableSplitModeBinderMockitoTest {


    @Test
    public void enter_shouldRegisterListeners() {
        // Arrange: mock dependency
        MapViewPort view = mock(MapViewPort.class);
        TestableSplitModeBinder binder = new TestableSplitModeBinder(view);

        MouseListener ml = mock(MouseListener.class);
        MouseMotionListener mml = mock(MouseMotionListener.class);

        // Act
        binder.enter(ml, mml);

        // Assert: behavior verification
        verify(view, times(1)).addMouseListener(ml);
        verify(view, times(1)).addMouseMotionListener(mml);
        verifyNoMoreInteractions(view);
    }

    @Test
    public void exit_shouldUnregisterListeners_inCorrectOrder() {
        MapViewPort view = mock(MapViewPort.class);
        TestableSplitModeBinder binder = new TestableSplitModeBinder(view);

        MouseListener ml = mock(MouseListener.class);
        MouseMotionListener mml = mock(MouseMotionListener.class);

        // Act
        binder.exit(ml, mml);

        // Assert: verify calls + (optional) order
        InOrder inOrder = inOrder(view);
        inOrder.verify(view).removeMouseMotionListener(mml);
        inOrder.verify(view).removeMouseListener(ml);
        verifyNoMoreInteractions(view);
    }
}