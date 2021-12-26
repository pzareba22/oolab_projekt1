package agh.ics.oop.proj1;

public class SimulationEngine implements Runnable {

    public final int refreshTime;
    public final String engineName;
    private int dayCount;

    SimulationEngine(String engineName, int refreshTime){
        this.engineName = engineName;
        this.refreshTime = refreshTime;
    }

    @Override
    public void run() {

        while(true){
            System.out.println(engineName + ":\tYet another day passes (" + Integer.toString(++dayCount) + ")");
            try {
                Thread.sleep(refreshTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
