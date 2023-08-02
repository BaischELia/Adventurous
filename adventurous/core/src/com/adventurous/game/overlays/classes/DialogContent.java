package com.adventurous.game.overlays.classes;

import java.util.ArrayList;

/**
 * This class is used to store the content of a dialog.
 */
public class DialogContent {

    private final String title;
    private final ArrayList<String> content;

    /**
     * Constructor for the DialogContent class.
     * @param title The title of the dialog.
     */
    public DialogContent(String title){
        this.title = title;
        this.content = new ArrayList<>();
    }

    /**
     * Gets the title of the dialog.
     * @return The title of the dialog.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the content of the dialog.
     * @return The content of the dialog.
     */
    public ArrayList<String> getContent() {
        return content;
    }

    /**
     * Adds a line to the dialog.
     * @param text The line of text to add.
     */
    public DialogContent addLine(String text) {
        this.content.add(text);
        return this;
    }
}
