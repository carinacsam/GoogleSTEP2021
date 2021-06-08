package com.company;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.*;

public class AnagramSolution2 extends AnagramSolution1 {
    public static final File CWD = new File(System.getProperty("user.dir")); //Reference to Current Workign Directory
    public static final File small = getFile(CWD, "small.txt");
    public static final File medium = getFile(CWD, "medium.txt");
    public static final File large = getFile(CWD, "large.txt");
    public static final File smallAnswer = getFile(CWD, "small_answer.txt");
    public static final File mediumAnswer = getFile(CWD, "medium_answer.txt");
    public static final File largeAnswer = getFile(CWD, "large_answer.txt");

    /** Gets Java file from computer
     * @param first
     * @param others
     */
    private static File getFile(File first, String... others) {
        return Paths.get(first.getPath(), others).toFile();
    }

    /** Write the result of concatenating the bytes in CONTENTS to FILE,
     *  creating or overwriting it as needed.  Each object in CONTENTS may be
     *  either a String or a byte array.  Throws IllegalArgumentException
     *  in case of problems.
     *  @source: My college's Data Structures course (CS61B).
     */
    private static void writeContents(File file, Object... contents) {
        try {
            if (file.isDirectory()) {
                throw
                        new IllegalArgumentException("cannot overwrite directory");
            }
            BufferedOutputStream str =
                    new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            for (Object obj : contents) {
                if (obj instanceof byte[]) {
                    str.write((byte[]) obj);
                } else {
                    str.write(((String) obj).getBytes(StandardCharsets.UTF_8));
                }
            }
            str.close();
        } catch (IOException | ClassCastException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /** Return the entire contents of FILE as a byte array.  FILE must
     *  be a normal file.  Throws IllegalArgumentException
     *  in case of problems.
     *  @source: My college's Data Structures course (CS61B).
     */
    private static byte[] readContents(File file) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("must be a normal file");
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /** Return the entire contents of FILE as a String.  FILE must
     *  be a normal file.  Throws IllegalArgumentException
     *  in case of problems.
     *  @source: My college's Data Structures course (CS61B).
     */
    private static String readContentsAsString(File file) {
        return new String(readContents(file), StandardCharsets.UTF_8);
    }

    /** Arranges the letters of the given string into sorted order.
     * For example, sortWord("dancer") returns "acdenr".
     */
    private static String sortWord(String word) {
        char[] wordArray = word.toCharArray();
        Arrays.sort(wordArray);
        return String.valueOf(wordArray);
    }

    /** Creates the HashMap "dictionaryWords" that stores a reference to each word in the dictionary as its keys
     * and another HashMap "wordCharCount" as its value, that contains each word's characters and number of characters.
     */
    private static HashMap<String, HashMap<Character, Integer>> dictCountChars(File dictionaryFile) {
        HashMap<String, HashMap<Character, Integer>> dictionaryWords = new HashMap<>();
        try {
            Scanner scanner = new Scanner(dictionaryFile);
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine();
                HashMap<Character, Integer> wordCharCount = countCharacters(word);
                dictionaryWords.put(word, wordCharCount);
            }
            scanner.close();
        } catch (IOException ex) {
            System.out.println("Error reading file words.txt.");
        }
        return dictionaryWords;
    }

    /** Counts the number of each character in a word and stores the information in a HashMap.
     */
    private static HashMap<Character, Integer> countCharacters(String word) {
        HashMap<Character, Integer> wordCharCount = new HashMap<>();
        for (char character = 'a'; character <= 'z'; character ++) {
            int count = 0;
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == character) {
                    count += 1;
                }
            }
            wordCharCount.put(character, count);
        }
        return wordCharCount;
    }

    /** Calculates the total score of each word
     * SCORES of the characters:
     * ----------------------------------------
     * | 1 point  | a, e, h, i, n, o, r, s, t |
     * | 2 points | c, d, l, m, u             |
     * | 3 points | b, f, g, p, v, w, y       |
     * | 4 points | j, k, q, x, z             |
     * ----------------------------------------
     * SCORES = [1, 3, 2, 2, 1, 3, 3, 1, 1, 4, 4, 2, 2, 1, 1, 3, 4, 1, 1, 1, 2, 3, 3, 4, 3, 4]
     * @source: The above table was taken from score_checker.py
     */
    private static int getScore(String word) {
        int score = 0;
        char[] wordArray = word.toCharArray();
        Character[] onePoint = {'a', 'e', 'h', 'i', 'n', 'o', 'r', 's', 't'};
        Character[] twoPoint = {'c', 'd', 'l', 'm', 'u'};
        Character[] threePoint = {'b', 'f', 'g', 'p', 'v', 'w', 'y'};
        Character[] fourPoint = {'j', 'k', 'q', 'x', 'z'};
        for (char character: wordArray) {
            if (Arrays.asList(onePoint).contains(character)) {
                score += 1;
            } else if (Arrays.asList(twoPoint).contains(character)) {
                score += 2;
            } else if (Arrays.asList(threePoint).contains(character)) {
                score += 3;
            } else if (Arrays.asList(fourPoint).contains(character)) {
                score += 4;
            }
        }
        return score;
    }


    /** Compares words in the dictionary to the given word and returns the anagram with the highest score.
     */
    public static String getSubAnagram(String word) {
        HashMap<String, HashMap<Character, Integer>> dictionaryWords = dictCountChars(getFile(CWD, "words.txt"));
        HashMap<Character, Integer> wordChars = countCharacters(word);
        String highestScoreAnagram = null;
        int highestScore = 0;
        boolean sameCount = false;
        for (String dictWord: dictionaryWords.keySet()) {
            if (dictWord.length() <= word.length()) {
                for (char character : dictionaryWords.get(dictWord).keySet()) {
                    int dictCharCount = dictionaryWords.get(dictWord).get(character); {
                        if (wordChars.get(character) >= dictCharCount) {
                            sameCount = true;
                        } else {
                            sameCount = false;
                            break;
                        }
                    }
                }
                if (sameCount) {
                    int currentWordScore = getScore(dictWord);
                    if (currentWordScore > highestScore) {
                        highestScore = currentWordScore;
                        highestScoreAnagram = dictWord;
                    }
                }
            }
        }
        return highestScoreAnagram;
    }

    /** Reads each data file and writes an answer file containing the highest scoring anagram for each file.
     */
    public static void getAllAnagrams(String questionFileName, String answerFileName) {
        try {
            getFile(CWD, answerFileName).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Scanner scanner = new Scanner(getFile(CWD, questionFileName));
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine();
                String anagram = getSubAnagram(word);
                File answerFile = getFile(CWD, answerFileName);
                writeContents(answerFile,
                        readContentsAsString(answerFile) + anagram + "\n");
            }
            scanner.close();
        } catch (IOException ex) {
            System.out.println("Error reading file word.txt.");
        }
    }

    /** Main method for testing
     */
    public static void main(String[] args) {
        getAllAnagrams("large.txt", "large_answer.txt");
    }
}