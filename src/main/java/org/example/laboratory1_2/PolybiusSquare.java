package org.example.laboratory1_2;

import org.example.interfaces.Password;

import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class PolybiusSquare implements Password {
    private static final int COUNT_COLUMN = 5;
    private static final char START_LETTER = 'A';
    private static final char END_LETTER = 'Z';
    private static final String IGNORE_LETTER = "J";
    private static final String BIGRAM_TEMPLATE = "(?<=\\G.{2})";
    private static final String SPACE_TEMPLATE = "\\s+";
    private static final String SPACE_REPLACEMENT_TEMPLATE = "";

    private final Character[][] matrix;
    private final HashMap<Character, String> hashEncoder;
    private final HashMap<String, Character> hashDecoder;

    public PolybiusSquare() {
        this("Independent Ukraine");
    }

    public PolybiusSquare(String key) {
        this.matrix = createMatrix(key);
        this.hashEncoder = new HashMap<>();
        this.hashDecoder = new HashMap<>();
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

        key = key
                .replaceAll(SPACE_TEMPLATE, SPACE_REPLACEMENT_TEMPLATE)
                .toUpperCase();

        key.chars().forEach(ch -> rowShift.add((char) ch));
        IntStream.rangeClosed(START_LETTER, END_LETTER).forEach(i -> rowShift.add((char) i));

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
        password = password.
                toUpperCase()
                .replaceAll(IGNORE_LETTER, "I")
                .replaceAll(SPACE_TEMPLATE, SPACE_REPLACEMENT_TEMPLATE);

        var builder = new StringBuilder();

        for(var ch : password.toCharArray()) {
            builder.append(encoderLetter(ch));
        }

        return builder.toString();
    }

    private String encoderLetter(char letter) {
        if(hashEncoder.containsKey(letter)) {
            return hashEncoder.get(letter);
        }

        var code =  findInMatrix(letter);
        hashEncoder.put(letter, code);
        return code;
    }

    private String findInMatrix(char letter) {
        int indexRow = 0;
        int indexColumn;

        for(; indexRow < matrix.length; ++indexRow) {
            var row = matrix[indexRow];

            for (indexColumn = 0; indexColumn < row.length; ++indexColumn) {
                if(row[indexColumn] == letter) {
                    return String.format("%d%d", indexRow, indexColumn);
                }
            }
        }

        return "";
    }


    @Override
    public String decoder(String encryptedPassword) {
        var bigrams = encryptedPassword
                .replaceAll(SPACE_TEMPLATE, SPACE_REPLACEMENT_TEMPLATE)
                .split(BIGRAM_TEMPLATE);

        var builder = new StringBuilder();

        for(var bigram : bigrams) {
            builder.append(decoderLetter(bigram));
        }

        return builder.toString();
    }

    private char decoderLetter(String encrypt) {
        if(hashDecoder.containsKey(encrypt)) {
            return hashDecoder.get(encrypt);
        }

        var letter =  findLetter(encrypt);
        hashDecoder.put(encrypt, letter);
        return letter;
    }

    private char findLetter(String encrypt) {
        var coordinate = encrypt.split("");
        var indexRow = Integer.parseInt(coordinate[0]);
        var indexColumn = Integer.parseInt(coordinate[1]);

        return matrix[indexRow][indexColumn];
    }
}
