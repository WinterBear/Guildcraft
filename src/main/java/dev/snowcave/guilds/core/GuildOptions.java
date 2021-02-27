package dev.snowcave.guilds.core;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * Created by WinterBear on 22/12/2020.
 */
public class GuildOptions {

    private boolean explosionsEnabled;

    private boolean alliesCanToggle; //Alliances

    private boolean alliesCanAccessStorage; //Alliances

    private boolean alliesCanBuild; //Alliances

    private boolean alliesCanUseItems; //Alliances

    private GuildSymbol guildSymbol;

    private Double tax;

    private boolean kickMembersWhoDontPayTax;

    private boolean pvpEnabled;

    private String noticeboard;

    private String guildTag;

    private boolean isPublic;

    private String color;

    public GuildOptions() {
        //Default Constructor
    }

    public GuildOptions(Guild guild) {
        this.explosionsEnabled = false;
        this.alliesCanToggle = false;
        this.alliesCanUseItems = false;
        this.alliesCanAccessStorage = false;
        this.alliesCanBuild = false;
        this.guildSymbol = GuildSymbol.DEFAULT;
        this.tax = 0.0;
        this.kickMembersWhoDontPayTax = false;
        this.pvpEnabled = false;
        this.noticeboard = "";
        this.guildTag = generateGuildTag(guild.getGuildName());
        this.isPublic = false;
        this.color = "#5e9cff";
    }

    private String generateGuildTag(String guildName){
        String[] split = guildName.split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            String s = split[i].toUpperCase();
            builder.append(s.charAt(0));
        }
        return builder.toString();
    }

    public boolean isExplosionsEnabled() {
        return explosionsEnabled;
    }

    public boolean isAlliesCanToggle() {
        return alliesCanToggle;
    }

    public boolean isAlliesCanAccessStorage() {
        return alliesCanAccessStorage;
    }

    public boolean isAlliesCanBuild() {
        return alliesCanBuild;
    }

    public boolean isAlliesCanUseItems() {
        return alliesCanUseItems;
    }

    public GuildSymbol getGuildSymbol() {
        return guildSymbol;
    }

    public Double getTax() {
        return tax;
    }

    public boolean isKickMembersWhoDontPayTax() {
        return kickMembersWhoDontPayTax;
    }

    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    public String getNoticeboard() {
        return noticeboard;
    }

    public String getGuildTag() {
        return guildTag;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setExplosionsEnabled(boolean explosionsEnabled) {
        this.explosionsEnabled = explosionsEnabled;
    }

    public void setAlliesCanToggle(boolean alliesCanToggle) {
        this.alliesCanToggle = alliesCanToggle;
    }

    public void setAlliesCanAccessStorage(boolean alliesCanAccessStorage) {
        this.alliesCanAccessStorage = alliesCanAccessStorage;
    }

    public void setAlliesCanBuild(boolean alliesCanBuild) {
        this.alliesCanBuild = alliesCanBuild;
    }

    public void setAlliesCanUseItems(boolean alliesCanUseItems) {
        this.alliesCanUseItems = alliesCanUseItems;
    }

    public void setGuildSymbol(GuildSymbol guildSymbol) {
        this.guildSymbol = guildSymbol;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public void setKickMembersWhoDontPayTax(boolean kickMembersWhoDontPayTax) {
        this.kickMembersWhoDontPayTax = kickMembersWhoDontPayTax;
    }

    public void setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    public void setNoticeboard(String noticeboard) {
        this.noticeboard = noticeboard;
    }

    public void setGuildTag(String guildTag) {
        this.guildTag = guildTag;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
