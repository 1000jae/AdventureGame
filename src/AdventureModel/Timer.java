package AdventureModel;

/**
 * Class AdventureModel.Timer implements Singleton pattern
 * takes the time of the system to calculate elapsed time
 */
public class Timer {
    /**
     * The Timer instance.
     */
    private static Timer instance = null;

    /**
     * The time the Timer starts.
     */
    private long startTime;

    /**
     * The time the Timer ends.
     */
    private long endTime;

    /**
     * Timer Constructor.
     */
    private Timer(){}

    /**
     * Creates an instance of the AdventureModel.Timer class if there is no previously existing instance
     *
     * @return Timer instance
     */
    public static Timer getInstance() {
        if (instance == null) {
            instance = new Timer();
        }
        return instance;
    }

    /**
     * Starts the timer by taking the current time of the system
     */
    public void startTimer(){
        startTime = System.currentTimeMillis();
    }

    /**
     * Ends the timer by taking the current time of the system
     */
    public void endTimer(){
        endTime = System.currentTimeMillis();
    }

    /**
     * Calculates the time
     * @return long of the time elapsed in seconds
     */
    public String getTime(){
        return String.format("%.2f", (endTime - startTime)/1000.0);
    }

}
