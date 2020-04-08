import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * SmartRails class
 * holds all the rails for the rail system
 * makes trains and gives them destinations
 */

public class SmartRails {


    ArrayList<ArrayList<Rail>> rails;
    LinkedList<Station> stations;
    int col;
    int row;
    boolean validMap;
    /**
     * constructor
     */
    public SmartRails(){
        //trainTest();
    }

    boolean initializeMap(String fileName){
        RailsInitializer railsInitializer = new RailsInitializer();
        validMap = railsInitializer.initliatizeMap(fileName);
        col = railsInitializer.getMapCol();
        row = railsInitializer.getMapRow();
        rails = railsInitializer.getRails();
        stations = railsInitializer.getStations();
        return validMap;
    }

    ArrayList<ArrayList<Rail>> getRails(){return rails;}

    LinkedList<Station> getStations(){return stations;}

    void startThreads(){
        for(ArrayList<Rail> line : rails){
            for(Rail rail: line){
                rail.setDaemon(true);
                rail.start();
            }
        }
    }
}
