package agh.ics.oop.proj1;


/*

    Tutaj zainspirowałem się następującym kodem na stackOverflow: https://stackoverflow.com/a/20445028

 */


import javafx.application.Platform;
import javafx.scene.canvas.Canvas;

import java.util.*;

public class SimulationEngine implements Runnable {

    public final int refreshTime;
    public final String engineName;
    private int dayCount;
    private final Map map;
    private final LinkedList<Animal> animalsList;
    private int magicCount;
    public final boolean isMagical;
    private final PlotHandler plotHanlder;


    private volatile boolean isRunning;
    private final Object lock;

    private final MainScreenController controller;

    SimulationEngine(String engineName, int refreshTime, Map map, int animalNumber, boolean isMagical, MainScreenController controller, Canvas plotCanvas){
        this.engineName = engineName;
        this.refreshTime = refreshTime;
        this.map = map;
        this.isRunning = true;
        this.lock = new Object();
        this.animalsList = new LinkedList<>();
        this.magicCount = 0;
        this.isMagical = isMagical;
        this.controller = controller;
        this.plotHanlder = new PlotHandler(plotCanvas.getGraphicsContext2D(), (int)plotCanvas.getWidth(), (int)plotCanvas.getHeight());
        for (int i = 0; i < animalNumber; i++) {
            Random random = new Random();
            HashSet<Vector2d> positions = new HashSet<>();
            while(true){
                int x = random.nextInt(this.map.width);
                int y = random.nextInt(this.map.height);
                Vector2d position = new Vector2d(x, y);
                if(!positions.contains(position)){
                    positions.add(position);
                    Animal animal = new Animal(position, map.startEnergy, this.map, null);
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
                Platform.runLater( () -> AlertWindowHandler.showInfoAlert("Symulacja sie skonczyla", "", ""));
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

            // ruchy zwierzat
            for(Animal animal: animalsList){
                animal.move();
                animal.decreaseEnergy(1);
                if(animal.getEnergy() <= 0){
                    toRemove.add(animal);
                    animal.deathDay = dayCount;
                    animal.isDead = true;
                    if(animal == controller.watchedAnimal)
                        controller.updateWatchedAnimal(dayCount);
                }
            }
            Platform.runLater(map::show);
            //usuwanie zwłok
            map.removeDead();
            Platform.runLater(map::show);
            //zjadanie trawy
            map.eatGrass();
            Platform.runLater(map::show);

            //rozmnażanie zwierząt
            ArrayList<Animal> animalsToBreed = map.selectAnimalsForBreeding();
            for (int i = 0; i < animalsToBreed.size(); i+=2) {
                Animal child = animalsToBreed.get(i).breed(animalsToBreed.get(i+1));
                animalsList.add(child);
                map.place(child);
            }
            Platform.runLater(map::show);

            map.generateGrass();
            Platform.runLater(map::show);

            animalsList.removeAll(toRemove);
            toRemove.clear();

            if(animalsList.size() == 5 && magicCount < 3 && isMagical){
                magic();
            }

            if(controller.watchedAnimal != null){
                controller.updateWatchedAnimal(-1);
            }

            Platform.runLater(() -> plotHanlder.updatePlot(animalsList.size(), map.getGrassCount()));
//            plotHanlder.addData(animalsList.size(), map.getGrassCount());
//            Platform.runLater(() -> plotHanlder.drawFromScratch());



            dayCount += 1;

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

    private void magic(){
        magicCount += 1;
        HashSet<Vector2d> positions = new HashSet<>();
        LinkedList<Animal> newAnimals = new LinkedList<>();
        Random random = new Random();
        for(Animal animal : animalsList){
            positions.add(animal.getPosition());
        }
        for(Animal animal : animalsList){
            while(true){
                int x = random.nextInt(map.width);
                int y = random.nextInt(map.height);
                Vector2d position = new Vector2d(x, y);
                if(!positions.contains(position)){
                    positions.add(position);
                    Animal newAnimal = new Animal(position, map.startEnergy, map, Arrays.copyOf(animal.genotype, 32));
                    newAnimals.add(newAnimal);
                    map.place(newAnimal);
                    break;
                }
            }
        }
        animalsList.addAll(newAnimals);
        for(Animal animal : newAnimals){
            map.place(animal);
        }
        Platform.runLater(() -> AlertWindowHandler.showInfoAlert("Wydarzyla sie magia", "Pojawilo sie 5 nowych zwierzat", ""));
    }

}
