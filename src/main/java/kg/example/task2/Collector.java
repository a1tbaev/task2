package kg.example.task2;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Collector {
    private static final int RECORDS_PER_FILE = 4;
    private static final String LOG_FILES_DIR = System.getProperty("user.home") + "/Desktop/parser/";
    private static final String STATE_FILE = System.getProperty("user.home") + "/Desktop/parser/state.txt";

    public static void main(String[] args) throws IOException {
        State state = readState();
        int lastProcessedFileIndex = state.lastProcessedFileIndex;
        int lastProcessedRecordIndex = state.lastProcessedRecordIndex;

        Path logFilesDir = Paths.get(LOG_FILES_DIR);
        if (Files.exists(logFilesDir)) {
            List<Path> logFiles = Files.list(logFilesDir)
                    .filter(path -> path.toString().endsWith(".json") && !path.toString().matches(".*-\\d{4}\\.log"))
                    .sorted()
                    .toList();

            List<String> currentBatchRecords = new ArrayList<>();
            int currentBatchFileIndex = state.currentBatchFileIndex;

            for (int i = lastProcessedFileIndex; i < logFiles.size(); i++) {
                List<String> records = readRecordsFromFile(logFiles.get(i), lastProcessedRecordIndex);
                lastProcessedRecordIndex = 0;

                for (String record : records) {
                    currentBatchRecords.add(record);
                    if (currentBatchRecords.size() == RECORDS_PER_FILE) {
                        saveRecordsToFile(currentBatchRecords, currentBatchFileIndex++);
                        currentBatchRecords.clear();
                    }
                }
                lastProcessedFileIndex = i + 1;
                updateState(lastProcessedFileIndex, lastProcessedRecordIndex, currentBatchFileIndex);
            }


            if (!currentBatchRecords.isEmpty()) {
                saveRecordsToFile(currentBatchRecords, currentBatchFileIndex);
            }
        } else {
            System.out.println("Log files directory does not exist.");
        }
    }

    private static List<String> readRecordsFromFile(Path logFilePath, int startFromRecordIndex) throws IOException {
        List<String> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath.toFile()))) {
            String line;
            int currentIndex = 0;
            while ((line = reader.readLine()) != null) {
                if (currentIndex++ >= startFromRecordIndex) {
                    records.add(line);
                }
            }
        }
        return records;
    }

    private static void saveRecordsToFile(List<String> records, int fileIndex) {
        String baseFileName = "merged";
        String fileName = String.format("%s-%04d.log", baseFileName, fileIndex);
        Path outputPath = Paths.get(LOG_FILES_DIR, fileName);
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath.toFile(), true))) {
            for (String record : records) {
                writer.println(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateState(int fileIndex, int recordIndex, int batchFileIndex) {
        try (PrintWriter stateWriter = new PrintWriter(new FileWriter(STATE_FILE))) {
            stateWriter.println(fileIndex);
            stateWriter.println(recordIndex);
            stateWriter.println(batchFileIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static State readState() {
        State state = new State(0, 0, 0);
        File stateFile = new File(STATE_FILE);
        if (stateFile.exists()) {
            try (BufferedReader stateReader = new BufferedReader(new FileReader(stateFile))) {
                state.lastProcessedFileIndex = Integer.parseInt(stateReader.readLine());
                state.lastProcessedRecordIndex = Integer.parseInt(stateReader.readLine());
                state.currentBatchFileIndex = Integer.parseInt(stateReader.readLine());
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return state;
    }

    private static class State {
        int lastProcessedFileIndex;
        int lastProcessedRecordIndex;
        int currentBatchFileIndex;

        State(int lastProcessedFileIndex, int lastProcessedRecordIndex, int currentBatchFileIndex) {
            this.lastProcessedFileIndex = lastProcessedFileIndex;
            this.lastProcessedRecordIndex = lastProcessedRecordIndex;
            this.currentBatchFileIndex = currentBatchFileIndex;
        }
    }
}
