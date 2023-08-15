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

public class NearHomophones {

    public class CompareMappings implements Comparator<String> {
        private Map<String, Set<String>> soundAlike;

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

    public static void main(String[] args) {
        new NearHomophones().doIt();
    }

    public void doIt() {
        ArrayList<String> dictionary = new ArrayList<String>();
        Map<String, Set<String>> soundAlike = new HashMap<>();
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
        System.out.println("Loaded dictionary with " + dictionary.size() + " words.");

        ArrayList<String> soundExCodes = new ArrayList<>();
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

        Collections.sort(soundExCodes, new CompareMappings(soundAlike));

        for (int i = 0; i < 10; ++i) {
            System.out.print(soundExCodes.get(i) + " is the code for");
            for (String word : soundAlike.get(soundExCodes.get(i))) {
                System.out.print(" " + word);
            }
            System.out.println();
            System.out.println();
        }

    }

}
