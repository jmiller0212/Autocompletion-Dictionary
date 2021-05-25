// Jarod Miller (jcm138)

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class ac_test {

    public static DLB createDictionary(DLB d) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("dictionary.txt"));
            String line = br.readLine();
            while(line != null) {
                d.addWord(line);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }
    // my user_history.txt reading and writing methods
    public static HashMap<String, Integer> createUserHistoryFile(HashMap<String, Integer> hm) {
        try {
            File file = new File("user_history.txt");
            if(file.createNewFile()) {
            } else {
                BufferedReader br = new BufferedReader(new FileReader("user_history.txt"));
                String line = br.readLine();
                String delim = "[:]";
                String[] tokens;
                while(line != null) {
                    tokens = line.split(delim);
                    hm.put(tokens[0], Integer.parseInt(String.valueOf(tokens[1])));
                    line = br.readLine();
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hm;
    }

    public static void printHashMap(HashMap<String, Integer> hm) {
        if(hm.isEmpty()) {
            System.out.println("empty hashmap");
        } else {
            System.out.println(hm);
        }
    }

    public static void writeToUserHistoryFile(HashMap<String, Integer> hm) {
        try {
            FileWriter fileWriter = new FileWriter("user_history.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            Iterator<String> words = hm.keySet().iterator();
            Iterator<Integer> counts = hm.values().iterator();
            while(words.hasNext()) {
                printWriter.println(words.next()+":"+counts.next());
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // timing data methods
    public static void printElapsedTime(long start, ArrayList<Double> searchTimeAL) {
        long elapsedTime = System.nanoTime() - start;
        double toSeconds = (double) elapsedTime / 1000000000;
        searchTimeAL.add(toSeconds);
        System.out.println("\n(" + toSeconds + " s)");
    }
    public static void avgSearchTime(ArrayList<Double> searchTimeAL) {
        double totalTimeInSecs = 0;
        double avgSearchTime = 0;
        for(int i = 0; i < searchTimeAL.size(); i++) {
            totalTimeInSecs += searchTimeAL.get(i);
        }
        avgSearchTime = totalTimeInSecs / searchTimeAL.size();
        System.out.printf("Average Time: %.6f s\n", avgSearchTime);
    }
    // finds the words in the user_history that have the prefix of the user input
    // returns a HashMap of the strings with the prefix and the associated 
    // frequency
    public static ArrayList<String> findUserPredictions(HashMap<String, Integer> userHash, String prefix) {
        HashMap<Integer, ArrayList<String>> inverse = new HashMap<Integer, ArrayList<String>>();
        ArrayList<Integer> userWordCounts = new ArrayList<>();
        ArrayList<String> ret = new ArrayList<>();
        Iterator<String> words = userHash.keySet().iterator();
        String key;
        while (words.hasNext()) {
            // now we have to check each iteration to see if each string
            // in the HashMap has the current prefix
            key = words.next();
            if (key.startsWith(prefix)) {
                // if the inverse hashmap already contains that key
                if (inverse.containsKey(userHash.get(key))) {
                    ArrayList<String> temp = inverse.get(userHash.get(key));
                    temp.add(key);
                    inverse.put(userHash.get(key), temp);
                } else {    // otherwise make a new ArrayList
                    inverse.put(userHash.get(key), new ArrayList<String>());
                    ArrayList<String> temp = inverse.get(userHash.get(key));
                    temp.add(key);
                    inverse.put(userHash.get(key), temp);
                    // only add new word counts to the array list
                    userWordCounts.add(userHash.get(key));
                }
            }
        }
        Collections.sort(userWordCounts, Collections.reverseOrder());
        for (int i = 0; i < userWordCounts.size(); i++) {
            ArrayList<String> tempAL = inverse.get(userWordCounts.get(i));
            for (String s : tempAL) {
                ret.add(s);
            }
        }
        return ret;
    }

    public static ArrayList<String> combinePredictions(ArrayList<String> userPred, ArrayList<String> dictPred) {
        int index = 0;
        try {
            while (userPred.size() < 5) {
                // if the add fails break (i.e. user + dict predictions < 5)
                if (dictPred.size() > index) {
                    if (userPred.contains(dictPred.get(index))) {
                        index++;
                        continue;
                    }
                    if (userPred.add(dictPred.get(index))) {
                        index++;
                    }
                }
                break;
            }
        } catch (NullPointerException e) {
            System.out.println("No predictions! New word alert");
        }
        return userPred;

    }
    // and displayPredictions prints them out
    public static void displayPredictions(ArrayList<String> pred) {
        System.out.println("Predictions:");
        // sanity check
        if(pred.size() > 5) {
            for(int i = 0; i < 5; i++) {
                System.out.print("("+(i+1)+") "+pred.get(i)+"\t");
            }
        } else {
            for(int i = 0; i < pred.size(); i++) {
                System.out.print("("+(i+1)+") "+pred.get(i)+"\t");
            }
        }
        System.out.println("");
    }

    // we check if the user accepted a prediction and then we store it into the user_history arraylist
    // case where the user_history arraylist suggests a word based on the prefix
    // definitely have to revise this method
    public static boolean isPredictionCorrect(ArrayList<String> predictions, String option, HashMap<String, Integer> userHistory, Scanner input) {
        System.out.println("");
        String word = "";
        try {
            int choice = Integer.valueOf(option);
            while (true) {
                if (choice > 0 && choice <= predictions.size()) {
                    word = predictions.get(choice - 1);
                    System.out.println("WORD COMPLETED:\t" + word);
                    placeWordInUserHistory(word, userHistory);
                    return true;
                }
                System.out.println("Invalid choice. Please choose a different option");
                displayPredictions(predictions);
                choice = Integer.valueOf(input.next());
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // if word not a new word in userHistory, set the string and increment the integer value
    // if this is a new word in userHistory, set the string and make the integer value 0
    public static void placeWordInUserHistory(String word, HashMap<String, Integer> userHistory) {
        int freq;
        if(userHistory.containsKey(word)) {
            freq = userHistory.get(word);
            userHistory.put(word, freq+1);
        } else {
            userHistory.put(word, 1);
        }
    }
    public static void main(String[] args) {
        String prefix = "";
        String nextChar = "";
        long startTime;
        boolean correctPrediction;

        HashMap<String, Integer> uHistory = new HashMap<String, Integer>();
        ArrayList<String> uPredictions = new ArrayList<>();

        ArrayList<Double> searchTimes = new ArrayList<>();
        ArrayList<String> dPredictions = new ArrayList<>();
        ArrayList<String> finalPredictions = new ArrayList<>();

        DLB dictionary = new DLB();
        dictionary = createDictionary(dictionary);
        uHistory = createUserHistoryFile(uHistory);

        // initializes the scanner
        System.out.println("To complete a word, enter: '$'");
        System.out.println("To exit, enter: '!'");
        Scanner input = new Scanner(System.in);

        boolean isNewWord = true;
        while (true) {
            // is this the start of a new word? if so we need to get the
            // prefix before passing into the DLB
            if (isNewWord) {
                System.out.print("\nEnter the first character: ");
                nextChar = input.next();
                prefix = nextChar;
                isNewWord = false;
            } else {
                startTime = System.nanoTime();
                dPredictions = dictionary.suggestWords(prefix);
                printElapsedTime(startTime, searchTimes);
                uPredictions = findUserPredictions(uHistory, prefix);
                finalPredictions = combinePredictions(uPredictions, dPredictions);
                displayPredictions(finalPredictions);

                System.out.print("\nEnter the next character: ");
                nextChar = input.next();
                prefix += nextChar;
            }
            // for every iteration we check if the user wants to end the run
            // works for all cases: the first word, a new word, or a regular old iteration
            // also works with the predictions (1-5)
            if (nextChar.equals("!")) {
                break;
            }
            else if (nextChar.equals("$")) {
                // add the word to our user prediction arraylist
                if (prefix.equals("$")) {
                    System.out.println("You didn't submit a word!");
                } else {
                    prefix = prefix.substring(0, prefix.length() - 1);
                    placeWordInUserHistory(prefix, uHistory);
                }
                prefix = null;
                isNewWord = true;
            }
            else if (!isNewWord) {
                // deal with the number input here

                // and the prefixes



                correctPrediction = isPredictionCorrect(finalPredictions, nextChar, uHistory, input);
                if (correctPrediction) {
                    prefix = null;
                    isNewWord = true;
                }
            }
        }
        input.close();
        writeToUserHistoryFile(uHistory);
        avgSearchTime(searchTimes);
        System.out.println("Bye!");
    }
}