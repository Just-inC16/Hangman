
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class displayFormat {
    private int totalGuessesSofar=0;
    public int getTotalGuessesSofar() {
        return totalGuessesSofar;
    }
    //Subtract the number of guesses
    public void addTotalGuess(){
        totalGuessesSofar+=1;
    }

    private int num_of_guesses_left = 10;

    //Subtract the number of guesses
    public int getNumGuess() {
        return num_of_guesses_left;
    }

    private SimpleStringProperty numGuesses = new SimpleStringProperty();

    private boolean flag =false;
    private boolean flag2 =false;

    //Store the Character and the position of the integer
    private Map<Character, Integer>garbage= new HashMap<Character, Integer>();
    private Map<Integer,Character>storeLetters= new HashMap<Integer,Character>();
    public void setSaveBntTrue(){
        saveButton.setDisable(true);
    }
    public void setSaveBntFalse(){
        saveButton.setDisable(false);
    }
    private BorderPane border = null;
    private GridPane newStart= null;
    private AlphabetKeyBoard keybd= new AlphabetKeyBoard();
    private List<ImageView> imageList = new ArrayList<ImageView>();
    private int curDisplayIndex = 0;
    private readWordFile file = null;

    Button newButton;

    Button saveButton;
    public Button getSaveButton(){
        return saveButton;
    }
    Button startPlaying;


    public displayFormat() throws FileNotFoundException, URISyntaxException {
        file = new readWordFile();
        //Store it in a list and extract one randomly from the txt file
        file.readTxt();
        file.getRandom();
    }
    public void reset() throws FileNotFoundException, URISyntaxException {
        Node childNode = null;
        for ( Node node : newStart.getChildren()){
            if ( newStart.getColumnIndex(node) == 3 && newStart.getRowIndex(node) ==3 ) {
                childNode = node;
            }
        }
        if (childNode != null ){
            newStart.getChildren().remove(childNode);
            newStart.add(file.fetchWord(), 3,3);
        }
        num_of_guesses_left = 10;
        setNumGuesses(numGuesses, num_of_guesses_left);
    }
    public HBox topButtons(Stage mainstage) {
        HBox topLabel=new HBox ();
        topLabel.setPrefWidth(80);
        topLabel.setPadding(new Insets(10, 1000, 10, 12));
        topLabel.setStyle("-fx-background-color: black;");


        //Add Images to icon
        Image saveImg= new Image(getClass().getResourceAsStream("Save.png"));
        saveButton = new Button("Save");
        saveButton.setGraphic(new ImageView(saveImg));
        saveButton.setMinWidth(topLabel.getPrefWidth());
        //Save button disable
        saveButton.setDisable(true);
        saveButton.setOnAction(e-> {
            try {
                saveFile(mainstage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        Image loadImg= new Image(getClass().getResourceAsStream("Load.png"));
        Button loadButton = new Button("Load");
        loadButton.setGraphic(new ImageView(loadImg));
        loadButton.setMinWidth(topLabel.getPrefWidth());
        loadButton.setOnAction(e-> {
            try {
                loadFile(mainstage);
            } catch (ClassNotFoundException | IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        });

        Image exitImg= new Image(getClass().getResourceAsStream("Exit.png"));
        Button exitButton = new Button("Exit");
        exitButton.setGraphic(new ImageView(exitImg));
        exitButton.setMinWidth(topLabel.getPrefWidth());
         exitButton.setOnAction(e->{
             //Check that there is guesses left
             if(totalGuessesSofar==0||num_of_guesses_left<=0||saveButton.isDisabled()){
                 mainstage.close();
             }
             else
                exitDisplay(mainstage);

         } );


        Image newImg= new Image(getClass().getResourceAsStream("New.png"));
        newButton = new Button("New");
        newButton.setGraphic(new ImageView(newImg));
        newButton.setMinWidth(topLabel.getPrefWidth());

        newButton.setOnAction(e->{
            //If 'new' is displayed
            if(num_of_guesses_left<=0){
                try {
                    file.getRandom();
                    startNewGame();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
            else if(getTotalGuessesSofar()>0){

                try {
                    saveFile(mainstage);
                    file.getRandom();
                    startNewGame();
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }

            //No display
            else{
                flagTrue();
                bottomLabelStartPlay();
            }
        });

        topLabel.getChildren().addAll(newButton,loadButton,saveButton, exitButton);
        return topLabel;
    }

    private void flagTrue() {
        flag= true;
    }

    private void flagMiddle() {
        flag2= true;
    }
    public void startNewGame() throws FileNotFoundException, URISyntaxException {
        reset();
        keybd.reset();
        totalGuessesSofar=0;
        showPicVisible(0);
    }
    public StringBuilder saveData(String guesses){
        //  Save the word playing
        //  Save the correct letters guessed
        //  save the incorrect letters guessed
        StringBuilder allInformation=new StringBuilder();
        //Append the number of guesses
        allInformation.append(guesses +"\n");
        allInformation.append(keybd.getGarbage()+"\n");//Garbage letters
        allInformation.append(file.getAllLetters());
        System.out.print(allInformation);
        return allInformation;
    }
    //Load the hangman file using FileChooser
    public void loadFile(Stage mainStage) throws ClassNotFoundException, IOException, URISyntaxException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("HangMan (*.hng)", "*.hng");
        fileChooser.getExtensionFilters().add(filter);
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null && selectedFile.getAbsolutePath().endsWith(".hng")) {
            BufferedReader br= new BufferedReader(new FileReader(selectedFile) );
            String st;
            int linecount = 0;
            while ( (st = br.readLine()) != null ){
                if (linecount == 0) {
                    file.setFinalWord(st);
                    startNewGame();
                    linecount++;
                }
                else {
                    for ( int i = 0; i < st.length(); i++ ) {
                        char c = st.charAt(i);
                        handleCharEvent(String.valueOf(c));
                    }
                }

            }
            newStart.setVisible(true);
        }
    }
    //Save the file using fileChooser
    public void saveFile(Stage mainStage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("HangMan (*.hng)", "*.hng");
        fileChooser.getExtensionFilters().add(filter);
        File namedFile = fileChooser.showSaveDialog(mainStage);

        if (namedFile != null && namedFile.getAbsolutePath().endsWith(".hng")){
            FileWriter writingToFile =new FileWriter(namedFile);
            //Storing the word
            writingToFile.write(file.getFinalWord()+ "\n");
            //Store all the letters
            ArrayList<String> allLetters=keybd.getAllLettersGuessed() ;
            for( int accessKeyBoard =0; accessKeyBoard< allLetters.size(); accessKeyBoard++){
                writingToFile.write(allLetters.get(accessKeyBoard)); //Append all letters together
            }
            //Close the file
            writingToFile.close();
            saveButton.setDisable(true);
        }
    }
    //Display the exit button
    public void exitDisplay(Stage mainstage){
        //Creation of a stage
        Stage exitWindowPop = new Stage();
        // Buttons and Controls
        Label exitLabel = new Label("Would you like to save the game?");
        exitLabel.setAlignment(Pos.CENTER);
        Button yes = new Button("YES");
        Button no = new Button("NO");
        Button cancel = new Button("Cancel");
        HBox controls =new HBox (yes,no, cancel);
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(5);

        VBox displayStack = new VBox();
        displayStack.setAlignment(Pos.CENTER);
        displayStack.getChildren().addAll(exitLabel,controls);

        //'Yes' button command(**USe filechooser method)
        yes.setOnAction(e->{
            try {
                saveFile(mainstage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            exitWindowPop.close();
        });
        //'No' button command
        no.setOnAction(e->System.exit(0));
        //'Cancel' button command
        cancel.setOnAction(e->closeTheProgram(exitWindowPop));

        Scene sceneDis= new Scene(displayStack, 300, 200);
        exitWindowPop.setTitle("Save?");
        exitWindowPop.setScene(sceneDis);
        exitWindowPop.show();
    }

    public GridPane middleDisplay() throws FileNotFoundException, URISyntaxException {
        if(newStart==null){
            newStart = new GridPane();
            int col_index = 3;
            newStart.add(displayText(), 2, 0);
            newStart.add(setNumGuesses(numGuesses, num_of_guesses_left), col_index ,1 );
            newStart.add(file.fetchWord(), col_index ,3);
            GridPane alphabetKeyBd = keybd.printAlphabet();
            newStart.add(alphabetKeyBd, col_index,4);
            newStart.add(initHangMan(),  1, 4);
            newStart.setHgap(10);
            newStart.setVgap(3);
            for (Node child : alphabetKeyBd.getChildren() ){
                Button button = (Button)child;
                button.setOnAction(e->{
                    handleCharEvent(button.getText());
                });
            }
        }
        newStart.setVisible(flag2);
        return newStart;
    }

    //Display whether you lost or won
    public void gameOverWindow(){
        Stage closeStage=new Stage();

        Label overMessage;
        if(file.countLetters(keybd.getCountTotal()) ) {
             overMessage = new Label("You Won.");// Print out that you won
        }
        else {
             overMessage = new Label("You lost(the word was: "+file.getFinalWord()+")");  // Print out that you lost
        }
        overMessage.setAlignment(Pos.CENTER);

        Button close =new Button("CLOSE");
        close.setAlignment(Pos.CENTER);
        close.setOnAction(e->closeTheProgram(closeStage));

        VBox message =new VBox();
        message.getChildren().addAll(overMessage, close);
        message.setAlignment(Pos.CENTER);
        message.setSpacing(10);
        Scene finishedGame= new Scene(message, 300, 200);

        closeStage.setTitle("GAME OVER");
        closeStage.setScene(finishedGame);
        closeStage.show();
        saveButton.setDisable(true);
        newButton.setDisable(false);

    }
    public void closeTheProgram(Stage newStage){
            newStage.close();
    }

    public HBox setNumGuesses(SimpleStringProperty setGuess, int num_of_guesses_left) {
        HBox guessesBox = new HBox();
        Text txt = new Text("Number of Guesses Left: ");
        txt.setFont(Font.font("", FontWeight.BLACK, 12));

        // Creating a label for Number of guesses to bind
        Label keepTrackOfGuesses=new Label(String.valueOf(num_of_guesses_left));
        keepTrackOfGuesses.textProperty().bind(setGuess);
        setGuess.set(String.valueOf(num_of_guesses_left));
        txt.setFont(Font.font("", FontWeight.BLACK, 12));

        guessesBox.getChildren().addAll(txt,keepTrackOfGuesses);
        return guessesBox;
    }
    public Text displayText(){
        Text txt =new Text("Hangman");
        txt.setFont(Font.font("", FontWeight.BOLD,30));
        txt.setFill(Color.RED);
        return txt;
    }
    public BorderPane bottomLabelStartPlay(){

            if ( border == null ) {
                border = new BorderPane();
                startPlaying = new Button("Start Playing");
                //startPlaying.setAlignment(Pos.CENTER);
                HBox bottom = new HBox();
                bottom.setAlignment(Pos.CENTER);
                bottom.setStyle("-fx-background-color: #F0E68C;");
                bottom.getChildren().add(startPlaying);
                border.setBottom(bottom);

                startPlaying.setOnAction(e -> {
                    flagMiddle();
                    try {
                        middleDisplay();

                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                    startPlaying.setDisable(true);
                });

        }
        border.setVisible(flag);

        return  border;
    }

    public StackPane initHangMan(){
        StackPane wholeImage =new StackPane();
        //All of the body of a hangman
        for ( int i = 1; i < 12; i++)
        {
            String filename = "Hangman" + i +".png";
            Image newImg= new Image(getClass().getResourceAsStream(filename));
            ImageView imgView = new ImageView(newImg);
            imgView.setVisible(false);
            wholeImage.getChildren().add(imgView);
            imageList.add(imgView);
        }

        return wholeImage;
    }
    public void showPicVisible(int index) {
        imageList.get(curDisplayIndex).setVisible(false);
        imageList.get(index).setVisible(true);
        curDisplayIndex = index;
    }

    public boolean handleCharEvent(String charTyped){
        keybd.charRecognize(charTyped, file, this, num_of_guesses_left, numGuesses);
        num_of_guesses_left = Integer.parseInt(numGuesses.getValue());
        if (num_of_guesses_left == 0 || file.countLetters(keybd.getCountTotal())) {
            file.displaytheRemaining();
            gameOverWindow();
            return true;
        }
        return false;
    }
}
