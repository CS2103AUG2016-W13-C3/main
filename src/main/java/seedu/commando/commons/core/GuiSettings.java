package seedu.commando.commons.core;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Serializable class that contains the GUI settings.
 */
public class GuiSettings implements Serializable {

    private Double windowWidth = Config.DefaultWindowWidth;
    private Double windowHeight = Config.DefaultWindowHeight;
    private boolean isMaximized = false;
    private Point windowCoordinates = null;

    public GuiSettings() { }

    /**
     * Copy constructor.
     */
    public GuiSettings(GuiSettings guiSettings) {
        this.windowWidth = guiSettings.windowWidth;
        this.windowHeight = guiSettings.windowHeight;
        this.isMaximized = guiSettings.isMaximized;

        if (guiSettings.windowCoordinates != null) {
            this.windowCoordinates = new Point(guiSettings.windowCoordinates);
        }
    }

    public GuiSettings(double windowWidth, double windowHeight, int xPosition, int yPosition, boolean isMaximized) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.windowCoordinates = new Point(xPosition, yPosition);
        this.isMaximized = isMaximized;
    }

    public Double getWindowWidth() {
        return windowWidth;
    }

    public Double getWindowHeight() {
        return windowHeight;
    }
    
    public Point getWindowCoordinates() {
        return windowCoordinates;
    }
    
    public boolean getIsMaximized() {
        return isMaximized;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this){
            return true;
        }
        if (!(other instanceof GuiSettings)){ //this handles null as well.
            return false;
        }

        GuiSettings o = (GuiSettings)other;
        return Objects.equals(windowWidth, o.windowWidth)
                && Objects.equals(windowHeight, o.windowHeight)
                && Objects.equals(windowCoordinates, o.windowCoordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(windowWidth, windowHeight, windowCoordinates);
    }

    @Override
    public String toString(){
        return String.join(",",
            "Width: " + windowWidth,
            "Height: " + windowHeight,
            "Position: " + windowCoordinates
        );
    }
}
