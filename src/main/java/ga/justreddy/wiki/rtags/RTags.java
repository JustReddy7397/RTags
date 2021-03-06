package ga.justreddy.wiki.rtags;

import ga.justreddy.wiki.rtags.commands.TagCommand;
import ga.justreddy.wiki.rtags.database.DatabaseManager;
import ga.justreddy.wiki.rtags.dependency.DLoader;
import ga.justreddy.wiki.rtags.dependency.base.Dependency;
import ga.justreddy.wiki.rtags.events.ChatEvent;
import ga.justreddy.wiki.rtags.events.JoinEvent;
import ga.justreddy.wiki.rtags.menu.MenuEvent;
import ga.justreddy.wiki.rtags.tags.TagManager;
import ga.justreddy.wiki.rtags.utils.Utils;
import lombok.Getter;
import lombok.SneakyThrows;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.function.Consumer;

public final class RTags extends JavaPlugin {

    @Getter
    private static RTags plugin;
    @Getter
    DatabaseManager databaseManager;
    @Getter
    private YamlConfig databaseConfig;
    @Getter
    private YamlConfig messagesConfig;

    @Getter
    private Permission permission = null;

    private static final int DATABASE_VERSION = 1;
    private static final int MESSAGES_VERSION = 4;

    @Override
    public void onLoad() {

        try{
            DLoader.getInstance().onLoad();
            DLoader.getInstance().load(new Dependency("h2", "2.1.212", "com.h2database", "h2"));
            DLoader.getInstance().load(new Dependency("MongoDB-Driver", "3.12.11", "org.mongodb", "mongodb-driver"));
            DLoader.getInstance().load(new Dependency("MongoDB-Core", "3.12.11", "org.mongodb", "mongodb-driver-core"));
            DLoader.getInstance().load(new Dependency("MongoDB-Bson", "3.12.11", "org.mongodb", "bson"));
        }catch (ClassCastException | ExceptionInInitializerError ignored) {

        }



    }

    @SneakyThrows
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        if (!loadConfigs()) return;
        getServer().getConsoleSender().sendMessage(Utils.format("&a=-=-=-=-RTags-=-=-=-="));
        getServer().getConsoleSender().sendMessage(Utils.format("&bMade by: " + getDescription().getAuthors()));
        getServer().getConsoleSender().sendMessage(Utils.format("&bVersion: " + getDescription().getVersion()));
        getServer().getConsoleSender().sendMessage(Utils.format("&bChecking for updates..."));
        checkUpdates();
        setupPermissions();
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
        } else {
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
        try {
            currentlyLoading = "database.yml";
            databaseConfig = new YamlConfig(currentlyLoading);
            if (databaseConfig.isOutdated(DATABASE_VERSION)) {
                Utils.error(null, "Outdated database.yml file.", true);
                return false;
            }
            currentlyLoading = "messages.yml";
            messagesConfig = new YamlConfig(currentlyLoading);
            if (messagesConfig.isOutdated(MESSAGES_VERSION)) {
                Utils.error(null, "Outdated messages.yml file.", true);
                return false;
            }
        } catch (IOException | InvalidConfigurationException ex) {
            Utils.error(ex, "Failed to load config: " + currentlyLoading, true);
            return false;
        }

        return true;
    }

    private void checkUpdates() {
        runAsync(task -> {
            final UpdateChecker checker = new UpdateChecker(102130);
            if (checker.getResult() == UpdateChecker.Result.OUTDATED) {
                Utils.sendConsole(
                        "&2%line%",
                        "&aA newer version of RTags is available!",
                        "&aCurrent version: &r" + getDescription().getVersion(),
                        "&aLatest version: &r" + checker.getLatestVersion(),
                        "&aGet the update: &rhttps://spigotmc.org/resources/102130",
                        "&2%line%");
                return;
            }

            if (checker.getResult() == UpdateChecker.Result.ERROR) {
                Utils.error(null, "Failed to check for updates", false);
            }
        });
    }

    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
    }
    private BukkitRunnable createRunnable(final Consumer<BukkitRunnable> task) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                task.accept(this);
            }
        };
    }

     private BukkitTask runAsync(final Consumer<BukkitRunnable> task) {
        return createRunnable(task).runTaskAsynchronously(this);
    }

}
