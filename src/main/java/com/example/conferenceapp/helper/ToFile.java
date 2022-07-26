package com.example.conferenceapp.helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ToFile {

    public static void saveFile(String filename, String massage) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy  HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formatted = formatter.format(now);
        Path filePath = Path.of(filename);

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath.toFile(), true))) {

            writer.write("\n" + formatted + " " + massage);
        }

    }
}
