package ga.justreddy.wiki.rtags;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import lombok.Getter;
public class UpdateChecker {

    @Getter private final int resourceId;
    @Getter private final String latestVersion;

    /**
     * Creates a new update checker instance and caches the latest version from Spigot. Should be
     * called asynchronously.
     *
     * @param resourceId The resource ID on SpigotMC
     */
    public UpdateChecker(final int resourceId) {
        this.resourceId = resourceId;
        this.latestVersion = retrieveVersionFromSpigot();
    }

    /**
     * Checks if this plugin version is the same as the one on Spigot.
     *
     * @return The update check result
     */
    public UpdateChecker.Result getResult() {

        if (latestVersion == null) {
            return UpdateChecker.Result.ERROR;
        }

        if (RTags.getPlugin().getDescription().getVersion().equals(latestVersion)) {
            return UpdateChecker.Result.UP_TO_DATE;
        }

        return UpdateChecker.Result.OUTDATED;
    }

    private String retrieveVersionFromSpigot() {

        try (final InputStream inputStream = new URL(
                "https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream();
             final Scanner scanner = new Scanner(inputStream)) {

            if (scanner.hasNext()) {
                return scanner.next();
            }

        } catch (final IOException ex) {
            return null;
        }

        return null;
    }

    /**
     * The result of an update check.
     */
    public enum Result {
        /**
         * The plugin version matches the one on the resource.
         */
        UP_TO_DATE,
        /**
         * The plugin version does not match the one on the resource.
         */
        OUTDATED,
        /**
         * There was an error whilst checking for updates.
         */
        ERROR
    }

}
