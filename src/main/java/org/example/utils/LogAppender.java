package org.example.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogAppender {
    private static final String LOG_FILE = "logs/beerbatch.log";

    public static void appendNewLineToLog() {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw, true)) {
            pw.println(); // Writes a newline character to the log file
        } catch (IOException e) {
            System.err.println("Error appending newline to log file: " + e.getMessage());
        }
    }
}
