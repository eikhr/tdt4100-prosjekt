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

import tjueførtiåtte.model.Game;

public class GameFileSupport implements IGameFileReading {

    public final static String HIGH_SCORE_FILE_EXTENSION = "2048hs";
    public final static String SAVE_GAME_FILE_EXTENSION = "2048";
    
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
        return getGameUserFolderPath().resolve("highScore" + "." + HIGH_SCORE_FILE_EXTENSION);
    }
    
    private Path getSaveGamePath() {
        return getGameUserFolderPath().resolve("saveGame" + "." + SAVE_GAME_FILE_EXTENSION);
    }

    public Integer readHighScore(InputStream input) {
        Integer highScore = null;
        try (var scanner = new Scanner(input)) {
            highScore = scanner.nextInt();
        }
        return highScore;
    }

    public Integer readHighScore() throws IOException {
        var highScorePath = getHighScorePath();
        try (var input = new FileInputStream(highScorePath.toFile())) {
            return readHighScore(input);
        }
    }

    public void writeHighScore(Integer highScore, OutputStream output) {
        try (var writer = new PrintWriter(output)) {
            writer.println(highScore);
        }
    }

    public void writeHighScore(Integer highScore) throws IOException {
        var highScorePath = getHighScorePath();
        ensureGameUserFolder();
        try (var output = new FileOutputStream(highScorePath.toFile())) {
        	writeHighScore(highScore, output);
        }
    }

	@Override
	public Game readSaveGame(InputStream is) {
		Game game = null;
        try (var scanner = new Scanner(is)) {
            String serializedGame = scanner.next();
            game = new Game(serializedGame);
        }
        return game;
	}

	@Override
	public Game readSaveGame() throws IOException {
		var saveGamePath = getSaveGamePath();
        try (var input = new FileInputStream(saveGamePath.toFile())) {
            return readSaveGame(input);
        }
	}

	@Override
	public void writeSaveGame(Game game, OutputStream os) {
		 try (var writer = new PrintWriter(os)) {
            writer.println(game.serialize());
        }
	}

	@Override
	public void writeSaveGame(Game game) throws IOException {
		var saveGamePath = getSaveGamePath();
        ensureGameUserFolder();
        try (var output = new FileOutputStream(saveGamePath.toFile())) {
        	writeSaveGame(game, output);
        }
	}
}