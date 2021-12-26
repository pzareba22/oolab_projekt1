package agh.ics.oop.proj1;

public abstract class MapElement {
    private Vector2d position;

    MapElement(int x, int y){
        this.position = new Vector2d(x, y);
    }


    public Vector2d getPosition() {
        return position;
    }

}
