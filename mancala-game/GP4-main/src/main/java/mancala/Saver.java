package mancala;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.*;

public class Saver {

    public static void saveObject(final Serializable toSave, final String filename) throws IOException {
        final Path currentDirectory = Paths.get(System.getProperty("user.dir"));
        final String folderName = "assets";
        final Path assetsFolderPath = currentDirectory.resolve(folderName);

        // Check if the 'assets' folder exists; if not, create it
        if (Files.notExists(assetsFolderPath)) {
            Files.createDirectories(assetsFolderPath);
        }

        final Path fileToWrite = assetsFolderPath.resolve(filename);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileToWrite.toFile()))) {
            out.writeObject(toSave);
        }
    }

    public static Serializable loadObject(final String filename) throws IOException {
        final Path currentDirectory = Paths.get(System.getProperty("user.dir"));
        final Path assetsFolderPath = currentDirectory.resolve("assets");
        final Path fileToRead = assetsFolderPath.resolve(filename);

        if (Files.notExists(fileToRead)) {
            throw new FileNotFoundException("The file " + filename + " does not exist.");
        }

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileToRead.toFile()))) {
            return (Serializable) input.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Class not found when attempting to deserialize " + filename, e);
        }
    }

   public static void saveUserProfile(final UserProfile profile, final String filename) throws IOException {
        saveObject(profile, filename);
    }

    public static UserProfile loadUserProfile(final String filename) throws IOException, ClassNotFoundException {
        final Serializable loadedObject = loadObject(filename);
        if (loadedObject instanceof UserProfile) {
            return (UserProfile) loadedObject;
        } else {
            throw new IOException("Loaded object is not a UserProfile.");
        }
    }
}
