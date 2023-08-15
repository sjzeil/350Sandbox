package edu.odu.cs.cs350;

public class TestSoundEx {

    private static final String[] nearHomophones = {
        "skit", "scat",
        "to", "two",
        "psych", "sack",
        "knife", "naff",
        "book", "back",
        "quick", "quake",
        "sadder", "cider"
    };

    public static void main(String[] args) {
        match("to", "two");
        match("too", "two");
        match("so", "sew");
        noMatch("to", "sew");
        
        for (int i = 0; i < nearHomophones.length; i += 2) {
            for (int j = 0; j < nearHomophones.length; j += 2) {
                if (i == j) {
                    match(nearHomophones[i], nearHomophones[i+1]);
                } else {
                    noMatch(nearHomophones[i], nearHomophones[j]);
                }
            }
        }
        System.out.println("done");
    }
    
    private static void match(String string1, String string2) {
        String s1 = SoundEx.soundEx(string1);
        String s2 = SoundEx.soundEx(string2);
        if (!s1.equals(s2)) {
            System.out.println("Unexpected mismatch between " + string1 
            + " and " + string2);
        }
    }
    
    private static void noMatch(String string1, String string2) {
        String s1 = SoundEx.soundEx(string1);
        String s2 = SoundEx.soundEx(string2);
        if (s1.equals(s2)) {
            System.out.println("Unexpected match between " + string1 
            + " and " + string2);
        }
    }
}
