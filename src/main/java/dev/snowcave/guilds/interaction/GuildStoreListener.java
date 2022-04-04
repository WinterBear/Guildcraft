package dev.snowcave.guilds.interaction;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.users.User;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by WinterBear on 07/04/2021.
 */
public class GuildStoreListener implements Listener {

    public static final List<UUID> OPEN_STORES = new ArrayList<>();

    private static final Map<Material, Integer> FOOD = foodValues();

    private static Map<Material, Integer> foodValues(){
        Map<Material, Integer> values = new HashMap<>();

        values.put(Material.APPLE, 4);
        values.put(Material.BAKED_POTATO, 5);
        values.put(Material.BEETROOT, 1);
        values.put(Material.BEETROOT_SOUP, 6);
        values.put(Material.BREAD, 5);
        values.put(Material.CAKE, 14);
        values.put(Material.CARROT, 3);
        values.put(Material.CHORUS_FRUIT, 4);
        values.put(Material.COOKED_CHICKEN, 6);
        values.put(Material.COOKED_COD, 5);
        values.put(Material.COOKED_MUTTON, 6);
        values.put(Material.COOKED_PORKCHOP, 8);
        values.put(Material.COOKED_RABBIT, 5);
        values.put(Material.COOKED_SALMON, 6);
        values.put(Material.COOKIE, 2);
        values.put(Material.ENCHANTED_GOLDEN_APPLE, 50);
        values.put(Material.GOLDEN_APPLE, 10);
        values.put(Material.GOLDEN_CARROT, 6);
        values.put(Material.HONEY_BOTTLE, 6);
        values.put(Material.MELON_SLICE, 2);
        values.put(Material.MUSHROOM_STEW, 6);
        values.put(Material.POTATO, 1);
        values.put(Material.PUMPKIN_PIE, 8);
        values.put(Material.RABBIT_STEW, 10);
        values.put(Material.COOKED_BEEF, 8);
        values.put(Material.SWEET_BERRIES, 2);
        values.put(Material.TROPICAL_FISH, 1);

        return values;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        if(event.getPlayer() instanceof Player && OPEN_STORES.contains(event.getPlayer().getUniqueId())) {
            Optional<User> user = Guilds.getUser((Player) event.getPlayer());
            int depositedFood = 0;
            for(ItemStack food : event.getInventory().getContents()){
                if(food != null) {
                    int amount = food.getAmount();
                    if (FOOD.containsKey(food.getType())) {
                        depositedFood += amount * FOOD.get(food.getType());
                    } else {
                        event.getPlayer().getInventory().addItem(food);
                    }
                }
            }
            user.get().getGuild().getGuildHall().addFood(depositedFood);
            ChatUtils.send((Player) event.getPlayer(), "&7Deposited &a+" + depositedFood + " &7food into the guild stores. &e(&6Total&8: &2" + user.get().getGuild().getGuildHall().getFoodStore() + "&e)");
            OPEN_STORES.remove(event.getPlayer().getUniqueId());
        }
    }


}
