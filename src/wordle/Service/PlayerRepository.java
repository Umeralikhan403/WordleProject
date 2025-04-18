package wordle.Service;

import wordle.Models.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerRepository {
    private static final String FILE_NAME = "players.dat";

    /** load all players (never null) */
    public static List<Player> loadAll() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<Player>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new UncheckedIOException(new IOException("Could not read players", e));
        }
    }

    /** save all players */
    public static void saveAll(List<Player> players) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(players);
        } catch (IOException e) {
            throw new UncheckedIOException(new IOException("Could not write players", e));
        }
    }

    /** register a new player, or throw if username taken */
    public static Player register(String username, String password) {
        List<Player> all = loadAll();
        for (Player p : all) {
            if (p.getUsername().equalsIgnoreCase(username)) {
                throw new IllegalArgumentException("Username already exists");
            }
        }
        Player newP = new Player(username, password);
        all.add(newP);
        saveAll(all);
        return newP;
    }

    /** authenticate, empty if not found or bad password */
    public static Optional<Player> authenticate(String username, String password) {
        return loadAll().stream()
                .filter(p -> p.getUsername().equalsIgnoreCase(username)
                          && p.getPassword().equals(password))
                .findFirst();
    }
    
    public static void printPlayers() {
        List<Player> players = loadAll();
        System.out.println("=== Registered Players ===");
        for (Player p : players) {
            System.out.println("Username: " + p.getUsername() + ", High Score: " + p.getHighScore());
        }
        System.out.println("==========================");
    }
}