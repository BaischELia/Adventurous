package com.adventurous.game.overlays.classes;


/**
 * Interface for Overlays.
 */
public interface IOverlay {
    void update(float deltaTime);
    void onShow();
    void onClose();
}
