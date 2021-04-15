package tjueførtiåtte.fxui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class GameFileSupport implements IGameFileReading {

    public final static String FILE_EXTENSION = "2048hs";

    private Path getGameUserFolderPath() {
        return Path.of(System.getProperty("user.home"), "tdt4100", "tjuefortiatte");
    }

    private boolean ensureGameUserFolder() {
        try {
            Files.createDirectories(getGameUserFolderPath());
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    private Path getHighScorePath() {
        return getGameUserFolderPath().resolve("highScore" + "." + FILE_EXTENSION);
    }

    public Integer readHighScore(InputStream input) {
        Integer highScore = null;
        try (var scanner = new Scanner(input)) {
            highScore = scanner.nextInt();
        }
        return highScore;
    }

    public Integer readHighScore() throws IOException {
        var todoListPath = getHighScorePath();
        try (var input = new FileInputStream(todoListPath.toFile())) {
            return readHighScore(input);
        }
    }

    public void writeHighScore(Integer highScore, OutputStream output) {
        try (var writer = new PrintWriter(output)) {
            writer.println(highScore);
        }
    }

    public void writeHighScore(Integer highScore) throws IOException {
        var todoListPath = getHighScorePath();
        ensureGameUserFolder();
        try (var output = new FileOutputStream(todoListPath.toFile())) {
        	writeHighScore(highScore, output);
        }
    }
}