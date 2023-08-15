package edu.odu.cs.cs350;

public class Substitutions {

    private String key;
    private String repl;

    public Substitutions(String key, String replacement) {
        this.key = key;
        this.repl = replacement;
    }

    public String getKey() {
        return key;
    }

    public String getReplacement() {
        return repl;
    }

    
}
