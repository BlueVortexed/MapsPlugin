package com.oresomecraft.maps.battles.maps;

import com.oresomecraft.OresomeBattles.api.BattlePlayer;
import com.oresomecraft.OresomeBattles.api.Gamemode;
import com.oresomecraft.maps.MapConfig;
import com.oresomecraft.maps.battles.BattleMap;
import com.oresomecraft.maps.battles.IBattleMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

@MapConfig
public class Sandbox extends BattleMap implements IBattleMap, Listener {

    public Sandbox() {
        super.initiate(this, name, fullName, creators, modes);
        setAllowBuild(false);
        disableDrops(new Material[]{});
    }

    String name = "sandbox";
    String fullName = "Sandbox";
    String creators = "_Moist, Stewpetasuarus, 123Oblivious and WiiiFreak123 ";
    Gamemode[] modes = {Gamemode.INFECTION, Gamemode.LMS};

    public void readyTDMSpawns() {
        redSpawns.add(new Location(w, 50, 75, -23, 128, 0));
        blueSpawns.add(new Location(w, -7, 65, 14, -180, 0));
    }

    public void readyFFASpawns() {
        FFASpawns.add(new Location(w, 50, 75, -23, 128, 0));
        FFASpawns.add(new Location(w, -7, 65, 14, -180, 0));
        FFASpawns.add(new Location(w, -3, 67, -33, -0, 0));
        FFASpawns.add(new Location(w, 19, 87, 9, 178, 0));
        FFASpawns.add(new Location(w, 20, 69, 9, 126, 0));
        FFASpawns.add(new Location(w, -17, 65, -2, -90, 0));
        FFASpawns.add(new Location(w, 2.1, 62, 28, 180, 0));
        FFASpawns.add(new Location(w, 50, 67, -10, 180, 0));
    }

    public void applyInventory(final BattlePlayer p) {
        Inventory i = p.getInventory();

        ItemStack HEALTH = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemStack CARROT = new ItemStack(Material.CARROT_ITEM, 3);
        ItemStack BOW = new ItemStack(Material.BOW, 1);
        ItemStack ARROWS = new ItemStack(Material.ARROW, 8);
        ItemStack IRON_BOOTS = new ItemStack(Material.IRON_BOOTS, 1);
        ItemStack WOODEN_SWORD = new ItemStack(Material.WOOD_SWORD, 1);
        ItemStack FISHING_ROD = new ItemStack(Material.FISHING_ROD, 1);

        IRON_BOOTS.addEnchantment(Enchantment.PROTECTION_FALL, 2);
        WOODEN_SWORD.addEnchantment(Enchantment.DURABILITY, 5);

        p.getInventory().setBoots(IRON_BOOTS);

        i.setItem(0, WOODEN_SWORD);
        i.setItem(1, BOW);
        i.setItem(2, FISHING_ROD);
        i.setItem(3, CARROT);
        i.setItem(4, HEALTH);
        i.setItem(17, ARROWS);
    }

    // Region. (Top corner block and bottom corner block.
    // Top left corner.
    public int x1 = 65;
    public int y1 = 124;
    public int z1 = -64;
    //Bottom right corner.
    public int x2 = -66;
    public int y2 = 35;
    public int z2 = 58;

    @EventHandler(priority = EventPriority.NORMAL)
    public void fishing(PlayerFishEvent event) {
        PlayerFishEvent.State state = event.getState();
        Player p = event.getPlayer();
        ItemStack is = p.getItemInHand();
        Material mat = is.getType();
        Location loc = p.getLocation();

        if (loc.getWorld().getName().equals(name)) {

            if (mat == Material.FISHING_ROD) {

                if (state == PlayerFishEvent.State.IN_GROUND || state == PlayerFishEvent.State.FAILED_ATTEMPT) {
                    p.launchProjectile(Snowball.class);

                }
            }
        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void grapple(ProjectileHitEvent event) {
        Entity proj = event.getEntity();
        Location hit = proj.getLocation();

        if (contains(hit, x1, x2, y1, y2, z1, z2)) {

            if (proj instanceof Snowball) {
                Snowball fish = (Snowball) proj;
                Entity shooter = fish.getShooter();

                if (shooter instanceof Player) {
                    Player p = (Player) shooter;
                    Location loc = p.getLocation();
                    ItemStack is = p.getItemInHand();
                    Material mat = is.getType();

                    if (mat == Material.FISHING_ROD) {

                        p.setFallDistance(0);
                        p.playSound(loc, Sound.ARROW_HIT, 1, 1);

                        int hitx = hit.getBlockX();
                        int hity = hit.getBlockY();
                        int hitz = hit.getBlockZ();
                        int locx = loc.getBlockX();
                        int locy = loc.getBlockY();
                        int locz = loc.getBlockZ();
                        double co[] = new double[3];

                        if (hitx > locx) {
                            co[0] = 1.2;
                        } else if (hitx < locx) {
                            co[0] = -1.2;
                        } else if (hitx == locx) {
                            co[0] = 0;
                        }

                        if (hity > locy) {
                            co[1] = 1.4;
                        } else if (hity < locy) {
                            co[1] = -0.8;
                        } else if (hity == locy) {
                            co[1] = 0;
                        }

                        if (hitz > locz) {
                            co[2] = 1.2;
                        } else if (hitz < locz) {
                            co[2] = -1.2;
                        } else if (hitz == locz) {
                            co[2] = 0;
                        }

                        p.setVelocity(new Vector(co[0], co[1], co[2]));

                    }
                }
            }
        }
    }

}