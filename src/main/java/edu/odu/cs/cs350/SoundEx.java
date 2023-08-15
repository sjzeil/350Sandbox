package edu.odu.cs.cs350;

public class SoundEx {

	private final static Substitutions[] prefixSubst = {
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

	private final static Substitutions[] suffixSubst = {
			new Substitutions("gn", "n"),
			new Substitutions("gns", "ns"),
			new Substitutions("mb", "m")
	};

	private final static Substitutions[] innerSubst = {
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
	public static String soundEx(final String str) {
		String encoded = str.toLowerCase();
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

	private static String letterEncoding(StringBuffer buff) {
		String encoded;
		encoded = buff.toString();
		buff = new StringBuffer();
		buff.append(encoded.charAt(0));
		for (int posn = 1; posn < encoded.length(); ++posn) {
			char c = encoded.charAt(posn);
			if (c >= 'a' && c <= 'z')
				buff.append(letterEncoding.charAt(c - 'a'));
			else
				buff.append(c);
		}
		encoded = buff.toString();
		return encoded;
	}

	private static StringBuffer innerTransformations(String encoded) {
		StringBuffer buff = new StringBuffer();
		buff.append(encoded.charAt(0));

		int pos = 1;
		while (pos < encoded.length()) {
			boolean done = false;
			for (Substitutions subst : innerSubst) {
				if (pos < encoded.length() - subst.getKey().length()) {
					if (encoded.substring(pos, pos + subst.getKey().length()).equals(subst.getKey())) {
						buff.append(subst.getReplacement());
						done = true;
						pos += subst.getKey().length();
					}
				}
			}
			if (!done) {
				buff.append(encoded.charAt(pos));
				++pos;
			}
		}
		return buff;
	}

	private static String suffixTransformations(String encoded) {
		for (Substitutions subst : suffixSubst) {
			if (encoded.endsWith(subst.getKey())) {
				encoded = encoded.substring(0, encoded.length() - subst.getKey().length())
						+ subst.getReplacement();
				break;
			}
		}
		return encoded;
	}

	private static String prefixTransformations(String encoded) {
		for (Substitutions subst : prefixSubst) {
			if (encoded.startsWith(subst.getKey())) {
				encoded = subst.getReplacement() + encoded.substring(subst.getKey().length());
				break;
			}
		}
		return encoded;
	}

}
