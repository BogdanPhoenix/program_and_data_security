package org.example.laboratory1_2;

import org.example.laboratory1_2.exception.BigramException;
import org.example.laboratory1_2.exception.InvalidInputException;
import org.example.laboratory1_2.exception.PairIdenticalLettersException;
import org.example.interfaces.Password;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class PlayfairCipher implements Password {
    private static final int COUNT_COLUMN = 6;
    private static final int NEXT_LEVEL = 1;
    private static final char START_LETTER = 'A';
    private static final char END_LETTER = 'Z';
    private static final char START_NUM = '0';
    private static final char END_NUM = '9';
    private static final char SPACE = ' ';
    private static final String IGNORE_LETTER = "J";
    private static final String BIGRAM_TEMPLATE = "(?<=\\G.{2})";

    private final Character[][] matrix;

    public PlayfairCipher() {
        this("ve3ry se1cr61et");
    }

    public PlayfairCipher(String key) {
        this.matrix = createMatrix(key);
    }

    private Character[][] createMatrix(String key) {
        var rowShift = shuffleAlphabet(key);
        var tableBuffer = createTableShuffleAlphabet(rowShift);

        var rowCount = tableBuffer.size();
        var columnCount = tableBuffer.getFirst().size();

        var matrixBuffer = new Character[rowCount][columnCount];

        for(int i = 0; i < rowCount; i++) {
            var row = tableBuffer.get(i).toArray(new Character[0]);
            System.arraycopy(row, 0, matrixBuffer[i], 0, row.length);
        }

        return matrixBuffer;
    }

    private Set<Character> shuffleAlphabet(String key) {
        var rowShift = new LinkedHashSet<Character>();

        key = key.toUpperCase();

        key.chars().forEach(ch -> rowShift.add((char) ch));
        IntStream.rangeClosed(START_LETTER, END_LETTER).forEach(i -> rowShift.add((char) i));
        IntStream.rangeClosed(START_NUM, END_NUM).forEach(i -> rowShift.add((char) i));

        rowShift.add(SPACE);

        return rowShift;
    }

    private List<Set<Character>> createTableShuffleAlphabet(Set<Character> rowShift) {
        List<Set<Character>> tableBuffer = new ArrayList<>();
        Set<Character> row = new LinkedHashSet<>();

        for(var ch : rowShift) {
            if(isIgnoreSymbols(ch)) {
                continue;
            }

            row.add(ch);

            if(row.size() == COUNT_COLUMN) {
                tableBuffer.add(row);
                row = new LinkedHashSet<>();
            }
        }

        return tableBuffer;
    }

    private boolean isIgnoreSymbols(Character character) {
        return IGNORE_LETTER.contains(character.toString());
    }

    @Override
    public String encoder(String password) {
        var bigrams = password
                .toUpperCase()
                .replaceAll(IGNORE_LETTER, "I")
                .split(BIGRAM_TEMPLATE);
        validatePassword(bigrams);

        return work(bigrams, this::encryptionBigram);
    }

    private void validatePassword(String[] bigrams) throws InvalidInputException, BigramException, PairIdenticalLettersException {
        if (!isValidInput(bigrams)) {
            throw new InvalidInputException("In the provided ones, you entered a character that is not supported by the system. The system works only with Latin letters, Arabic numerals, and spaces.");
        }
        if (bigrams[bigrams.length - 1].length() % 2 != 0) {
            throw new BigramException("The password you provide must contain an even number of characters.");
        }
        if (!isValidBigrams(bigrams)) {
            throw new PairIdenticalLettersException(String.format("A bigram with two identical letters was found in the password broken into bigrams: %s. This is a violation of the rules.", Arrays.toString(bigrams)));
        }
    }

    private boolean isValidInput(String[] bigrams) {
        for(var bigram : bigrams) {
            if(!bigram.matches("^[A-Z0-9\\s]+$")) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidBigrams(String[] bigrams) {
        for(var bigram : bigrams) {
            if(bigram.charAt(0) == bigram.charAt(1)) {
                return false;
            }
        }

        return true;
    }

    private String encryptionBigram(String bigram) {
        return processBigram(bigram, true);
    }

    private Point findLetter(char letter) {
        int indexRow = 0;
        int indexColumn;

        for(; indexRow < matrix.length; ++indexRow) {
            var row = matrix[indexRow];

            for (indexColumn = 0; indexColumn < row.length; ++indexColumn) {
                if(row[indexColumn] == letter) {
                    return new Point(indexRow, indexColumn);
                }
            }
        }

        return new Point();
    }

    @Override
    public String decoder(String encryptedPassword) {
        var bigrams = encryptedPassword.split(BIGRAM_TEMPLATE);
        return work(bigrams, this::decryptionBigram);
    }

    private String work(String[] bigrams, UnaryOperator<String> workingMethod) {
        var result = new StringBuilder();
        for(var bigram : bigrams) {
            result.append(workingMethod.apply(bigram));
        }

        return result.toString();
    }

    private String decryptionBigram(String bigram) {
        return processBigram(bigram, false);
    }

    private String processBigram(String bigram, boolean encrypt) {
        var countRows = matrix.length;
        var countColumns = matrix[0].length;

        Point firstLetterPoint = findLetter(bigram.charAt(0));
        Point secondLetterPoint = findLetter(bigram.charAt(1));

        Point first;
        Point second;

        if (firstLetterPoint.y == secondLetterPoint.y) {
            first = shiftPoint(firstLetterPoint, countRows, encrypt, true);
            second = shiftPoint(secondLetterPoint, countRows, encrypt, true);
        } else if (firstLetterPoint.x == secondLetterPoint.x) {
            first = shiftPoint(firstLetterPoint, countColumns, encrypt, false);
            second = shiftPoint(secondLetterPoint, countColumns, encrypt, false);
        } else {
            first = new Point(firstLetterPoint.x, secondLetterPoint.y);
            second = new Point(secondLetterPoint.x, firstLetterPoint.y);
        }

        return getNewBigram(first, second);
    }

    private Point shiftPoint(Point point, int dimensionSize, boolean encrypt, boolean shiftX) {
        var shiftCoordinate = pointValue(point, shiftX) + getNextOrPrevLevel(encrypt);

        if (shiftCoordinate < 0) {
            shiftCoordinate += dimensionSize;
        } else {
            shiftCoordinate %= dimensionSize;
        }
        return shiftX ? new Point(shiftCoordinate, point.y) : new Point(point.x, shiftCoordinate);
    }

    private int pointValue(Point point, boolean shiftX) {
        return shiftX ? point.x : point.y;
    }

    private int getNextOrPrevLevel(boolean encrypt) {
        return encrypt ? NEXT_LEVEL : -NEXT_LEVEL;
    }

    private String getNewBigram(Point firstLetter, Point secondLetter) {
        return String.valueOf(matrix[firstLetter.x][firstLetter.y]) +
                matrix[secondLetter.x][secondLetter.y];
    }
}
