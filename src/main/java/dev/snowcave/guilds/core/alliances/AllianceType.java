package dev.snowcave.guilds.core.alliances;

import net.md_5.bungee.api.ChatColor;

/**
 * Created by WinterBear on 12/11/2023.
 */
public enum AllianceType {

    ///g alliance create Lost Dawn
    //--------------- Guild Alliance Declaration: Lost Dawn -------------
    //Before your Alliance can be formed, you must choose how power will
    //be distributed amongst its members. Click to select a Treaty from
    //the options below.
    //
    //   🟢Democracy🧑‍🤝‍🧑         🟣Hegemony             🔴Empire👑
    //
    //   &a🟢&fDemocracy&a🤝 &5🟣&fHegemony&5🏛 &c🔴&fEmpire&c👑
    //
    //---------------|-------------------------------------|-------------


    DEMOCRACY(ChatColor.of("#80e8a8") + "Democracy", "Senator", "Aide", "the Senate"),
    // Leadership: Alliance Senate - Power is spread equally among all
    //   guilds in your Alliance. Any time a Guild is invited to the
    //   Alliance, its Grandmaster joins the Alliance Senate. Certain
    //   actions can only be done by an Alliance Senate majority vote,
    //   such as:
    //     - Changing the Alliance Treaty
    //     - Kicking a Guild
    //     - Establishing a Capital
    //   It is not possible to disband the Alliance unless you are the last
    //   Guild remaining.
    //
    // Assistants: Aides - Members of the Alliance Senate can promote
    //   members of their Guild to perform Alliance actions.
    //
    // Capital: You will be able to construct a Democratic Capital. You
    //   can choose an existing Guild as your Capital, or establish
    //   a new one independant of the Guilds.

    GRAND_COUNCIL(ChatColor.of("#ffa724") + "Grand Council", "Councillor", "Ambassador", "the Grand Council"),
    // Leadership: Grand Council - Power is kept within a Grand Council
    //   of Guild Masters in your Alliance. Grandmasters become Alliance
    //   Councillors by being promoted by existing members of the Council.
    //   Certain actions can only be done by a Council majority vote:
    //     - Establish Capital
    //     - Change Alliance Treaty
    //     - Kick Councillor's Guild
    //
    // Assistants: Ambassadors - Members of the Grand Council can promote
    //   Alliance members to status of Ambassador to allow them to manage
    //   Alliance resources.
    //
    // Capital: You will be able to construct a Grand Council Center. You
    //   can choose an existing Guild as your Capital, or establish
    //   a new one independant of the Guilds.

    IMPERIAL(ChatColor.of("#bf2e2e") + "Empire", "Emperor", "Vizier", "the Emperor");
    // Leadership: Emperor - Power is granted only to the Grandmaster of
    //   the Founding Guild, who becomes the Emperor. Guild Grandmasters
    //   get no extra rights, and the Alliance Treaty can only be redefined
    //   by the Emperor, or by a Successful Coup.
    //
    // Assistants: Guild members can be promoted to Viziers by the Emperor.
    //   Viziers can manage Alliance affairs but do not hold true power.
    //
    // Capital: You will be able to construct an Imperial Throne. You
    //   can choose an existing Guild as your Capital, or establish
    //   a new one independant of the Guilds.

    private final String displayName;

    private final String leaderTitle;

    private final String assistantTitle;

    private final String leaderGroupName;

    AllianceType(String displayName, String leaderTitle, String assistantTitle, String leaderGroupName){
        this.displayName = displayName;
        this.leaderTitle = leaderTitle;
        this.assistantTitle = assistantTitle;
        this.leaderGroupName = leaderGroupName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLeaderTitle() {
        return leaderTitle;
    }

    public String getAssistantTitle() {
        return assistantTitle;
    }

    public String getLeaderGroupName() {
        return leaderGroupName;
    }
}
