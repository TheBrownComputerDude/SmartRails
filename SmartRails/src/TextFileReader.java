/**
 * Created by bucky on 3/27/2018.
 * Class CS 351
 * Lab3: SmartRails
 */
import java.io.*;
import java.util.ArrayList;

public class TextFileReader {
    /*
   Reads in a text file given a specific address.
   Returns an ArrayList with each entry being a line of the text file
   Input:
       fileName: name of the requested file
   Output:
       ArrayList with each entry being a line of the text file
    */
    ArrayList<String> FileToList(String fileName){
        ArrayList<String> fileList = new ArrayList<>();
        String line;
        try{
            InputStream file = getClass().getClassLoader().getResourceAsStream(fileName);
            InputStreamReader fileReader = new InputStreamReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null){
                fileList.add(line);
            }
            bufferedReader.close();
        }
        catch (FileNotFoundException ex){
            System.out.println("No file found with name " + fileName);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return fileList;
    }

}
