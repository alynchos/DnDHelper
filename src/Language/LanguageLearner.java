package Language;

import Constants.LanguageConstants;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * This is the worker for translating into a partially learned language.
 *
 * ===================== Example =====================
 * Known words list: [hello, not, do, this]
 *
 * Input: Hello, I do not know this whole language
 *
 * Output: Hello, - do not ---- this ----- --------
 */
public class LanguageLearner {

    private static final char REDACT_CHAR = '-';

    public LanguageLearner() {
    }

    //TODO: FIX BUG WITH "'"
    public String process(String input, final int level, final Set<String> adHocWords) throws IOException {
        StringBuilder output = new StringBuilder();
        Set<String> languageSet = getLanguageLevelArray(level);
        Set<String> usedSet = Stream.concat(languageSet.stream(), adHocWords.stream()).collect(Collectors.toSet());
        String[] parsed = fixNewlines(input).split(" +|(?=\\p{Punct})|(?<=\\p{Punct})");
        for (String word : parsed) {
            String curr = word.toLowerCase();
            if (curr.length() == 1 && !isNumber(curr) && !isPunctuation(curr)) {
                output.append(word);
            } else if (usedSet.contains(curr) || isNewline(curr)) {
                output.append(word);
            } else if (isPunctuation(curr)) {
                fixPunctuation(output, curr);
            } else if (level >= 5 && isNumber(curr)) {
                output.append(word);
            } else if (level >= 4 && canConjugate(curr, usedSet)) {
                output.append(word);
            } else {
                output.append(redact(curr));
            }
            if (curr.length() > 0 && !isNewline(curr)) output.append(' ');
        }
        return output.toString();
    }

    private String redact(String input) {
        char[] output = new char[input.length()];
        Arrays.fill(output, REDACT_CHAR);
        return new String(output);
    }

    private Set<String> getLanguageLevelArray(int level) throws IOException {
        switch (level) {
            case 1:
                return LanguageConstants.LEVEL_1;
            case 2:
                return LanguageConstants.LEVEL_2;
            case 3:
                return LanguageConstants.LEVEL_3;
            case 4:
                return LanguageConstants.LEVEL_4;
            case 5:
                return LanguageConstants.LEVEL_5;
            case 6:
                return LanguageConstants.LEVEL_6;
            default:
                throw new IOException(String.format("The given level [%d] is unsupported.", level));
        }
    }

    private boolean isNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isNewline(String input) {
        return Pattern.matches("[\n\t\r]", input);
    }

    private String fixNewlines(String input) {
        return input.replaceAll("\n", " \n ")
                .replaceAll("\r", " \r ")
                .replaceAll("\t", " \t ")
                .replaceAll("'", " ' "); // Hacky conjunction "fix", not a newline ><
    }

    private boolean isPunctuation(String input) {
        return Pattern.matches("\\p{Punct}", input);
    }

    private void fixPunctuation(StringBuilder output, String curr) {
        if (Pattern.matches("[!.,;:'?]", curr) && output.length() > 1) {
            output.deleteCharAt(output.length() - 1);
        }
        output.append(curr);
    }

    // This does not catch every single conjugation, but a vast majority, like if you were learning a language!
    private boolean canConjugate(String input, Set<String> usedSet) {
        for (String conj : LanguageConstants.CONJUGATIONS) {
            if (input.endsWith(conj)) {
                // Catches normal conjugation case (i.e. dog -> dogs)
                String trimmed = input.substring(0, input.length() - conj.length());
                if (usedSet.contains(trimmed)) return true;
                if (input.length() <= conj.length()) continue;
                // Catches semi-normal conjugation case (i.e. run -> running)
                trimmed = input.substring(0, input.length() - conj.length() - 1);
                if (usedSet.contains(trimmed)) return true;
                // Catches some edge conjugation case (i.e. involve -> involving)
                trimmed = input.substring(0, input.length() - conj.length()) + 'e';
                if (usedSet.contains(trimmed)) return true;
                // Catches some edge conjugation case (i.e. enemy -> enemies)
                trimmed = input.substring(0, input.length() - conj.length()) + 'y';
                if (usedSet.contains(trimmed)) return true;
            }
        }
        return false;
    }
}
