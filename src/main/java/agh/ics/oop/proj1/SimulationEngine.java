package agh.ics.oop.proj1;


/*

    Tutaj zainspirowałem się następującym kodem na stackOverflow: https://stackoverflow.com/a/20445028

 */


import javafx.application.Platform;

public class SimulationEngine implements Runnable {

    public final int refreshTime;
    public final String engineName;
    private int dayCount;
    private final Map map;


    private volatile boolean isRunning;
    private final Object lock;

    SimulationEngine(String engineName, int refreshTime, Map map){
        this.engineName = engineName;
        this.refreshTime = refreshTime;
        this.map = map;
        this.isRunning = true;
        this.lock = new Object();
    }

    @Override
    public void run() {

        while(true){
            synchronized (lock) {
                while (!isRunning) {
                    try {
                        lock.wait();
                    }
                    catch (InterruptedException e){

                    }
                }
            }
            try{
                Thread.sleep(refreshTime);
            } catch (InterruptedException e) {
            }

            map.generateGrass();
            Platform.runLater(() -> map.show());
            System.out.println(engineName + ":\tYet another day passes (" + ++dayCount + ")");


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
        boolean res = isRunning;
        return res;
    }

}
