import java.util.LinkedList;

/**
 * InboxMessage class
 * creates a message in order to pass information from rail to rail
 */

public class InboxMessage {

    private Station station;
    private Train train;
    private LinkedList<Direction> path;
    private Direction direction;
    private Station startStation;
    private Message type;
    private LinkedList<Integer> numPaths;
    private int trainID;
    private Rail checkPoint;

    /**
     * constructor for message
     * @param type of message it is
     */
    public InboxMessage(Message type){
        this.type = type;
    }

    /**
     * @return the type of message it is
     */
    public Message getType() {
        return type;
    }

    /**
     * sets numPaths
     * @param numPaths list to be set
     */
    public void setNumPaths(LinkedList<Integer> numPaths){
        this.numPaths = numPaths;
    }

    /**
     *
     * @return the list containing num of Paths
     */
    public LinkedList<Integer> getNumPaths(){
        return numPaths;
    }

    /**
     * sets train id
     * @param trainID to be set
     */
    public void setTrainID(int trainID){
        this.trainID = trainID;
    }

    /**
     * returns id of train
     * @return trainID
     */
    public int getTrainID() {
        return trainID;
    }

    /**
     * rail to be set as a check point
     * @param rail to be set as the check point
     */
    public void setCheckPoint(Rail rail){
        this.checkPoint = rail;
    }

    /**
     * returns checkpoint
     * @return checkpoint
     */
    public Rail getCheckPoint() {
        return checkPoint;
    }

    /**
     *
     * @return start station
     */
    public Station getStartStation() {
        return startStation;
    }

    /**
     * sets start station
     * @param startStation
     */
    public void setStartStation(Station startStation) {
        this.startStation = startStation;
    }

    /**
     * returns direction the message is going
     * @return direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * sets direction for the message to travel
     * @param direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     *
     * @return station
     */
    public Station getStation() {
        return station;
    }

    /**
     *
     * @return train
     */
    public Train getTrain() {
        return train;
    }

    /**
     *
     * @return path
     */
    public LinkedList<Direction> getPath() {
        return path;
    }

    /**
     * sets path
     * @param path
     */
    public void setPath(LinkedList<Direction> path) {
        this.path = path;
    }

    /**
     * sets station
     * @param station
     */
    public void setStation(Station station) {
        this.station = station;
    }

    /**
     * sets train
     * @param train
     */
    public void setTrain(Train train) {
        this.train = train;
    }

    /**
     * adds direction to path list
     * @param dir
     */
    public void addToPath(Direction dir){
        path.push(dir);
    }
}
