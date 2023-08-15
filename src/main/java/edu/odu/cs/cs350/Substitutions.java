package edu.odu.cs.cs350;

/**
 * A simple container for pairs of strings.
 */
public class Substitutions {

    private String key;
    private String repl;

    /**
     * Create oe substitution object
     * @param key a string to search for
     * @param replacement a replacement for that string, when found
     */
    public Substitutions(String key, String replacement) {
        this.key = key;
        this.repl = replacement;
    }

    /**
     * @return the key string to search for
     */
    public String getKey() {
        return key;
    }

    public String getReplacement() {
        return repl;
    }

    
}
