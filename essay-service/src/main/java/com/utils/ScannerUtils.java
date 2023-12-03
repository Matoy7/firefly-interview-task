package com.utils;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@Log4j2
public class ScannerUtils {
    public static Set<String> getLists(String filePath) {
        Set<String> wordList = new HashSet<>();
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String currentLine = scanner.nextLine();
                wordList.add(currentLine);
            }
        } catch (Exception exception) {
            log.error(" this error had occured during parsing " + filePath + " ,error: " + exception);
        }
        return wordList;
    }
}
