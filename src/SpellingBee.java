// Gabriel Gil, March 18th, 2025

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Gabriel
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // Call the makeWords method, with the initial call of potentialWord as an empty screen and letters as all the
        // letters provided by the user
        makeWords("", letters);
    }

    // makeWords method to recursively generate all the possible combinations of words using the letters inputted by
    // the user and adding those words to the words ArrayList
    public void makeWords(String potentialWord, String letters){
        // As long as the potentialWord is not just an empty string, add it to the words ArrayList
        if(!potentialWord.equals("")){
            words.add(potentialWord);
        }
        // Base Case: if the string of letters to be added to the potentialWord is empty, then return
        if(letters.equals("")){
            return;
        }
        // For each letter still in the letters string, call makeWords on the potentialWord + that letter, passing in
        // all the remaining letters but that letter as the letters for the new call
        for(int i = 0; i < letters.length(); i++){
            makeWords(potentialWord + letters.charAt(i), letters.substring(0,i) +
                    letters.substring(i + 1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // Call the mergeSort method, passing in the left and right indices of the words ArrayList
        words = mergeSort(0, words.size() - 1);
    }

    // mergeSort method to recursively sort all the words in the words ArrayList into alphabetical order. Return an
    // ArrayList sorted in alphabetical order that we can set words equal to
    public ArrayList<String> mergeSort(int left, int right){
        // Base case: if the left and right indices are the same, meaning we are looking at one word, return an
        // ArrayList of only that word
        if (left == right){
            ArrayList<String> newArr =  new ArrayList<String>();
            newArr.add(words.get(left));
            return newArr;
        }
        // Find the middle index and make two new sorted ArrayLists by calling mergeSort on the left side of the
        // midpoint including the midpoint and then calling mergeSort on the right side of the midpoint
        int mid = (left + right) / 2;
        ArrayList<String> arr1 = mergeSort(left, mid);
        ArrayList<String> arr2 = mergeSort(mid + 1, right);

        // Call merge to merge those two sorted ArrayLists
        return merge(arr1, arr2);

    }

    // Merge method to combine two sorted ArrayLists into one sorted ArrayList and return that combined, sorted
    // ArrayList
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2){
        // Initialize the necessary variables for this process: an ArrayList that will be returned and variables to
        // keep track of the indices of each of the input ArrayLists
        ArrayList<String> toReturn = new ArrayList<String>();
        int i = 0;
        int j = 0;

        // While both i and j are within the bounds of their respective arrays, add the "lower" (alphabetically)
        // string of the compared strings to the final ArrayList and increment the appropriate index tracker
        while(i < arr1.size() && j < arr2.size()){
            if(arr1.get(i).compareTo(arr2.get(j)) < 0){
                toReturn.add(arr1.get(i));
                i++;
            }
            else{
                toReturn.add(arr2.get(j));
                j++;
            }
        }
        // If there are still words present in the first ArrayList, add them to the final ArrayList
        while(i < arr1.size()){
            toReturn.add(arr1.get(i));
            i++;
        }
        // If there are still words present in the second ArrayList, add them to the final ArrayList
        while(j < arr2.size()){
            toReturn.add(arr2.get(j));
            j++;
        }
        // Return the final, both combined and sorted ArrayList
        return toReturn;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // For every word in words, if it isn't in the dictionary, remove it from the words ArrayList
        for(int i = 0; i < words.size(); i++){
            // Call binarySearch to determine if the word is present in the dictionary or not, and pass in the indices
            // of the dictionary
            if(!(binarySearch(words.get(i), 0, DICTIONARY_SIZE - 1))){
                words.remove(i);
                i--;
            }
        }
    }

    // binarySearch method to recursively determine whether the selected word, called word in the method header, is
    // present in the dictionary
    public boolean binarySearch(String word, int start, int end){
        // Base case: if the start index is greater than the end index, return that the word is not found
        if (start > end){
            return false;
        }
        // Find the middle index
        int mid = (start + end) / 2;

        // If the word at the middle index is equal to the target, return that word was found
        if(word.equals(DICTIONARY[mid])){
            return true;
        }
        // Given that the word was not found above, call binary search on either the left side or right side of the
        // dictionary. That is, if the target word is "lower" alphabetically than the word at the middle index of the
        // dictionary, call binarySearch on the left side of the middle index. If not, call binarySearch on the right
        // side of the middle index
        if(word.compareTo(DICTIONARY[mid]) < 0) {
            return binarySearch(word, start, mid -1);
        }
        return binarySearch(word,mid + 1, end);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
