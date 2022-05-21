package ga.justreddy.wiki.rtags;

import dev.vaziak.mavendd.MavenDownloader;
import dev.vaziak.mavendd.ParsedPom;
import ga.justreddy.wiki.rtags.commands.TagCommand;
import ga.justreddy.wiki.rtags.database.DatabaseManager;
import ga.justreddy.wiki.rtags.events.ChatEvent;
import ga.justreddy.wiki.rtags.events.JoinEvent;
import ga.justreddy.wiki.rtags.menu.MenuEvent;
import ga.justreddy.wiki.rtags.tags.TagData;
import ga.justreddy.wiki.rtags.tags.TagManager;
import ga.justreddy.wiki.rtags.utils.Utils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.DriverManager;
import java.util.logging.Level;

public final class RTags extends JavaPlugin {

    @Getter private static RTags plugin;
    @Getter DatabaseManager databaseManager;
    @Getter private YamlConfig databaseConfig;
    @Getter private YamlConfig messagesConfig;

    private static final int DATABASE_VERSION = 1;
    private static final int MESSAGES_VERSION = 1;

    @Override
    public void onLoad() {
        MavenDownloader.MavenDownloaderBuilder downloader = MavenDownloader.of();
        ParsedPom parsedPom = new ParsedPom();
        parsedPom.addDependency(new ParsedPom.Dependency("com.h2database", "h2", "2.1.212"));
        parsedPom.addDependency(new ParsedPom.Dependency("org.mongodb", "mongodb-driver", "3.12.11"));
        parsedPom.addDependency(new ParsedPom.Dependency("org.mongodb", "mongodb-driver-core", "3.12.11"));
        parsedPom.addDependency(new ParsedPom.Dependency("org.mongodb", "bson", "3.12.11"));
        downloader.parsedPom(parsedPom);
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        if(!loadConfigs()) return;
        getServer().getConsoleSender().sendMessage(Utils.format("&a=-=-=-=-RTags-=-=-=-="));
        getServer().getConsoleSender().sendMessage(Utils.format("&bMade by: " + getDescription().getAuthors()));
        getServer().getConsoleSender().sendMessage(Utils.format("&bVersion: " + getDescription().getVersion()));
        getServer().getConsoleSender().sendMessage(Utils.format("&bChecking for updates..."));
/*        new VersionCheckerTask(this, id).getVersion(v -> {
            if(getDescription().getVersion().equalsIgnoreCase(v)){
                getServer().getConsoleSender().sendMessage(Utils.c("&aLooks like there isn't a new update available!"));
            }else{
                getServer().getConsoleSender().sendMessage(Utils.c("&cLooks like there is a new update available!"));
                getServer().getConsoleSender().sendMessage(Utils.c("&Link: https://www.spigotmc.org/resources/teleportbow.79733/"));
            }
        });*/
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getServer().getConsoleSender().sendMessage(Utils.format("&aPlaceholderAPI found! Using it!"));
            new Placeholders().register();
        }else{
            getServer().getConsoleSender().sendMessage(Utils.format("&cPlaceholderAPI not found! Not using it!"));
        }
        getServer().getConsoleSender().sendMessage(Utils.format("&a=-=-=-=-RTags-=-=-=-="));


        switch (getDatabaseConfig().getConfig().getString("storage").toLowerCase()) {
            case "mongodb":
                databaseManager = new DatabaseManager();
                databaseManager.connectMongoDB(getDatabaseConfig().getConfig().getString("mongodb.uri"), "rtags");
                break;
            case "sql":
                databaseManager = new DatabaseManager();
                databaseManager.connectH2(this, "data/database");
                break;
            case "mysql":
                databaseManager = new DatabaseManager();
                databaseManager.connectMySQL(
                        getDatabaseConfig().getConfig().getString("mysql.database"),
                        getDatabaseConfig().getConfig().getString("mysql.user"),
                        getDatabaseConfig().getConfig().getString("mysql.password"),
                        getDatabaseConfig().getConfig().getString("mysql.host"),
                        getDatabaseConfig().getConfig().getInt("mysql.port")
                );
                break;
        }
        getServer().getPluginManager().registerEvents(new MenuEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getCommand("rtags").setExecutor(new TagCommand());
        Bukkit.getScheduler().runTaskLater(this, () -> {
            TagManager.getTagManager().loadTags();
        }, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        TagManager.getTagManager().deleteTags();
    }

    private boolean loadConfigs() {
        String currentlyLoading = "configuration file";
        try{
            currentlyLoading = "database.yml";
            databaseConfig = new YamlConfig(currentlyLoading);
            if(databaseConfig.isOutdated(DATABASE_VERSION)) {
                Utils.error(null, "Outdated database.yml file.", true);
                return false;
            }
            currentlyLoading = "messages.yml";
            messagesConfig = new YamlConfig(currentlyLoading);
            if(messagesConfig.isOutdated(MESSAGES_VERSION)) {
                Utils.error(null, "Outdated messages.yml file.", true);
                return false;
            }
        }catch (IOException | InvalidConfigurationException ex) {
            Utils.error(ex, "Failed to load config: " + currentlyLoading, true);
            return false;
        }

        return true;
    }
}
