/**
 * Created by bucky on 3/26/2018.
 * Class CS 351
 * Lab3: SmartRails
 */
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;

/*
Gui to display an instance of smart rails and allow the user to interact with the game
 */
public class SmartRailsGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    ArrayList<String> mapsList;
    SmartRails smartRails;
    FlowPane comboPane;
    BorderPane paneStack;
    GridPane guiGrid;
    private Image light;
    private Image railImage;
    private Image station;
    private Image switchdl;
    private Image switchul;
    private Image switchdr;
    private Image switchur;
    private Image train;
    boolean validMap;
    private final int IMAGE_SIZE = 80;
    private final int TRAIN_SIZE = 90;
    private int currTrainNum;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Smart Rails");
        MapLoader mapLoader = new MapLoader();
        loadImages();
        smartRails = new SmartRails();
        validMap = smartRails.initializeMap("Maps/Default");
        final File folder = new File(getClass().getClassLoader().getResource("Maps").getFile());
        mapsList = mapLoader.listOfMaps(folder);
        comboPane = new FlowPane();
        comboPane.setAlignment(Pos.CENTER);
        Label toolLabel = new Label("Tools:  ");
        FlowPane toolSelector = new FlowPane();
        toolSelector.setAlignment(Pos.CENTER);
        RadioButton sendTrainButton = new RadioButton("Send Train ");
        sendTrainButton.setOnAction(event -> {
            comboPane.getChildren().clear();
            sendTrainCombos();
        });
        RadioButton loadMapButton = new RadioButton("Load New Map ");
        loadMapButton.setOnAction(event -> {
            comboPane.getChildren().clear();
            loadMapComobos(mapsList);
        });
        ToggleGroup toolGroup = new ToggleGroup();
        loadMapButton.setToggleGroup(toolGroup);
        sendTrainButton.setToggleGroup(toolGroup);
        toolSelector.getChildren().add(toolLabel);
        toolSelector.getChildren().add(loadMapButton);
        toolSelector.getChildren().add(sendTrainButton);

        guiGrid = loadGridBack();//For the textures
        guiGrid.setMinSize(630,530);
        paneStack = new BorderPane();
        paneStack.setCenter(toolSelector);
        paneStack.setTop(guiGrid);
        paneStack.setBottom(comboPane);
        paneStack.setMinSize(630,650);
        if(validMap){
            initializeBoard("Maps/Default");
        }
        Scene background = new Scene(paneStack, 645,700);
        primaryStage.setResizable(false);
        primaryStage.setScene(background);
        primaryStage.show();
        System.out.println("Done");
    }

    /*
    Sets up the train tool's GUI bar(combo boxes of stations and send button)
    Input: None
    Ouput: None
    */
    void sendTrainCombos(){
        /*
        Inputs for a train movement
         */
        ArrayList<String> cordsStringList = new ArrayList<>();
        for(Station station: smartRails.getStations()){
            cordsStringList.add(station.stationCord);
        }
        ObservableList<String> cords = FXCollections.observableArrayList(cordsStringList);
        ComboBox<String> departureBox = new ComboBox<>(cords);//Will set the starting station of the trip
        Label departureLabel = new Label(" Departure:");
        departureBox.setPrefSize(80,20);
        ComboBox<String> desinationBox = new ComboBox<>(cords);//Will set the end station of the trip
        Label desinationLabel = new Label(" Destination:");
        desinationBox.setPrefSize(80,20);
        Button sendButton = new Button("Send");//Send in the desired order of movements
        sendButton.setOnAction(event -> {
            if(departureBox.getValue() != null && desinationBox.getValue() != null){
                if(!departureBox.getValue().equals(desinationBox.getValue())){
                    Station departStation = findStation(departureBox.getValue());
                    Station destinationStation = findStation(desinationBox.getValue());
                    if(departStation != null && destinationStation != null){
                        int s1x = departStation.row;
                        int s1y = departStation.col;
                        int s2x = destinationStation.row;
                        int s2y = destinationStation.col;
                        if(smartRails.getRails().get(s1x).get(s1y).getTrain() == null && smartRails.getRails().get(s1x).get(s1y).getSecuredPathForTrain() == 0) {
                            System.out.println(s1x + ", " + s1y + " : " + s2x + ", " + s2y);
                            ImageView iv = new ImageView();
                            iv.setViewport(new Rectangle2D(0, 0, TRAIN_SIZE, TRAIN_SIZE));
                            iv.setTranslateX(s1y * IMAGE_SIZE);
                            iv.setTranslateY(s1x * IMAGE_SIZE + 30);
                            iv.setImage(train);
                            guiGrid.getChildren().add(iv);
                            currTrainNum++;
                            Train t = new Train(currTrainNum, iv);
                            smartRails.getRails().get(s1x).get(s1y).setTrain(t);
                            t.requestFindPath(destinationStation);
                        }
                    }
                }
            }
        });
        comboPane.getChildren().add(departureLabel);
        comboPane.getChildren().add(departureBox);
        comboPane.getChildren().add(desinationLabel);
        comboPane.getChildren().add(desinationBox);
        comboPane.getChildren().add(sendButton);
    }

    /*
    Loads the map loading tool bar
    Input: List of all the maps found
    Output: None
     */
    void loadMapComobos(ArrayList<String> list){
        for(String buttonName : list){
            Button button = new Button(buttonName);
            button.setId(buttonName);
            button.setOnAction(event -> {
                initializeBoard("Maps/" + button.getId());
            });
            comboPane.setPadding(new Insets(0,5,0,5));
            comboPane.getChildren().add(button);
        }
    }

    /*
    Loads in all of the images for SmartRails
    Input: None
    Output: None
     */
    private void loadImages(){
        light = new Image(getClass().getClassLoader().getResourceAsStream("Images/light.png"),IMAGE_SIZE,IMAGE_SIZE,false,false);
        railImage = new Image(getClass().getClassLoader().getResourceAsStream("Images/rail.png"),IMAGE_SIZE,IMAGE_SIZE,false,false);
        station = new Image(getClass().getClassLoader().getResourceAsStream("Images/station.png"),IMAGE_SIZE,IMAGE_SIZE,false,false);
        train = new Image(getClass().getClassLoader().getResourceAsStream("Images/train.png"),IMAGE_SIZE,IMAGE_SIZE,false,false);
        switchul = new Image(getClass().getClassLoader().getResourceAsStream("Images/switch-ul.png"),IMAGE_SIZE,IMAGE_SIZE,false,false);
        switchdl = new Image(getClass().getClassLoader().getResourceAsStream("Images/switch-dl.png"),IMAGE_SIZE,IMAGE_SIZE,false,false);
        switchur = new Image(getClass().getClassLoader().getResourceAsStream("Images/switch-ur.png"),IMAGE_SIZE,IMAGE_SIZE,false,false);
        switchdr = new Image(getClass().getClassLoader().getResourceAsStream("Images/switch-dr.png"),IMAGE_SIZE,IMAGE_SIZE,false,false);
    }

    /*
    Initialize the board with a new imageview grid and smartrail instance
    Input: String of the mapFile being loaded in
    Output: None
     */
    private void initializeBoard(String mapFile){
        validMap = smartRails.initializeMap(mapFile);
        if(validMap){
            System.out.println("Valid Map");
            guiGrid.getChildren().clear();
            guiGrid = loadGridBack();
            paneStack.setTop(guiGrid);
            smartRails.startThreads();
        }
    }

    /*
    Loads in the right textures and order of the textures to represent the map
    Input: none
    Output: Gridpane with the right textures in the right order
     */
    private GridPane loadGridBack(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setMinSize(800,580);
        ArrayList<ArrayList<Rail>> railsList = smartRails.getRails();
        int i = 0;
        for(ArrayList<Rail> line : railsList){
            int j = 0;
            for(Rail rail: line){
                StackPane pane = new StackPane();
                ImageView backIV = new ImageView();
                backIV.setImage(railImage);
                backIV.setViewport(new Rectangle2D(0,0,IMAGE_SIZE,IMAGE_SIZE));
                ImageView iv = new ImageView();
                iv.setViewport(new Rectangle2D(0,0,IMAGE_SIZE,IMAGE_SIZE));
                if(rail instanceof Station){
                    iv.setImage(station);
                    pane.getChildren().add(backIV);
                    pane.getChildren().add(iv);
                }else if(rail instanceof Light){
                    iv.setImage(light);
                    pane.getChildren().add(backIV);
                    pane.getChildren().add(iv);
                }else if(rail instanceof Switch){
                    if(rail.getDirectioinOfSwitch() == Direction.DOWNLEFT){
                        iv.setImage(switchdl);
                       // iv.setTranslateY(40);
                    }else if(rail.getDirectioinOfSwitch() == Direction.DOWNRIGHT){
                        iv.setImage(switchdr);
                       // iv.setTranslateY(40);
                    }else if(rail.getDirectioinOfSwitch() == Direction.UPLEFT){
                        iv.setImage(switchul);
                        //iv.setTranslateY(-40);
                    }else{
                        iv.setImage(switchur);
                        //iv.setTranslateY(-40);
                    }
                    pane.getChildren().add(iv);
                }else{
                    pane.getChildren().add(backIV);
                }
                grid.add(pane, j, i,1,1);
                j++;
            }
            i++;
        }
        return grid;
    }

    /*
    Scans the list of stations to find the desired station
    Input: train coordinate
    Output: the found station
        If nothing is found null is returned
     */
    Station findStation(String trainCordStr){
        for(Station station: smartRails.getStations()){
            if(trainCordStr.equals(station.stationCord)){
                return station;
            }
        }
        return null;
    }
}
