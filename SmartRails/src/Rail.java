import javafx.scene.layout.Pane;

import java.util.LinkedList;

/**
 * Rail holds information for all the rail types
 * big thing it does is keep track of the messages
 */

public abstract class Rail extends Thread {

    private Rail left;
    private Rail right;
    private Rail alternatePath;
    private Train train;
    protected Direction directioinOfSwitch;
    private boolean running = true;
    protected Rail currentRail;
    private int securedPathForTrain = 0;
    private LinkedList<InboxMessage> inbox = new LinkedList<>();
    public InboxMessage currentMessage;
    private char switchID;

    abstract void requestPathStation();
    abstract void invalidPathStation();
    abstract void foundPathStation();
    abstract void secureRailPath();
    abstract void checkPoint();
    abstract void passTrain();

    /**
     * called when thread starts keep checking for messages until the program stops running
     * must be in class so it can access its methods
     */
    public void run() {
        while (amRunning()){
            if(getNumMessages() > 0){
                currentMessage = getMessage();
                //System.out.println("switch");
                //System.out.println(currentMessage.getPath());
                processMessage();
            }else{
                try {
                    sleep(100);
                }catch (Exception e){}
            }
        }
    }

    /**
     * process the different message types and calls the correct method
     */
    private void processMessage(){
        if(currentMessage.getType().equals(Message.RequestPath)){
            requestPathStation();
        }else if(currentMessage.getType().equals(Message.InvalidPath)){
            invalidPathStation();
        }else if(currentMessage.getType().equals(Message.FoundPath)){
            foundPathStation();
        }else if(currentMessage.getType().equals(Message.SecurePath)){
            secureRailPath();
        }else if(currentMessage.getType().equals(Message.CHECKPOINT)){
            checkPoint();
        }else if(currentMessage.getType().equals(Message.PASSTRAIN)){
            passTrain();
        }
    }
    public void setSwitchID(char switchID){
        this.switchID = switchID;
    }
    public char getSwitchID(){
        return switchID;
    }

    /**
     * returns next message in inbox
     * @return message
     */
    private synchronized InboxMessage getMessage(){
        return inbox.pop();
    }

    /**
     * adds message to inbox
     * @param x
     */
    synchronized void addMessage(InboxMessage x){
        inbox.addLast(x);
    }

    /**
     *
     * @return size of inbox
     */
    int getNumMessages(){
        return inbox.size();
    }

    /**
     *
     * @return the direction of the switch if the rail is a switch
     */
    Direction getDirectioinOfSwitch(){
        return directioinOfSwitch;
    }

    /**
     * method for switch
     * @return the rail the switch is currently on
     */
    public Rail getCurrentRail() {
        return currentRail;
    }

    /**
     *
     * @return the rail left to this rail
     */
    Rail getLeft() {
        return left;
    }

    /**
     *
     * @return the rail right to this rail
     */
    Rail getRight() {
        return right;
    }

    /**
     * sets secured path back to 0
     */
    public void clearSecuredPath(){
        securedPathForTrain = 0;
    }

    /**
     *
     * @return train on this rail
     */
    Train getTrain() {
        return train;
    }

    /**
     * sets left and right rails
     * @param left
     * @param right
     */
    void setRails(Rail left, Rail right){
        this.left = left;
        this.right = right;
    }

    /*
    Sets left rail reference
    Input:
        left: rail to be set as left reference
    Output: None
     */
    void setLeftRail(Rail left){
        this.left = left;
    }

    /*
    Sets right rail reference
    Input:
        right: rail to be set as right reference
    Output: None
     */
    void setRightRail(Rail right){
        this.right = right;
    }
    /**
     * sets train for track and calls train to set its rail to this
     * @param train
     */
    void setTrain(Train train){
        //System.out.println("train set");
        this.train = train;
        train.setRail(this);

    }

    /**
     *
     * @return is program is running
     */
    boolean amRunning(){
        return running;
    }

    /**
     * makes threead stop running
     */
    void stopRail(){
        running = false;
    }

    /**
     * sets alternates rail for switch
     * @param rail
     */
    public void setAlternate(Rail rail){
        this.alternatePath = rail;
    }

    /**
     * gets the alternate rail for switch
     * @return
     */
    public Rail getAlternateRail(){
        return alternatePath;
    }

    /**
     * sets the alternate rail and the direction for the switch
     * @param path
     * @param directionOfSwitch
     */
    public void setswitchPath(Rail path, Direction directionOfSwitch){
        this.directioinOfSwitch = directionOfSwitch;
        setAlternate(path);
        if(directionOfSwitch.equals(Direction.DOWNLEFT) || directionOfSwitch.equals(Direction.UPLEFT)){
            currentRail = getLeft();
        }else{
            currentRail = getRight();
        }
    }

    /**
     * sets rail so that it is secured for train based off the trains id
     * @param trainID
     * @return
     */
    public boolean securePath(int trainID){
        if(securedPathForTrain == 0){
            securedPathForTrain = trainID;
            return true;
        }
        return false;
    }

    /**
     *
     * @return the train id for what the rail is secured for
     */
    public int getSecuredPathForTrain(){
        return securedPathForTrain;
    }
    public boolean canMoveTrain(){
        if(train != null){
            return train.haveCheckPoint();
        }
        else{
            return false;
        }
    }
    public void clearTrain(){
        train.getIv().imageProperty().set(null);
        train.setIv(null);
        train = null;
        clearSecuredPath();
    }

    /**
     *
     * @return train on rail and sets train to empty
     */
    public synchronized Train giveTrain(Direction dir){
        Train t = getTrain();
        clearSecuredPath();
        train = null;
        //System.out.println(dir.toString());
        if(dir.equals(Direction.RIGHT)){
            moveTrainIV(1,0, t);
        }else if(dir.equals(Direction.DOWNRIGHT)){
            moveTrainIV(1,1, t);
        }else if(dir.equals(Direction.UPRIGHT)){
            moveTrainIV(1,-1, t);
        }else if(dir.equals(Direction.LEFT)){
            moveTrainIV(-1,0, t);
        }else if(dir.equals(Direction.UPLEFT)){
            moveTrainIV(-1,-1, t);
        }else if(dir.equals(Direction.DOWNLEFT)){
            moveTrainIV(-1,1, t);
        }

        //System.out.println("moving train");

        return t;
    }
    private void moveTrainIV(int x, int y, Train t){

        for(int i = 0; i < 80; i++){
            t.getIv().setTranslateX(t.getIv().getTranslateX() + x);
            t.getIv().setTranslateY(t.getIv().getTranslateY() + y);
            try {
                sleep(20);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
