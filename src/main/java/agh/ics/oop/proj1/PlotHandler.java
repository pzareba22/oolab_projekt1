package agh.ics.oop.proj1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class PlotHandler {

    private final GraphicsContext gc;
    private final int pixelGap;
    private int xPos;
    private int maxValue;
    private ArrayList<Integer> plot1data; // liczba zwierzat
    private ArrayList<Integer> plot2data; // liczba roslin
    private ArrayList<Integer> plot3data; // sredni poziom energii
    private ArrayList<Integer> plot4data; // srednia dlugosc zycia
    private ArrayList<Integer> plot5data; // srednia liczba dzieci
    private final int width;
    private final int height;

    PlotHandler(GraphicsContext gc, int width, int height){
        this.gc = gc;
        this.pixelGap = width/100;
        this.width = width;
        this.height = height;
        this.plot1data = new ArrayList<>();
        this.plot2data = new ArrayList<>();
        this.plot3data = new ArrayList<>();
        this.plot4data = new ArrayList<>();
        this.plot5data = new ArrayList<>();
    }

    public void updatePlot(int d1, int d2, int d3, int d4, int d5){

        drawDataLine(plot1data, d1, Color.BLUE);
        drawDataLine(plot2data, d2, Color.LIGHTGREEN);
        drawDataLine(plot3data, d3, Color.ORANGE);
        drawDataLine(plot4data, d4, Color.SALMON);
        drawDataLine(plot5data, d5, Color.DEEPPINK);

        xPos = xPos+pixelGap;

        if(xPos >= width){
            xPos = 0;
            plot1data.clear();
            plot2data.clear();
            plot3data.clear();
            plot4data.clear();
            plot5data.clear();
            gc.clearRect(0, 0, width, height);
            maxValue = 0;
        }
    }

    public void drawFromScratch(){

        gc.clearRect(0, 0, width, height);

        gc.setStroke(Color.BLACK);

        maxValue = 0;

        int[] maximums = new int[5];
        maximums[0] = plot1data.size() > 0 ? Collections.max(plot1data) : 0;
        maximums[1] = plot2data.size() > 0 ? Collections.max(plot2data) : 0;
        maximums[2] = plot3data.size() > 0 ? Collections.max(plot3data) : 0;
        maximums[3] = plot4data.size() > 0 ? Collections.max(plot4data) : 0;
        maximums[4] = plot5data.size() > 0 ? Collections.max(plot5data) : 0;

        for (int i = 0; i < 5; i++) {
            maxValue = Math.max(maximums[i], maxValue);
        }

        drawDataFromScratch(plot1data, Color.BLUE);
        drawDataFromScratch(plot2data, Color.LIGHTGREEN);
        drawDataFromScratch(plot3data, Color.ORANGE);
        drawDataFromScratch(plot4data, Color.SALMON);
        drawDataFromScratch(plot5data, Color.DEEPPINK);

    }

    private int normalize(int x){
        return (int) ((height * 0.9) * x / maxValue);
    }

    private void drawDataFromScratch(ArrayList<Integer> data, Color color){
        xPos = 0;
        gc.setStroke(color);
        for (int i = 0; i < data.size() - 1; i++) {
            gc.strokeLine(xPos, height*0.9 - normalize(data.get(i)), xPos+pixelGap, height*0.9 - normalize(data.get(i+1)));
            xPos += pixelGap;
        }
    }

    private void drawDataLine(ArrayList<Integer> data, int newData, Color color){
        if(newData > maxValue){
            data.add(newData);
            drawFromScratch();
            maxValue = newData;
            return;
        }
        if(data.size() < 2){
            data.add(newData);
            return;
        }
        int y1 = data.get(data.size() - 1);
        gc.setStroke(color);
        gc.strokeLine(xPos, height*0.9 - normalize(y1), xPos+pixelGap, height*0.9 - normalize(newData));
        data.add(newData);

    }

}
