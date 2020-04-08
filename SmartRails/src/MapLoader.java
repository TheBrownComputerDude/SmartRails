import java.io.File;
import java.util.ArrayList;

/**
 * Created by bucky on 4/4/2018.
 * Class CS 351
 * Lab3: SmartRails
 */
public class MapLoader {
    /*
     Reads the files in a specified folders
     Input: file object of the folder
     Output: ArrayList of file names in the folder/path
      */
    ArrayList<String> listOfMaps(final File folder){
        ArrayList<String> list = new ArrayList<>();
        System.out.println(folder.toString());
        list.add("Default");
        list.add("TestMap1");
        list.add("TestMap2");
        list.add("TestMap3");
        list.add("TestMap4");
        /*
        for (final File fileEntry : folder.listFiles()) {
            System.out.println(fileEntry.toString());
            if (fileEntry.isDirectory()) {
                listOfMaps(fileEntry);
            } else {
                list.add(fileEntry.getName());
            }
        }*/
        return list;
    }

}
