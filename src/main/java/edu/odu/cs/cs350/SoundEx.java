package edu.odu.cs.cs350;

/**
 * Provides the soundex function to encode strings into a form allowing them
 * to be compared for "sounding alike".
 */
public class SoundEx {

	private final static Substitutions[] prefixSubstitutions = {
			new Substitutions("hough", "h5"),
			new Substitutions("cough", "kof"),
			new Substitutions("chough", "sof"),
			new Substitutions("laugh", "laf"),
			new Substitutions("rough", "ruf"),
			new Substitutions("tough", "tuf"),
			new Substitutions("enough", "enuf"),
			new Substitutions("trough", "trof"),
			new Substitutions("ps", "s"),
			new Substitutions("pt", "t"),
			new Substitutions("pn", "n"),
			new Substitutions("mn", "n"),
			new Substitutions("wr", "r"),
			new Substitutions("kn", "n"),
			new Substitutions("gn", "n"),
			new Substitutions("x", "z"),
			new Substitutions("ce", "se"),
			new Substitutions("ci", "si"),
			new Substitutions("cy", "sy"),
			new Substitutions("ch", "sh"),
			new Substitutions("c", "k")
	};

	private final static Substitutions[] suffixSubstitutions = {
			new Substitutions("gn", "n"),
			new Substitutions("gns", "ns"),
			new Substitutions("mb", "m")
	};

	private final static Substitutions[] innerSubstitutions = {
			new Substitutions("ce", "se"),
			new Substitutions("ci", "se"),
			new Substitutions("cy", "s"),
			new Substitutions("ch", "s"),
			new Substitutions("igha", "ifa"),
			new Substitutions("ugha", "ufa"),
			new Substitutions("gh", ""),
			new Substitutions("th", "H"),
			new Substitutions("ph", "f"),
			new Substitutions("sce", "se"),
			new Substitutions("sci", "si"),
			new Substitutions("scy", "sy"),
			new Substitutions("tia", "sa"),
			new Substitutions("tio", "so"),
			new Substitutions("x", "ks")
	};

	static final String letterEncoding = "0bkd0fg00gklmn0pkrst0v0s0z";

	/**
	 * Phonetic encoding based on
	 * Hodge V.J., Austin J. (2001) A Novel Binary Spell Checker.
	 * In: Dorffner G., Bischof H., Hornik K. (eds) Artificial Neural Networks —
	 * ICANN 2001.
	 * Lecture Notes in Computer Science, vol 2130. Springer, Berlin, Heidelberg.
	 * https://doi.org/10.1007/3-540-44668-0_167
	 * 
	 * 
	 */
	public static String soundEx(final String string) {
		String encoded = string.toLowerCase();
		if (encoded.equals("")) {
			return "";
		}

		// prefix transformations
		encoded = prefixTransformations(encoded);

		// ending transformations
		encoded = suffixTransformations(encoded);

		// Inner substitutions
		StringBuffer buff = innerTransformations(encoded);

		//
		// Letter encoding
		encoded = letterEncoding(buff);

		// Remove adjacent duplicates
		encoded = removeAdjacentDuplicates(encoded);

		// Remove all 0's (vowels and other barely-sounded letters)
		encoded = removeInaudibleLetters(encoded);

		return encoded;
	}

	/**
	 * Remove letters encoded as 0 (vowels and unvoiced consonants)
	 * @param encoded the string to modify
	 * @return the modified string
	 */
	private static String removeInaudibleLetters(String encoded) {
		StringBuffer buff;
		buff = new StringBuffer();
		for (int i = 0; i < encoded.length(); ++i) {
			char c = encoded.charAt(i);
			if (c != '0') {
				buff.append(c);
			}
		}
		encoded = buff.toString();
		return encoded;
	}

	/**
	 * Replace any consecutive duplicates by a single instance.
	 * @param encoded the string to modify
	 * @return the modified string
	 */
	private static String removeAdjacentDuplicates(String encoded) {
		StringBuffer buff;
		buff = new StringBuffer();
		buff.append(encoded.charAt(0));
		char lastChar = encoded.charAt(0);
		for (int i = 1; i < encoded.length(); ++i) {
			char c = encoded.charAt(i);
			if (c != lastChar) {
				buff.append(c);
				lastChar = c;
			}
		}
		encoded = buff.toString();
		return encoded;
	}

	/**
	 * Re-encode the letters of a word to merge letters that sound similar into
	 * a single character code.
	 * @param buff the string to modify
	 * @return the modified string
	 */
	private static String letterEncoding(StringBuffer buff) {
		String encoded;
		encoded = buff.toString();
		buff = new StringBuffer();
		buff.append(encoded.charAt(0));
		for (int position = 1; position < encoded.length(); ++position) {
			char c = encoded.charAt(position);
			if (c >= 'a' && c <= 'z')
				buff.append(letterEncoding.charAt(c - 'a'));
			else
				buff.append(c);
		}
		encoded = buff.toString();
		return encoded;
	}

	/**
	 * Apply all transformation rules that apply to the interior of a word.
	 * @param encoded the word to modify
	 * @return the modified string
	 */
	private static StringBuffer innerTransformations(String encoded) {
		StringBuffer buff = new StringBuffer();
		buff.append(encoded.charAt(0));

		int position = 1;
		while (position < encoded.length()) {
			position = attemptTransformationsAtPosition(encoded, buff, position);
		}
		return buff;
	}

	/**
	 * Attempt all inner transformation rules at a specific position in a string.
	 * @param encoded the string to modify
	 * @param buff the space to store modified characters
	 * @param position the position of encoded at which to attempt this
	 * @return the position at which to attempt the next set of transformations
	 */
	private static int attemptTransformationsAtPosition(String encoded, StringBuffer buff, int position) {
		int position0 = position;
		position = checkAllTransformations(encoded, buff, position);
		if (position0 == position) {
			buff.append(encoded.charAt(position));
			++position;
		}
		return position;
	}

	/**
	 * Check each inner substitution rule against the indicated position in the string.
	 * @param encoded the input string
	 * @param buff accumulated changes to that string
	 * @param position position within encoded to be checked
	 * @return
	 */
	private static int checkAllTransformations(String encoded, StringBuffer buff, int position) {
		for (Substitutions substitution : innerSubstitutions) {
			if (keyMatchesAtThisPosition(encoded, position, substitution)) {
				buff.append(substitution.getReplacement());
				position += substitution.getKey().length();
				break;
			}
		}
		return position;
	}

	/**
	 * Test to see if the indicated string can match a substitution key at
	 * a specific position.
	 * @param encoded the string to be checked
	 * @param position ths position within encoded at which to check
	 * @param substitution a substitution
	 * @return true if the substitution key matches the contents of encoded starting at pos
	 */
	private static boolean keyMatchesAtThisPosition(String encoded, int position, Substitutions substitution) {
		return position < encoded.length() - substitution.getKey().length() &&
				(encoded.substring(position, position + substitution.getKey().length()).equals(substitution.getKey()));
	}

	/**
	 * Apply special transformation rules that work on the end of a word.
	 * @param encoded the string to examine
	 * @return the modified string
	 */
	private static String suffixTransformations(String encoded) {
		for (Substitutions substitution : suffixSubstitutions) {
			if (encoded.endsWith(substitution.getKey())) {
				encoded = encoded.substring(0, encoded.length() - substitution.getKey().length())
						+ substitution.getReplacement();
				break;
			}
		}
		return encoded;
	}

	/**
	 * Apply special transformation rules that work on the beginning of a word.
	 * @param encoded the string to examine
	 * @return the modified string
	 */
	private static String prefixTransformations(String encoded) {
		for (Substitutions substitution : prefixSubstitutions) {
			if (encoded.startsWith(substitution.getKey())) {
				encoded = substitution.getReplacement() + encoded.substring(substitution.getKey().length());
				break;
			}
		}
		return encoded;
	}

}
