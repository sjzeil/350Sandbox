package edu.odu.cs.cs350;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * This program analyzes a spelling dictionary to determine the
 * 10 largest sets of words that are considered near-homophones
 * (approximately sounding alike) according to the SoundEx class.
 */
public class NearHomophones {

    /**
     * This class is used to compare two soundex codes by the size
     * of the set of words associated with each code.
     */
    public class CompareMappings implements Comparator<String> {
        private Map<String, Set<String>> soundAlike;

        /**
         * Construct a comparator.
         * @param soundAlike mapping of soundex codes to words
         */
        CompareMappings(Map<String, Set<String>> soundAlike) {
            this.soundAlike = soundAlike;
        }

        @Override
        public int compare(String left, String right) {
            int rSize = soundAlike.get(right).size();
            int lSize = soundAlike.get(left).size();
            if (rSize != lSize)
                return rSize - lSize;
            else
                return left.compareTo(right);
        }

    }

    /**
     * Main program to run the analysis.
     */
    public static void main(String[] arguments) {
        new NearHomophones().doIt();
    }

    /**
     * Member function to perform tha analysis of the
     * near-homophones in the provided dictionary.
     */
    public void doIt() {
        ArrayList<String> dictionary = new ArrayList<String>();
        Map<String, Set<String>> soundAlike = new HashMap<>();
        loadDictionary(dictionary);
        System.out.println("Loaded dictionary with " + dictionary.size() + " words.");

        ArrayList<String> soundExCodes = new ArrayList<>();
        collectNearHomophones(dictionary, soundAlike, soundExCodes);

        Collections.sort(soundExCodes, new CompareMappings(soundAlike));

        report(soundAlike, soundExCodes);

    }

    /**
     * Print the report on most common sound-alikes.
     * @param soundAlike mapping of soundex codes to words
     * @param soundExCodes list of all soundexCodes
     */
    private void report(Map<String, Set<String>> soundAlike, ArrayList<String> soundExCodes) {
        for (int i = 0; i < 10; ++i) {
            System.out.print(soundExCodes.get(i) + " is the code for");
            for (String word : soundAlike.get(soundExCodes.get(i))) {
                System.out.print(" " + word);
            }
            System.out.println();
            System.out.println();
        }
    }

    /**
     * Collect all words into a mapping by soundex code.
     * @param dictionary all words to be considered
     * @param soundAlike a mapping from soundex codes to words
     * @param soundExCodes list of all soundex codes
     */
    private void collectNearHomophones(ArrayList<String> dictionary, Map<String, Set<String>> soundAlike,
            ArrayList<String> soundExCodes) {
        for (String word : dictionary) {
            String soundEx1 = SoundEx.soundEx(word);
            Set<String> homophones = soundAlike.get(soundEx1);
            if (homophones == null) {
                homophones = new TreeSet<>();
                soundAlike.put(soundEx1, homophones);
                soundExCodes.add(soundEx1);
            }
            homophones.add(word);
        }
    }

    /**
     * Read the dictionary file.
     * 
     * @param dictionary list of words read from the file (output)
     */
    private void loadDictionary(ArrayList<String> dictionary) {
        Path inputFile = Paths.get("src", "test", "data", "words.txt");
        try (
                BufferedReader in = new BufferedReader(new FileReader(inputFile.toFile()))) {
            String line = in.readLine();
            while (line != null) {
                dictionary.add(line.trim());
                line = in.readLine();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
