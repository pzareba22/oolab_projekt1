package agh.ics.oop.proj1;


/*

    Tutaj zainspirowałem się następującym kodem na stackOverflow: https://stackoverflow.com/a/20445028

 */



public class SimulationEngine implements Runnable {

    public final int refreshTime;
    public final String engineName;
    private int dayCount;


    private volatile boolean isRunning;
    private final Object lock;

    SimulationEngine(String engineName, int refreshTime){
        this.engineName = engineName;
        this.refreshTime = refreshTime;
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
