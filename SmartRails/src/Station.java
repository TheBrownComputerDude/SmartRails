import java.util.LinkedList;

/**
 * Station class
 *
 * Station is starting rail for trains
 */
public class Station extends Rail {

    int stationNum;

    String stationCord;
    int col;
    int row;
    /**
     * receives train, should be the end location for the train
     */
   public void passTrain(){
        if(getTrain() != null && currentMessage.getTrain().equals(getTrain())){
            //station is the starting station for train
            if(canMoveTrain()) {
                Direction x = currentMessage.getPath().pop();
                if (x.equals(Direction.RIGHT)) {
                    getRight().addMessage(currentMessage);
                } else {
                    getLeft().addMessage(currentMessage);
                }
            }else{
                addMessage(currentMessage);
            }
        }else{
            //receives train from left
            if (currentMessage.getDirection().equals(Direction.RIGHT)) {
                if(getLeft().canMoveTrain()) {
                    setTrain(getLeft().giveTrain(Direction.RIGHT));
                 //   currentMessage.getPath().pop();
                    getTrain().removeCheckPoint();
                    //System.out.println("moved to final");
                    //layout.getChildren().remove(getTrain().getIv());
                    clearTrain();
                    clearSecuredPath();
                }else{
                    addMessage(currentMessage);
                }
            } else {
                //receives train from right
                if(getRight().canMoveTrain()) {
                    setTrain(getRight().giveTrain(Direction.LEFT));
                    //currentMessage.getPath().pop();
                    getTrain().removeCheckPoint();
                    //System.out.println("moved to final");
                    //layout.getChildren().remove(getTrain().getIv());
                    clearTrain();
                    clearSecuredPath();
                }else{
                    addMessage(currentMessage);
                }
            }
        }
    }
    /**
     * checks if train is on rail then gives it a new check point
     */
    public void checkPoint(){
        if(currentMessage.getTrain().equals(getTrain())){
            getTrain().giveCheckPoint(currentMessage.getCheckPoint());
        }else{
            if(getLeft() != null && currentMessage.getDirection().equals(Direction.LEFT) ){
                getLeft().addMessage(currentMessage);
            }else if(getRight() != null && currentMessage.getDirection().equals(Direction.RIGHT)){
                getRight().addMessage(currentMessage);
            }
        }

    }
    /**
     * secures path for train and also sends a message out saying train has new checkpoint
     */
    public void secureRailPath(){
        if(!securePath(currentMessage.getTrainID())){
            addMessage(currentMessage);
        }else {
            //System.out.println("secured station");
            if (currentMessage.getPath().size() > 0) {
                Direction dir = currentMessage.getPath().pop();
                if (dir.equals(Direction.RIGHT)) {
                    getRight().addMessage(currentMessage);
                } else {
                    getLeft().addMessage(currentMessage);
                }
            }else{
                InboxMessage msg = new InboxMessage(Message.CHECKPOINT);
                msg.setCheckPoint(this);
                msg.setTrain(currentMessage.getTrain());
                if(currentMessage.getDirection().equals(Direction.RIGHT)){
                    msg.setDirection(Direction.LEFT);
                    getLeft().addMessage(msg);
                }else{
                    msg.setDirection(Direction.RIGHT);
                    getRight().addMessage(msg);
                }
                // make new message here
            }
        }
    }

    /**
     * checks if train is at station if it is gives invalid path to train
     */
    public void invalidPathStation(){
        //currentMessage.getPath().removeLast();
        if(currentMessage.getTrain().equals(getTrain())){
            getTrain().noPath();
        }
    }

    /**
     * gives valid path to train
     */
    public void foundPathStation(){
        if(currentMessage.getTrain().equals(getTrain())){
            getTrain().givePath(currentMessage.getPath());
        }
    }

    /**
     * checks if station is end station or start station
     * if start station passes message
     * if end station checks if it is the right end station
     * if its the right one sends a new message
     */
    public void requestPathStation(){
        // is start station
        if(currentMessage.getStartStation().equals(this)){
            //System.out.println("found station");
            if(currentMessage.getDirection().equals(Direction.RIGHT)){
                if(getRight() != null){
                    getRight().addMessage(currentMessage);
                }else{
                    getTrain().noPath();
                }
            }else{
                if(getLeft()!= null){
                    getLeft().addMessage(currentMessage);

                }else{
                    getTrain().noPath();
                }
            }
        }else{
            //is the correct end station
            if(currentMessage.getStation().equals(this)) {
                //System.out.println("found station");
                LinkedList<Direction> path= new LinkedList<>();
                InboxMessage msg = new InboxMessage(Message.FoundPath);
                msg.setStation(currentMessage.getStartStation());
                msg.setPath(path);
                msg.setTrain(currentMessage.getTrain());
                if (currentMessage.getDirection().equals(Direction.RIGHT)){
                    msg.getPath().push(Direction.RIGHT);
                    msg.setDirection(Direction.LEFT);
                    getLeft().addMessage(msg);
                }else{
                    msg.getPath().push(Direction.LEFT);
                    msg.setDirection(Direction.RIGHT);
                    getRight().addMessage(msg);
                }
            // is incorrect end station
            }else{

                currentMessage.getTrain().noPath();
            }
        }
    }

    /*
    Sets identifiers for book keeping
    Input:
        stationNum: station number
        row: row location for direction setting
        col: col location for direction setting
     */
    void setIdentity( int row, int col){
        this.stationCord = "(" + (row + 1) + "," + (col+1) + ")";
        this.row = row;
        this.col = col;
    }
}
