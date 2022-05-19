package ga.justreddy.wiki.rtags.tags;
import ga.justreddy.wiki.rtags.RTags;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class Tag {

    @Getter @Setter private String name;
    @Getter private final String identifier;
    @Getter @Setter private String description = "";
    public Tag(String name, String identifier) {
        this.name = name;
        this.identifier = identifier;
    }

}
