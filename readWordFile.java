import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
public class readWordFile {
    private List<String> allWords =new ArrayList<String>();
    private List<Boolean> allLetters=new ArrayList<Boolean>();
    private String finalWord;
    private HBox randomWord;
    public List<Boolean> getAllLetters(){
        return allLetters;
    }
    //Get the word
    public String getFinalWord(){
        return  finalWord;
    }
    //Set the final word
    public void setFinalWord(String newWord){
        finalWord=newWord;
    }
    //Read the txt file and store into List
    public void readTxt() throws URISyntaxException, FileNotFoundException {
        URL filepath = getClass().getResource("words.txt");
        File words = new File(filepath.toURI());
        Scanner nextWord =new Scanner(words);
        while (nextWord.hasNextLine()) {
            allWords.add(nextWord.nextLine());
        }
    }
    // Get random word
    public void getRandom(){
        //Pick Random Word
        Random rand = new Random();
        int randomInteger = rand.nextInt(allWords.size());
        finalWord=allWords.get(randomInteger); // Stores the word into the finalWord
        System.out.println(finalWord);
    }
    // Create the word labels
    public HBox fetchWord() throws FileNotFoundException, URISyntaxException {
        randomWord= new HBox();
        randomWord.setSpacing(5);
        //storeLetter=new StackPane();
        for(int i=0; i<finalWord.length();i++){
                //Set the letters to false
                allLetters.add(i,false);
                Label blankPosition=new Label();
                blankPosition.setMinWidth(20);
                blankPosition.setMinHeight(30);
                blankPosition.setText(" ");
                blankPosition.setStyle("-fx-background-color: black");

                //storeLetter.getChildren().addAll(blankPosition,charPosition);
                randomWord.getChildren().addAll(blankPosition);
        }
        return randomWord;
    }
    public void changeCharifFound(String charEntered){
        for(int i=0; i<finalWord.length(); i++){
            if(charEntered.toUpperCase().equals(finalWord.substring(i,i+1).toUpperCase())){
                //Set the letters entered to be true
                allLetters.set(i,true);
                //Change the labels of the character
                Label newCharData=new Label(finalWord.substring(i,i+1));
                newCharData.setMinWidth(20);
                newCharData.setMinHeight(30);
                newCharData.setTextFill(Color.ORANGE);
                BackgroundFill backgroundF= new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,Insets.EMPTY);
                Background background=new Background(backgroundF);
                newCharData.setBackground(background);
                newCharData.setFont(Font.font("Time News Roman",FontWeight.BOLD, 20));
                randomWord.getChildren().set(i,newCharData);
            }
        }
    }
    public void displaytheRemaining(){
        for(int i=0; i<finalWord.length(); i++) {
            if (allLetters.get(i)==false) {
                Label newRemains = new Label(finalWord.substring(i, i + 1));
                newRemains.setStyle("-fx-background-color: Gray");
                newRemains.setMinWidth(20);
                newRemains.setMinHeight(30);
                BackgroundFill backgroundF = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
                Background background = new Background(backgroundF);
                newRemains.setBackground(background);
                newRemains.setFont(Font.font("Time News Roman",FontWeight.BOLD, 20));
                randomWord.getChildren().set(i, newRemains);
            }
        }
    }
    public boolean countLetters(int totalLetters){
        if(totalLetters==finalWord.length())
            return true;
        return false;
    }
}
