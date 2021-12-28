package agh.ics.oop.proj1;


/*

    Tutaj zainspirowałem się następującym kodem na stackOverflow: https://stackoverflow.com/a/20445028

 */


import javafx.application.Platform;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class SimulationEngine implements Runnable {

    public final int refreshTime;
    public final String engineName;
    private int dayCount;
    private final Map map;
    private final LinkedList<Animal> animalsList;


    private volatile boolean isRunning;
    private final Object lock;

    SimulationEngine(String engineName, int refreshTime, Map map, int animalNumber){
        this.engineName = engineName;
        this.refreshTime = refreshTime;
        this.map = map;
        this.isRunning = true;
        this.lock = new Object();
        this.animalsList = new LinkedList<>();
        for (int i = 0; i < animalNumber; i++) {
            Random random = new Random();
            HashSet<Vector2d> positions = new HashSet<>();
            while(true){
                int x = random.nextInt(this.map.width);
                int y = random.nextInt(this.map.height);
                Vector2d position = new Vector2d(x, y);
                if(!positions.contains(position)){
                    positions.add(position);
                    Animal animal = new Animal(position, map.startEnergy, this.map);
                    animal.generateRandomGenotype();
                    this.animalsList.add(animal);
                    this.map.place(animal);
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        LinkedList<Animal> toRemove = new LinkedList<>();
        Platform.runLater(map::show);
        while(true){
            if(animalsList.isEmpty()){
                break;
            }
            synchronized (lock) {
                while (!isRunning) try {
                    lock.wait();
                } catch (InterruptedException ignored) {
                }
            }
            try{
                Thread.sleep(refreshTime);
            } catch (InterruptedException ignored) {}

            for(Animal animal: animalsList){
                animal.move();
                animal.decreaseEnergy(1);
                if(animal.getEnergy() <= 0){
                    toRemove.add(animal);
                }
            }
            Platform.runLater(map::show);
            map.eatGrass();
            Platform.runLater(map::show);
            map.generateGrass();
            Platform.runLater(map::show);
            map.removeDead();
            Platform.runLater(map::show);

            animalsList.removeAll(toRemove);

        }
    }

    public void setRunning(boolean flag) {
        synchronized (lock){
            isRunning = flag;
            if(isRunning){
                lock.notify();
            }
        }
    }

    public boolean getRunning(){
        return isRunning;
    }

}
