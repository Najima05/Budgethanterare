package com.example.java26;

// Kontrollerar för trasiga rader i CSV-filen

public class FileFormatException  extends Exception {
    public FileFormatException(String meddelande) {
        super(meddelande);
    }

    public FileFormatException(String meddelande, Throwable cause) {
        super(meddelande, cause);
    }
}
