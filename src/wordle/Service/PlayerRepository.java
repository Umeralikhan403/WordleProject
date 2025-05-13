package wordle.Service;

import wordle.Models.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class handles the persistence of Player objects.
 * It uses serialisation to save and load players to/from a file.
 */
public class PlayerRepository {
    private static final String FILE_NAME = "players.dat";

    /** load all players (never null) */
    /** This method loads all players from the file.
	 * If the file does not exist, it returns an empty list.
	 * @return a list of players
	 */
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
    /**
     * This method saves all players to the file.
     * @param players - the list of players to save
     */
    public static void saveAll(List<Player> players) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(players);
        } catch (IOException e) {
            throw new UncheckedIOException(new IOException("Could not write players", e));
        }
    }

    /** register a new player, or throw if username taken */
    /**
	 * This method registers a new player.
	 * If the username is already taken, it throws an IllegalArgumentException.
	 * @param username - the username of the player
	 * @param password - the password of the player
	 * @return the new player
	 */
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
    
    /**
     * This method updates the player in the list.
     * @param p - the player to update
     */
    public static void updatePlayer(Player p) {
        List<Player> all = loadAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getUsername().equalsIgnoreCase(p.getUsername())) {
                all.set(i, p);
                saveAll(all);
                return;
            }
        }
        // if somehow not found, add it:
        all.add(p);
        saveAll(all);
    }

    /** authenticate, empty if not found or bad password */
    /**
	 * This method authenticates a player.
	 * If the username and password match, it returns the player.
	 * If not, it returns an empty Optional.
	 * @param username - the username of the player
	 * @param password - the password of the player
	 * @return an Optional containing the player if found, or empty if not
	 */
    public static Optional<Player> authenticate(String username, String password) {
        return loadAll().stream()
                .filter(p -> p.getUsername().equalsIgnoreCase(username)
                          && p.getPassword().equals(password))
                .findFirst();
    }
    
    /**
     * This method prints all players to the console.
     */
    public static void printPlayers() {
        List<Player> players = loadAll();
        System.out.println("=== Registered Players ===");
        for (Player p : players) {
            System.out.println("Username: " + p.getUsername() + ", High Score: " + p.getHighScore() + ", total score: " + p.getTotalScore());
        }
        System.out.println("==========================");
    }
}