package agh.ics.oop.proj1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class PlotHandler {

    private final GraphicsContext gc;
    private final int pixelGap;
    private int xPos;
    private int maxValue;
    private ArrayList<Integer> plot1data;
    private ArrayList<Integer> plot2data;
    private final int width;
    private final int height;

    private int debugCount = 0;

    PlotHandler(GraphicsContext gc, int width, int height){
        this.gc = gc;
        this.pixelGap = width/100;
        this.width = width;
        this.height = height;
        this.plot1data = new ArrayList<>();
        this.plot2data = new ArrayList<>();
        System.out.println(width);
    }

    public void updatePlot(int animalData, int grassData){

        if(Math.max(animalData, grassData) > maxValue){
            maxValue = Math.max(animalData, grassData);
            plot1data.add(animalData);
            plot2data.add(grassData);
            drawFromScratch();
            return;
        }

        if(plot1data.size() < 2){
            plot1data.add(animalData);
            plot2data.add(grassData);
            return;
        }

        int y1 = normalize(plot1data.get(plot1data.size()-1));
        gc.setStroke(Color.BLUE);
        gc.strokeLine(xPos, height - y1, xPos+pixelGap, height - normalize(animalData));
        plot1data.add(animalData);

        y1 = normalize(plot2data.get(plot2data.size()-1));
        gc.setStroke(Color.GREEN);
        gc.strokeLine(xPos, height - y1, xPos+pixelGap, height - normalize(grassData));
        plot2data.add(grassData);

        xPos = xPos+pixelGap;

        if(xPos >= width){
            xPos = 0;
            plot1data.clear();
            plot2data.clear();
            gc.clearRect(0, 0, width, height);
        }
    }

    public void drawFromScratch(){

        gc.clearRect(0, 0, width, height);

        gc.setStroke(Color.BLACK);


        xPos = 0;
        gc.setStroke(Color.BLUE);
        for (int i = 0; i < plot1data.size()-1; i++) {
            gc.strokeLine(xPos, height - normalize(plot1data.get(i)), xPos+pixelGap, height - normalize(plot1data.get(i+1)));
            xPos += pixelGap;
        }
        gc.setStroke(Color.GREEN);
        xPos = 0;
        for (int i = 0; i < plot2data.size()-1; i++) {
            gc.strokeLine(xPos, height - normalize(plot2data.get(i)), xPos+pixelGap, height - normalize(plot2data.get(i+1)));
            xPos += pixelGap;
        }

    }

    private int normalize(int x){
        return (int) ((height * 0.9) * x / maxValue);
    }

    public void addData(int animalData, int grassData){
        if(plot1data.size() > 120){
            plot1data.clear();
            plot2data.clear();
        }
        maxValue = Math.max(maxValue, Math.max(animalData, grassData));
        plot1data.add(animalData);
        plot2data.add(grassData);
    }



}
