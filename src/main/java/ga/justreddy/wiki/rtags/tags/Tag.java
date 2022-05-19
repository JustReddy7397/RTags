package ga.justreddy.wiki.rtags.tags;

import lombok.Getter;
import lombok.Setter;

public class Tag {

    @Getter @Setter private String name;
    @Getter private final String identifier;
    @Getter @Setter private String description = "";
    @Getter private final String permission;
    public Tag(String name, String identifier) {
        this.name = name;
        this.identifier = identifier;
        this.permission = "rtags.tag." + identifier;
    }

}
