// Jarod Miller (jcm138)

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
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
                String str;
                String num;
                String delim = "[_]";
                String[] tokens;
                while(line != null) {
                    tokens = line.split(delim);
                    str = tokens[0];
                    num = tokens[1];
                    int freq = Integer.parseInt(String.valueOf(num));
                    hm.put(str, freq);
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
            Iterator<String> iterateS = hm.keySet().iterator();
            Iterator<Integer> iterateI = hm.values().iterator();
            while(iterateS.hasNext()) {
                printWriter.println(iterateS.next()+"_"+iterateI.next());
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
    public static HashMap<String, Integer> findUserPredictions(HashMap<String, Integer> userHash, String prefix) {
        HashMap<String, Integer> userPredictions = new HashMap<String, Integer>();
        Iterator<String> iterateS = userHash.keySet().iterator();
        String key;
        while(iterateS.hasNext()) {
            // now we have to check each iteration to see if each string
            // in the HashMap has the current prefix
            key = iterateS.next();
            if(key.startsWith(prefix)) {
                userPredictions.put(key, userHash.get(key));
            }
        }
        // note that this is not sorted by highest frequency yet.
        return userPredictions;
    }
    public static ArrayList<String> combinePredictions(HashMap<String, Integer> userPred, ArrayList<String> dictPred) {
        HashMap<String,Integer> noDuplicates = new HashMap<String, Integer>();
        noDuplicates.putAll(userPred);
        int arrIndex = 0;
        int freq;
        // ensures no duplicates are in the final prediction arraylist
        while(noDuplicates.size() < dictPred.size()) {
            if(noDuplicates.containsKey(dictPred.get(arrIndex))) {
                freq = noDuplicates.get(dictPred.get(arrIndex));
                noDuplicates.put(dictPred.get(arrIndex), freq);
                arrIndex++;
            } else {
                noDuplicates.put(dictPred.get(arrIndex), 0);
                arrIndex++;
            }
        }
        ArrayList<String> finalPred = new ArrayList<>(noDuplicates.keySet());
        return finalPred;
    }
    // and displayPredictions prints them out
    public static void displayPredictions(ArrayList<String> pred) {
        System.out.println("Predictions:");
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

    
    public static boolean isPredictionCorrect(ArrayList<String> predictions, String option, HashMap<String, Integer> userHistory) {
        System.out.println("");
        String word;
        if(option.equals("1")) {
            word = predictions.get(0);
            System.out.println("WORD COMPLETED:\t" + word);
            placeWordInUserHistory(word, userHistory);
            return true;
        }
        else if(option.equals("2")) {
            word = predictions.get(1);
            System.out.println("WORD COMPLETED:\t" + word);
            placeWordInUserHistory(word, userHistory);
            return true;
        }
        else if(option.equals("3")) {
            word = predictions.get(2);
            System.out.println("WORD COMPLETED:\t" + word);
            placeWordInUserHistory(word, userHistory);
            return true;
        }
        else if(option.equals("4")) {
            word = predictions.get(3);
            System.out.println("WORD COMPLETED:\t" + word);
            placeWordInUserHistory(word, userHistory);
            return true;
        }
        else if(option.equals("5")) {
            word = predictions.get(4);
            System.out.println("WORD COMPLETED:\t" + word);
            placeWordInUserHistory(word, userHistory);
            return true;
        }
        return false;
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
        boolean runProgram = true;
        String prefix;
        String nextChar;
        long startTime;
        boolean correctPrediction;

        HashMap<String, Integer> uHistory = new HashMap<String, Integer>();
        HashMap<String, Integer> uPredictions = new HashMap<String, Integer>();
        ArrayList<Double> searchTimes = new ArrayList<Double>();
        ArrayList<String> dPredictions = new ArrayList<String>();
        ArrayList<String> finalPredictions = new ArrayList<String>();

        DLB dictionary = new DLB();
        dictionary = createDictionary(dictionary);
        uHistory = createUserHistoryFile(uHistory);

        // initializes the scanner
        System.out.print("\nEnter your first character: ");
        Scanner input = new Scanner(System.in);
        nextChar = input.next();
        prefix = nextChar;
        boolean isNewWord = false;

        while(runProgram == true) {
            // is this the start of a new word? if so we need to get the
            // nextChar and prefix before passing into the DLB
            if(isNewWord) {
                System.out.print("\nEnter the first character of the next word: ");
                nextChar = input.next();
                prefix = nextChar;
                isNewWord = false;
            }
            // for every iteration we check if the user wants to end the run
            // works for all cases: the first word, a new word, or a regular old iteration
            // also works with the predictions (1-5)
            if(nextChar.equals("!")) {
                runProgram = false;
                break;
            }
            startTime = System.nanoTime();
            dPredictions = dictionary.suggestWords(prefix);
            printElapsedTime(startTime, searchTimes);
            uPredictions = findUserPredictions(uHistory, prefix);
            finalPredictions = combinePredictions(uPredictions, dPredictions);
            displayPredictions(finalPredictions);
            if(!isNewWord) {
                System.out.print("\nEnter the next character: ");
                nextChar = input.next();
                prefix += nextChar;
            }
            if(nextChar.equals("$")) {
                // this will cause an error if I pass it into my DLB
                // be aware of where to put it when the time comes to implement
                // but we add the word to our user prediction arraylist.
                prefix = prefix.substring(0, prefix.length() - 1);
                placeWordInUserHistory(prefix, uHistory);
                prefix = null;
                isNewWord = true;
            } else {
                correctPrediction = isPredictionCorrect(finalPredictions, nextChar, uHistory);
                if(correctPrediction) {
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