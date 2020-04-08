/**
 * Basic Rail class
 *
 * extends Rail only passes things left and right
 */
public class BasicRail extends Rail{

    /**
     * used to pass train from rail to rail
     *
     * checks if the train is ready to be passed then takes the train and alerts the next rail that is has the train
     */
    public void passTrain(){
        //checks that the rail is secured for the train
        if(currentMessage.getTrainID() == getSecuredPathForTrain()){
            //train moving left
            if(currentMessage.getDirection().equals(Direction.LEFT)){
                if(getRight().canMoveTrain()) {
                    currentMessage.getPath().pop();
                    setTrain(getRight().giveTrain(Direction.LEFT));
                    getLeft().addMessage(currentMessage);
                }else{
                    addMessage(currentMessage);
                }
            // train moving right
            }else{
                if(getLeft().canMoveTrain()) {
                    currentMessage.getPath().pop();
                    setTrain(getLeft().giveTrain(Direction.RIGHT));
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
     * checks if this rail has the train on it.
     * if it does have the train it it tell it that it has a new check point
     * if not passes the message along to the next track.
     */
    public void checkPoint(){
        if(currentMessage.getTrain().equals(getTrain())){
            getTrain().giveCheckPoint(currentMessage.getCheckPoint());
        }else{
            requestPathStation();
        }
    }

    /**
     * secures the rail for a train
     * and then passes the message to next train.
     * if it cant secure then gives its self the message again until it can
     */
    public void secureRailPath(){
        if(!securePath(currentMessage.getTrainID())){
            addMessage(currentMessage);
        }else {
            //System.out.println("secured rail");
            Direction dir = currentMessage.getPath().pop();
            if (dir.equals(Direction.RIGHT)) {
                getRight().addMessage(currentMessage);
            } else {
                getLeft().addMessage(currentMessage);
            }
        }
    }

    /**
     * just passes the message to the next rail
     */
    public void invalidPathStation(){
        requestPathStation();

    }

    /**
     * adds the direction to the direction list then sends to next rail that is part of the path
     *
     */
    public void foundPathStation(){
        if(currentMessage.getDirection().equals(Direction.RIGHT)) {
            currentMessage.getPath().push(Direction.LEFT);
            getRight().addMessage(currentMessage);

        }else{
            currentMessage.getPath().push(Direction.RIGHT);
            //System.out.println(currentMessage.getPath());

            getLeft().addMessage(currentMessage);
        }
    }

    /**
     * just passes the message to next track.
     *
     */
    public void requestPathStation(){

        if(currentMessage.getDirection().equals(Direction.RIGHT)) {
            getRight().addMessage(currentMessage);

        }else{
            getLeft().addMessage(currentMessage);
        }
    }

}
