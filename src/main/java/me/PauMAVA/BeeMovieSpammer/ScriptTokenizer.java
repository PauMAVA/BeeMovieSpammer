package me.PauMAVA.BeeMovieSpammer;

import org.openqa.selenium.InvalidArgumentException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ScriptTokenizer {

    private BufferedReader scriptReader;

    private final SeparationType type;

    private String currentLine = null;

    public ScriptTokenizer(String filePath, SeparationType type) throws InvalidArgumentException {
        this.type = type;
        if (!loadScript(filePath)) {
            throw new IllegalArgumentException("Unable to load bee movie script.");
        }
    }

    private boolean loadScript(String filePath) {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            try {
                this.scriptReader = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    public String nextToken() throws IOException {
        refreshCurrentLine();
        switch (type) {
            case LINE: return nextLine();
            case LETTER: return nextLetter();
            default: return nextWord();
        }
    }

    private String nextWord() {
        int space = currentLine.indexOf(" ");
        String word;
        if (space == -1) {
            word = currentLine;
            currentLine = null;
        } else {
            word = currentLine.substring(0, space);
            currentLine = currentLine.substring(space + 1);
        }
        return word;
    }

    private String nextLine() {
        String line = currentLine;
        currentLine = null;
        return line;
    }

    private String nextLetter() {
        String letter;
        if (currentLine.length() == 1) {
            letter = currentLine;
            currentLine = null;
        } else {
            letter = String.valueOf(currentLine.charAt(0));
            currentLine = currentLine.substring(1);
        }
        return letter;
    }

    private void refreshCurrentLine() throws IOException {
        while (currentLine == null || currentLine.isEmpty() || currentLine.isBlank() ||currentLine.startsWith("\n")) {
            currentLine = scriptReader.readLine();
            if (currentLine != null) {
                currentLine = currentLine.replace("\n", "");
            }
        }
    }

}
