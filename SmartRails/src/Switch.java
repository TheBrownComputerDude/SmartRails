import java.util.LinkedList;

/**
 * Switch Class
 * Switch has left and right rail as well as an alternate rail
 */

public class Switch extends Rail {

    char switchNum;
    int row;
    int col;
    /**
     * receives train and passes message to next rail its suppose to go to
     */
    public void passTrain(){
        // check if its the right train
        if(currentMessage.getTrainID() == getSecuredPathForTrain()){
            // train going left
            if(currentMessage.getDirection().equals(Direction.LEFT)){
                //switch has 2 rails going left
                if(getDirectioinOfSwitch().equals(Direction.DOWNLEFT) || getDirectioinOfSwitch().equals(Direction.UPLEFT)){
                    if(getRight().canMoveTrain()) {
                        Direction x = currentMessage.getPath().pop();
                        setTrain(getRight().giveTrain(Direction.LEFT));
                        //train needs to go to the alternate rail
                        if(x.equals(Direction.DOWNLEFT) || x.equals(Direction.UPLEFT)){
                            getAlternateRail().addMessage(currentMessage);
                        }else {
                            getLeft().addMessage(currentMessage);
                        }
                    }else{
                        addMessage(currentMessage);
                    }
                 //switch has 1 rail left
                }else{
                    currentMessage.getPath().pop();
                    if(getAlternateRail().getTrain() != null && getAlternateRail().getTrain().equals(currentMessage.getTrain())){
                        setTrain(getAlternateRail().giveTrain(getDirectioinOfSwitch() == Direction.DOWNRIGHT ? Direction.UPLEFT : Direction.DOWNLEFT));
                    }else{
                        setTrain(getRight().giveTrain(Direction.LEFT));
                    }
                    getLeft().addMessage(currentMessage);
                }
            // trains going right
            }else{
                //switch has 2 rails going right
                if(getDirectioinOfSwitch().equals(Direction.DOWNRIGHT) || getDirectioinOfSwitch().equals(Direction.UPRIGHT)){
                    if(getLeft().canMoveTrain()) {
                        Direction x = currentMessage.getPath().pop();
                        setTrain(getLeft().giveTrain(Direction.RIGHT));
                        //switch sends message to alternate rail
                        if(x.equals(Direction.DOWNRIGHT) || x.equals(Direction.UPRIGHT)){
                            getAlternateRail().addMessage(currentMessage);
                        }else {
                            getRight().addMessage(currentMessage);
                        }
                    }else{
                        addMessage(currentMessage);
                    }
                //switch has 1 rail going right
                }else{
                    currentMessage.getPath().pop();
                    if(getAlternateRail().getTrain() != null && getAlternateRail().getTrain().equals(currentMessage.getTrain())){
                        setTrain(getAlternateRail().giveTrain(getDirectioinOfSwitch() == Direction.DOWNLEFT ? Direction.UPRIGHT : Direction.DOWNRIGHT));
                    }else{
                        setTrain(getLeft().giveTrain(Direction.RIGHT));
                    }
                    getRight().addMessage(currentMessage);
                }
            }
        }else{
            addMessage(currentMessage);
        }
    }

    /**
     * checks if rail has train if not sends message to next rail
     */
    void checkPoint(){
        if(currentMessage.getTrain().equals(getTrain())){
            getTrain().giveCheckPoint(currentMessage.getCheckPoint());
        }else{
            invalidPathStation();
        }

    }

    /**
     * switches current rail between alternate one
     */
    private void switchPath(){
        if(getDirectioinOfSwitch().equals(Direction.DOWNRIGHT) || getDirectioinOfSwitch().equals(Direction.UPRIGHT)) {
            currentRail = currentRail == getRight() ? getAlternateRail() : getRight();
        }else{
            currentRail = currentRail == getLeft() ? getAlternateRail() : getLeft();
        }
    }

    /**
     * secures rail for train and passes message to next rail
     */
    void secureRailPath(){
        if(!securePath(currentMessage.getTrainID())){
            addMessage(currentMessage);
        }else {
            Direction dir = currentMessage.getPath().pop();

            if (dir.equals(Direction.RIGHT)) {
                getRight().addMessage(currentMessage);
            } else if (dir.equals(Direction.LEFT)) {
                getLeft().addMessage(currentMessage);
            } else {
                getAlternateRail().addMessage(currentMessage);
            }
        }
    }

    /**
     * passes message to next rail also increases the number of paths the train has
     */
    void requestPathStation(){
        // switch has 2 rails going right
        if(getDirectioinOfSwitch().equals(Direction.DOWNRIGHT) || getDirectioinOfSwitch().equals(Direction.UPRIGHT)){
            //message is going right sends message to both rails
            if(currentMessage.getDirection().equals(Direction.RIGHT) ) {
                for(int i = 0; i < 2; i++){
                    if(currentRail != null && currentRail.equals(getAlternateRail())){
                        int x = currentMessage.getNumPaths().pop();
                        currentMessage.getNumPaths().push(x + 1);
                        currentRail.addMessage(currentMessage);
                    }else if(currentRail != null && currentRail.equals(getRight())){
                        currentRail.addMessage(currentMessage);
                    }
                    switchPath();
                }
            //message if going left
            }else{
                if(getLeft() != (null)) getLeft().addMessage(currentMessage);
            }
         // switch has 2 rails going left
        }else if(getDirectioinOfSwitch().equals(Direction.DOWNLEFT) || getDirectioinOfSwitch().equals(Direction.UPLEFT)){
            //message is going left sends to both rails
            if(currentMessage.getDirection().equals(Direction.LEFT)){
                for(int i = 0; i < 2; i++){
                    if(currentRail != null && currentRail.equals(getAlternateRail())){
                        int x = currentMessage.getNumPaths().pop();
                        currentMessage.getNumPaths().push(x + 1);
                        currentRail.addMessage(currentMessage);
                    }else if(currentRail != null && currentRail.equals(getLeft())){
                        currentRail.addMessage(currentMessage);
                    }
                    switchPath();
                }
            //message is going right
            }else{
                getRight().addMessage(currentMessage);
            }
        }

    }

    /**
     * just passes message to next rail
     */
    void invalidPathStation(){
        //switch has 2 rails right
        if(getDirectioinOfSwitch().equals(Direction.DOWNRIGHT) || getDirectioinOfSwitch().equals(Direction.UPRIGHT)){
            //message is going right
            if(currentMessage.getDirection().equals(Direction.RIGHT) ) {
                for(int i = 0; i < 2; i++){
                    if(currentRail != null && currentRail.equals(getAlternateRail())){
                        currentRail.addMessage(currentMessage);
                    }else if(currentRail != null && currentRail.equals(getRight())){
                        currentRail.addMessage(currentMessage);
                    }
                    switchPath();
                }
             //message if going left
            }else{
                if(getLeft() != (null)) getLeft().addMessage(currentMessage);
            }
         //switch has 2 rails left
        }else if(getDirectioinOfSwitch().equals(Direction.DOWNLEFT) || getDirectioinOfSwitch().equals(Direction.UPLEFT)){
            //message is going left
            if(currentMessage.getDirection().equals(Direction.LEFT)){
                for(int i = 0; i < 2; i++){
                    if(currentRail != null && currentRail.equals(getAlternateRail())){
                        currentRail.addMessage(currentMessage);
                    }else if(currentRail != null && currentRail.equals(getLeft())){
                        currentRail.addMessage(currentMessage);
                    }
                    switchPath();
                }
            //message is going right
            }else{
                getRight().addMessage(currentMessage);
            }
        }
    }

    /**
     * adds direction to list and passes to next rail.
     * Also has 2 makes new message to send in both forks because of memory issues
     */
    void foundPathStation(){
        // two path going right
        if(getDirectioinOfSwitch().equals(Direction.DOWNRIGHT) || getDirectioinOfSwitch().equals(Direction.UPRIGHT)){
            //if the direction of message is right
            if(currentMessage.getDirection().equals(Direction.RIGHT)) {
                //send message to both paths going right
                LinkedList<Direction> x = new LinkedList<>();
                LinkedList<Direction> y = new LinkedList<>();
                x.addAll(currentMessage.getPath());
                y.addAll(currentMessage.getPath());
                for (int i = 0; i < 2; i++) {
                    InboxMessage msg = new InboxMessage(Message.FoundPath);
                    msg.setStation(currentMessage.getStartStation());
                    msg.setTrain(currentMessage.getTrain());
                    msg.setDirection(currentMessage.getDirection().equals(Direction.RIGHT) ? Direction.RIGHT : Direction.LEFT);
                    //current rail is the right rail and not null
                    if (currentRail != (null) && currentRail.equals(getRight())) {
                        //push new direction on message and pass it
                        x.push(Direction.LEFT);
                        msg.setPath(x);
                        currentRail.addMessage(msg);
                    } else if (currentRail != (null) && currentRail.equals(getAlternateRail())){
                        if (getDirectioinOfSwitch().equals(Direction.UPRIGHT)) {
                            y.push(Direction.DOWNLEFT);
                        } else if (getDirectioinOfSwitch().equals(Direction.DOWNRIGHT)) {
                            y.push(Direction.UPLEFT);
                        }
                        msg.setPath(y);
                        currentRail.addMessage(msg);
                    }
                    switchPath();
                }
            }else{
                if(getLeft() != (null)) {
                    currentMessage.getPath().push(Direction.RIGHT);
                    getLeft().addMessage(currentMessage);
                }
            }
            //two path going left
        }else if(getDirectioinOfSwitch().equals(Direction.DOWNLEFT) || getDirectioinOfSwitch().equals(Direction.UPLEFT)){
            //sends message to both paths left
            if(currentMessage.getDirection().equals(Direction.LEFT)){
                LinkedList<Direction> x = new LinkedList<>();
                LinkedList<Direction> y = new LinkedList<>();
                x.addAll(currentMessage.getPath());
                y.addAll(currentMessage.getPath());
                for (int i = 0; i < 2; i++) {
                    InboxMessage msg = new InboxMessage(Message.FoundPath);
                    msg.setStation(currentMessage.getStartStation());
                    msg.setTrain(currentMessage.getTrain());
                    msg.setDirection(currentMessage.getDirection().equals(Direction.RIGHT) ? Direction.RIGHT : Direction.LEFT);
                    if (currentRail != (null) && currentRail.equals(getLeft())) {

                        x.push(Direction.RIGHT);
                        msg.setPath(x);
                        currentRail.addMessage(msg);
                    } else if (currentRail != (null) && currentRail.equals(getAlternateRail())){
                        if (getDirectioinOfSwitch().equals(Direction.UPLEFT)) {
                            y.push(Direction.DOWNRIGHT);
                            msg.setPath(y);
                            currentRail.addMessage(msg);
                        } else if (getDirectioinOfSwitch().equals(Direction.DOWNLEFT)) {
                            y.push(Direction.UPRIGHT);
                            msg.setPath(y);
                            currentRail.addMessage(msg);
                        }
                    }
                    switchPath();
                }
            // sends right
            }else{
                if(getRight() != (null)) {
                    currentMessage.getPath().push(Direction.LEFT);
                    getRight().addMessage(currentMessage);
                }
            }
        }
    }

    /*
    Sets identifiers for book keeping
    Input:
        c: Char for switch identifier(OFF limit chars: S, R, L, s, l, r)
        row: row location for direction setting
        col: col location for direction setting
     */
    void setIdentity(char c, int row, int col){
        switchNum = c;
        this.row = row;
        this.col = col;
    }

    char getSwitchNum(){return switchNum;}
}
