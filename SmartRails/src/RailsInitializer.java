import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by bucky on 3/30/2018.
 * Class CS 351
 * Lab3: SmartRails
 */
public class RailsInitializer{

    int mapRow;
    int mapCol;
    ArrayList<ArrayList<Rail>> rails;
    Boolean validMap;
    LinkedList<Station> stations;

    /*
    Initialize a map of rails from a specified .txt file
    Does book keeping of:
        map's Rows and Columns
        validity of the map
        all rails initialized
        list of stations initialized
    Input:
        Map file being initialized
    Output:
        boolean of the validity of the map
     */
    boolean initliatizeMap(String mapFile){
        rails = new ArrayList<>();
        mapCol = 0;
        stations = new LinkedList<>();
        TextFileReader textReader = new TextFileReader();
        ArrayList<String> fileLines = textReader.FileToList(mapFile);
        String[] mapStrings = new String[fileLines.size()];
        mapStrings = fileLines.toArray(mapStrings);
        int lineNum = 0;
        LinkedList<Switch> lonelySwitches = new LinkedList<>();
        mapRow = fileLines.size();
        validMap = true;
        for(String line : mapStrings){
            Rail prev = null;
            if(line.length() > mapCol){mapCol = line.length();}
            ArrayList<Rail> railLine = new ArrayList<>();
            for(int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == 'S' || c == 's') {
                    Station station = new Station();
                    station.setIdentity(lineNum, i);
                    if (prev == null) {
                        station.setLeftRail(null);
                    } else {
                        station.setLeftRail(prev);
                        prev.setRightRail(station);
                    }
                    railLine.add(station);
                    stations.add(station);
                    prev = station;
                } else if (c == 'R' || c == 'r') {
                    BasicRail rail = new BasicRail();
                    if (prev != null) {
                        rail.setLeftRail(prev);
                        prev.setRightRail(rail);
                    }
                    railLine.add(rail);
                    prev = rail;
                } else if(c == 'L' || c == 'l'){//Is a switch
                    Light light = new Light();//lights
                    prev.setRightRail(light);
                    light.setLeftRail(prev);
                    railLine.add(light);
                    prev = light;
                } else if(c != ' '){ // is a Switch
                    Switch swtch = new Switch();
                    swtch.setIdentity(c, lineNum, i);
                    railLine.add(swtch);
                    prev = swtch;
                }
            }
            lineNum++;
            rails.add(railLine);
        }
        //Sets switches
        for(ArrayList<Rail> line: rails){
            for(Rail rail: line){
                if(rail instanceof Switch){
                    for(ArrayList<Rail> lineAgain: rails){
                        for (Rail otherSwt: lineAgain){
                            if(otherSwt instanceof Switch){
                                if(((Switch) otherSwt).switchNum == ((Switch) rail).switchNum){
                                    //Sets the switch's switch requirements
                                    if(((Switch) rail).row < ((Switch) otherSwt).row){
                                        if(((Switch) rail).col <= ((Switch) otherSwt).col ){
                                            //System.out.println(((Switch) rail).col + "," + ((Switch) rail).row + ": DR :" + ((Switch)rail).switchNum);
                                            rail.setswitchPath(otherSwt, Direction.DOWNRIGHT);
                                        }else{
                                            //System.out.println(((Switch) rail).col + "," + ((Switch) rail).row + ": DL : " + ((Switch)rail).switchNum);
                                            rail.setswitchPath(otherSwt, Direction.DOWNLEFT);
                                        }
                                    }else if(((Switch) rail).row > ((Switch) otherSwt).row ) {
                                        if(((Switch) rail).col <= ((Switch) otherSwt).col){
                                            //System.out.println(((Switch) rail).col + "," + ((Switch) rail).row + ": UR :" + ((Switch)rail).switchNum);
                                            rail.setswitchPath(otherSwt, Direction.UPRIGHT);
                                        }else{
                                            //System.out.println(((Switch) rail).col + "," + ((Switch) rail).row + ": UL :" + ((Switch)rail).switchNum);
                                            rail.setswitchPath(otherSwt, Direction.UPLEFT);
                                        }
                                    }
                                    //sets switch's basic rail requirements
                                    rail.setRightRail(rails.get(((Switch) rail).row).get(((Switch) rail).col+1));
                                    rails.get(((Switch) rail).row).get(((Switch) rail).col+1).setLeftRail(rail);
                                    rail.setLeftRail(rails.get(((Switch) rail).row).get(((Switch) rail).col-1));
                                    rails.get(((Switch) rail).row).get(((Switch) rail).col-1).setRightRail(rail);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Done! " + validMap);
        return validMap;
    }

    /*
    Gets the number of rows the in the initialized map
    Input:
        NONE
    Output:
        Number of rows in the map
     */
    int getMapRow(){return mapRow;}
    /*
    Gets the number of columns the in the initialized map
    Input:
        NONE
    Output:
        Number of columns in the map
     */
    int getMapCol(){return mapCol;}

    /*
    Gets the array list of rail lines
    Input:
        None
    Output:
        array list of rail lines
     */
    ArrayList<ArrayList<Rail>> getRails(){return rails;}

    /*
    Gets a LinkedList of all station rails
    Input:
        None
    Output:
        LinkedList of all station rails
     */
    LinkedList<Station> getStations(){return stations;}
}
