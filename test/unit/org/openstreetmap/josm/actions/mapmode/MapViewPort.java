package org.openstreetmap.josm.actions.mapmode;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public interface MapViewPort {
    void addMouseListener(MouseListener l);
    void addMouseMotionListener(MouseMotionListener l);
    void removeMouseListener(MouseListener l);
    void removeMouseMotionListener(MouseMotionListener l);
}