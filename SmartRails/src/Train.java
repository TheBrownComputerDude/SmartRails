import javafx.scene.image.ImageView;

import java.util.LinkedList;

/**
 * Train class
 * Train is made and told its starting location and its destination
 *
 */
public class Train {

    private int id;
    private LinkedList<Integer> numNoPaths;
    private Rail currentRail;
    private LinkedList<Direction> path;
    private LinkedList<LinkedList<Direction>> workingPaths = new LinkedList<>();
    private LinkedList<Direction> chosenPath;
    private LinkedList<Rail> checkPoints = new LinkedList<>();
    private int numOfNoPaths = 0;
    private int numOfValidPaths = 0;
    private ImageView iv;

    /**
     * constructor
     * @param id for train
     */
    public Train(int id, ImageView iv){
        this.id = id;
        this.iv = iv;
    }

    public void setIv(ImageView iv){
        this.iv = iv;
    }

    public ImageView getIv() {
        return iv;
    }

    /**
     * sets rail it is on
     * @param rail
     */
    public void setRail(Rail rail){
        this.currentRail = rail;
    }

    /**
     *
     * @return rail it is on
     */
    public Rail getCurrentRail(){
        return currentRail;
    }

    /**
     * called when train gets a path back that does not work
     */
    public void noPath(){
        numOfNoPaths++;
        if(numOfNoPaths == numNoPaths.get(0)) {
            currentRail.clearTrain();
            //System.out.println(numOfValidPaths);
            System.out.println("No path found of " + numNoPaths.get(0) + " tried paths");
        }else{
            checkPaths();
        }
    }

    /**
     * sends message to current rail to find all paths to its destination
     * @param station to go to(destination)
     */
    public void requestFindPath(Station station){
        InboxMessage msg = new InboxMessage(Message.RequestPath);
        path = new LinkedList<>();
        numNoPaths = new LinkedList<>();
        numNoPaths.push(1);
        //sets info for message
        msg.setPath(path);
        msg.setStartStation((Station) currentRail);
        msg.setStation(station);
        msg.setTrain(this);
        msg.setNumPaths(numNoPaths);
        if(currentRail.getLeft() != null){
            msg.setDirection(Direction.LEFT);
        }else{
            msg.setDirection((Direction.RIGHT));
        }
        //sends message
        //System.out.println("request sent");
        currentRail.addMessage(msg);
    }

    /**
     * gives valid path to train
     * @param path
     */
    public void givePath(LinkedList<Direction> path){
        numOfValidPaths++;
        workingPaths.push(path);
        checkPaths();
    }

    /**
     * checks if paths received equals total paths possible if so secures a path
     */
    public void checkPaths(){
        if((numOfNoPaths + numOfValidPaths) == numNoPaths.get(0)){
            requestToSecurePath();
        }
    }

    /**
     * creats a message to send out asking to secure a random path chosen from its valid paths
     */
    public void requestToSecurePath(){
        chosenPath = workingPaths.get((int)(Math.random() * workingPaths.size()));
        LinkedList<Direction> pathToSend = new LinkedList<>();
        pathToSend.addAll(chosenPath);
        //System.out.println(chosenPath);
        InboxMessage msg = new InboxMessage(Message.SecurePath);
        msg.setTrainID(id);
        msg.setTrain(this);
        msg.setPath(pathToSend);
        msg.setDirection(chosenPath.get(0));
        currentRail.addMessage(msg);

        InboxMessage msg2 = new InboxMessage(Message.PASSTRAIN);
        msg2.setTrain(this);
        msg2.setTrainID(id);
        msg2.setPath(chosenPath);
        msg2.setDirection(chosenPath.get(0));
        currentRail.addMessage(msg2);
    }

    /**
     * adds check point to its liost of check points
     * @param checkPoint
     */
    public void giveCheckPoint(Rail checkPoint){
        checkPoints.addLast(checkPoint);
    }

    /**
     * checks if it has a check point
     * @return true if it has one false otherwise
     */
    public boolean haveCheckPoint(){
        return checkPoints.size() > 0;
    }

    /**
     * removes check point
     */
    public void removeCheckPoint(){
        checkPoints.pop();
    }


}
