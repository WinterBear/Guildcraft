package dev.snowcave.guilds.commands.alliances;

/**
 * Created by WinterBear on 12/11/2023.
 */
public class AllianceInvite {

    private String alliance;

    private String guild;

    public AllianceInvite(String alliance, String guild) {
        this.alliance = alliance;
        this.guild = guild;
    }

    public String getAlliance() {
        return alliance;
    }

    public void setAlliance(String alliance) {
        this.alliance = alliance;
    }

    public String getGuild() {
        return guild;
    }

    public void setGuild(String guild) {
        this.guild = guild;
    }
}
