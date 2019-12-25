import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AlphabetKeyBoard {
    private GridPane gridAlpha = new GridPane();
    private Map<Character, Integer> garbage =new HashMap<Character, Integer>();
    private ArrayList<Character> allValues=new ArrayList<Character>();
    private ArrayList<String> allLettersGuessed=new ArrayList<String>();
    public Map<Character,Integer> getGarbage(){
        return garbage;
    }
    private int countTotal=0;   //Determine if the word has been solved
    public int getCountTotal() {
        return countTotal;
    }

    public void reset() {
        countTotal = 0;
        for ( Node child : gridAlpha.getChildren()) {
            Button button = (Button)child;
            button.setStyle("-fx-background-color: green" );
        }
        garbage.clear();
        allLettersGuessed.clear();
    }
    public ArrayList<Character> storeAlphabet(){
        char end = 'Z';
        for (char beg ='A'; beg<=end; beg ++){
            allValues.add(beg);
        }
        return allValues;
    }
    public ArrayList<String> getAllLettersGuessed(){
        return allLettersGuessed;
    }
    public GridPane printAlphabet(){
        storeAlphabet();
        gridAlpha.setAlignment(Pos.CENTER);
        gridAlpha.setHgap(2);
        gridAlpha.setVgap(2);
        int count =0;
        for (int rows = 0; rows < 4; rows++) {
            for (int columns= 0; columns< 7; columns++) {
                if (count !=26){
                    //Button buttonAlpha = new Button(String.valueOf(allValues.get(count)));
                    Button buttonAlpha=new Button(String.valueOf(allValues.get(count)));
                    buttonAlpha.setAlignment(Pos.CENTER);

                    // Set the size of the buttons
                    buttonAlpha.setStyle("-fx-background-color: green" );
                    buttonAlpha.setMinSize(50,50);
                    buttonAlpha.setFont(Font.font("Times New Roman",FontWeight.BOLD,20));
                    gridAlpha.add(buttonAlpha, columns,rows);
                    count++;
                }
                else continue;
            }
        }
        return gridAlpha;
    }
    public void charRecognize(String charEntered, readWordFile wordfile, displayFormat display, int num_of_guesses_left, SimpleStringProperty numGuesses){
        int ASCIIA=65;
        int ASCIIZ=91;
        int total_guess=10;
        int enteredValue =Integer.valueOf(charEntered.toUpperCase().charAt(0));//Check the event entered tosee if it matches the keyboard value
        if (enteredValue>=ASCIIA && enteredValue<=ASCIIZ && !garbage.containsKey(allValues.get(enteredValue-ASCIIA))&&num_of_guesses_left>0){
            int countOccurrences=0;  //Determine if a letter exist in word
            allLettersGuessed.add(charEntered.toUpperCase());//Add all letters entered to through the system
            for(int i=0; i<wordfile.getFinalWord().length();i++) {
                if (wordfile.getFinalWord().substring(i, 1 + i).toUpperCase().equals(charEntered.toUpperCase())) {
                    countOccurrences += 1;
                }
            }
            if(countOccurrences>0) {
                //Store in the position where it exist
                countTotal+=countOccurrences;
                wordfile.changeCharifFound(charEntered);
            }
            else {
                numGuesses.set(String.valueOf(num_of_guesses_left-1));
                // Add one body part to hangman
                display.showPicVisible(total_guess-num_of_guesses_left+1);

            }
            int currentPos=enteredValue-ASCIIA;
            gridAlpha.getChildren().get(currentPos).setStyle("-fx-background-color: #ADFF2F;");  //Change the color to lighter color
            display.addTotalGuess();

            garbage.put(allValues.get(currentPos), currentPos );
            display.getSaveButton().setDisable(false);
            display.getSaveButton().setDisable(false);
        }

    }

}
