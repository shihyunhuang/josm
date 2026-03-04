package org.openstreetmap.josm.actions.mapmode;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Objects;

/**
 * New, more testable design:
 * No MainApplication.getMap().mapView static dependency.
 * We inject a MapViewPort (adapter) instead.
 */
public class TestableSplitModeBinder {

    private final MapViewPort view;

    public TestableSplitModeBinder(MapViewPort view) {
        this.view = Objects.requireNonNull(view, "view");
    }

    public void enter(MouseListener ml, MouseMotionListener mml) {
        view.addMouseListener(ml);
        view.addMouseMotionListener(mml);
    }

    public void exit(MouseListener ml, MouseMotionListener mml) {
        view.removeMouseMotionListener(mml);
        view.removeMouseListener(ml);
    }
}