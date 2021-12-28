package agh.ics.oop.proj1;


import javafx.scene.Node;

public class GuiElement {
    public final Node node;
    public final int x, y, width, height;

    GuiElement(Node node, int x, int y, int width, int height){
        this.node = node;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

}
