package com.company;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;


public class AnagramSolution1 {

    public static final File CWD = new File(System.getProperty("user.dir"));

    /** Gets Java file from computer
     * @param first
     * @param others
     */
    private static File getFile(File first, String... others) {
        return Paths.get(first.getPath(), others).toFile();
    }


    /** Arranges the letters of the given string into sorted order.
     * For example, sortWord("dancer") returns "acdenr".
     */
    private static String sortWord(String word) {
        char[] wordArray = word.toCharArray();
        Arrays.sort(wordArray);
        return String.valueOf(wordArray);
    }

    /** Reads each word.txt (dictionary file) with a scanner and saves them as "word"
     * Sorts each word in the file using sortWord() and saves them as "sortedWord"
     * Creates an array called "list" where we put words with the same anagram
     * Puts both the sorted word and array list as keys and values of a HashMap, respectively, called "sortedDictionary'
     */
    private static HashMap<String, ArrayList<String>> sortDictionary(File fileName) {
        HashMap<String, ArrayList<String>> sortedDictionary = new HashMap<>();
        try {
            Scanner scanner = new Scanner(fileName);
            while (scanner.hasNextLine()) {
                ArrayList<String> list = new ArrayList();
                String word = scanner.nextLine();
                String sortedWord = sortWord(word);
                if (sortedDictionary.containsKey(sortedWord)) {
                    ArrayList<String> existingList = sortedDictionary.get(sortedWord);
                    existingList.add(word);
                    sortedDictionary.put(sortedWord, existingList);
                } else {
                    list.add(word);
                    sortedDictionary.put(sortedWord, list);
                }
            }
            scanner.close();
        } catch (IOException ex) {
            System.out.println("Error reading file word.txt.");
        }
        return sortedDictionary;
    }

    /** Takes in a given word, sorts it and then returns the anagram given by a sorted dictionary
     */
    public static ArrayList<String> getAnagram(String word) {
        HashMap<String, ArrayList<String>> sortedDictionary = sortDictionary(getFile(CWD, "words.txt"));
        String sortedWord = sortWord(word);
        ArrayList<String> anagrams = new ArrayList();
        for (String sortedDictWord: sortedDictionary.keySet()) {
            if (sortedDictWord.equals(sortedWord)) {
                anagrams = sortedDictionary.get(sortedWord);
            }
        }
        return anagrams;
    }

    /** Main method for testing
     */
    public static void main(String[] args) {
        System.out.println(getAnagram(""));
        System.out.println(getAnagram("abcde"));
        System.out.println(getAnagram("acdr"));
        System.out.println(getAnagram("maes"));
        System.out.println(getAnagram("slneit"));
    }
}
