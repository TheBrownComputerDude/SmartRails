/**
 * Light class
 * light is able to stop and hold train
 * light is used as a check point
 */
public class Light extends Rail {

    /**
     * used to pass train from rail to rail
     *
     * checks if the train is ready to be passed then takes the train and alerts the next rail that is has the train
     * also removes check point from trains list of check points
     */
    public void passTrain(){
        // checks if it is the correct train to be passed
        if(currentMessage.getTrainID() == getSecuredPathForTrain()){
            //going left
            if(currentMessage.getDirection().equals(Direction.LEFT)){
                if(getRight().canMoveTrain()) {
                    currentMessage.getPath().pop();
                    setTrain(getRight().giveTrain(Direction.LEFT));
                    getTrain().removeCheckPoint();
                    getLeft().addMessage(currentMessage);
                }else{
                    addMessage(currentMessage);
                }
            // going right
            }else{
                if(getLeft().canMoveTrain()) {
                    currentMessage.getPath().pop();
                    setTrain(getLeft().giveTrain(Direction.RIGHT));
                    getTrain().removeCheckPoint();
                    getRight().addMessage(currentMessage);
                }else{
                    addMessage(currentMessage);
                }
            }
        }else{
            addMessage(currentMessage);
        }
    }

    /**
     * checks if train is on rail then gives it a new check point if not passed message on to next rail
     */
    public void checkPoint(){
        if(currentMessage.getTrain().equals(getTrain())){
            getTrain().giveCheckPoint(currentMessage.getCheckPoint());
        }else{
            requestPathStation();
        }

    }

    /**
     * just calls request path to pass message along
     */
    public void invalidPathStation(){
        requestPathStation();
    }

    /**
     * adds direction to list and passes message along
     */
    public void foundPathStation(){
        if(currentMessage.getDirection().equals(Direction.RIGHT)) {
            currentMessage.getPath().push(Direction.LEFT);
            getRight().addMessage(currentMessage);
        }else{
            currentMessage.getPath().push(Direction.RIGHT);
            getLeft().addMessage(currentMessage);
        }
    }

    /**
     * just passes message along
     */
    public void requestPathStation(){

        if(currentMessage.getDirection().equals(Direction.RIGHT)) {
            getRight().addMessage(currentMessage);

        }else{
            getLeft().addMessage(currentMessage);
        }
    }

    /**
     * secures path for train and also sends a message out saying train has new checkpoint
     */
    public void secureRailPath(){
        //check if it secured the path
        if(!securePath(currentMessage.getTrainID())){
            addMessage(currentMessage);
        }else {
            // makes checkpoint message
            InboxMessage msg = new InboxMessage(Message.CHECKPOINT);
            msg.setCheckPoint(this);
            msg.setTrain(currentMessage.getTrain());
            Direction dir = currentMessage.getPath().pop();
            //sends message and passes to next rail
            if (dir.equals(Direction.RIGHT)) {
                msg.setDirection(Direction.LEFT);
                getLeft().addMessage(msg);
                getRight().addMessage(currentMessage);
            } else {
                msg.setDirection(Direction.RIGHT);
                getRight().addMessage(msg);
                getLeft().addMessage(currentMessage);
            }
        }
    }
}

