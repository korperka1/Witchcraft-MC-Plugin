package hi.korperka.witchcraftplugin.listeners;

import hi.korperka.witchcraftplugin.enums.Spells;
import hi.korperka.witchcraftplugin.util.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MagicWandListener implements Listener {
    Inventory spellsInventory = null;
    HashMap<Player, Spells> selectedSpells = new HashMap<>();
    HashMap<Player, Block> lastLumusBlocks = new HashMap<>();
    Set<Block> spikeusBlocks = new HashSet<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getItem() != null && event.getItem().getType() == Material.STICK) {
            Player player = event.getPlayer();
            World world = player.getWorld();

            if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                openSpellsGUI(event.getPlayer());
            }

            else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
                Spells selectedSpell = selectedSpells.get(player);

                if(selectedSpell == null)
                    return;

                switch(selectedSpell)
                {
                    case BOMBARDA: {
                        Block targetBlock = player.getTargetBlock(null, 64);
                        Location targetLocation = targetBlock.getLocation();
                        Utils.spawnParticleBeam(Particle.LAVA, player.getEyeLocation(), targetLocation);
                        world.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);

                        if(targetBlock.getType() != Material.AIR)
                            world.createExplosion(targetLocation, 2);

                        Utils.sendChatMessageFromPlayer(player, Component.text("Бомбарда!")
                                .color(TextColor.color(255, 0, 0)));
                    } break;

                    case SPIKEUS: {
                        Location location = player.getLocation();
                        Block block = location.clone().subtract(0, 1, 0).getBlock();

                        if(block.getType() == Material.AIR || spikeusBlocks.contains(block))
                            return;

                        for(Block b : spikeusBlocks) {
                            b.setType(Material.AIR);
                        }

                        spikeusBlocks.clear();

                        for(int y = 0; y < 10; y++) {
                            Block currentBlock = location.clone().add(0, y, 0).getBlock();
                            currentBlock.setType(Material.ICE);
                            spikeusBlocks.add(currentBlock);
                        }

                        player.teleport(location.add(0, 10, 0));
                        player.playSound(player, Sound.BLOCK_GLASS_BREAK, 1, 1);

                        Utils.sendChatMessageFromPlayer(player, Component.text("Спайкус!")
                                .color(TextColor.color(32, 183, 189)));
                    } break;

                    case BAUBILIUS: {
                        Block targetBlock = player.getTargetBlock(null, 64);
                        if(targetBlock.getType() != Material.AIR) {
                            targetBlock.getWorld().strikeLightning(targetBlock.getLocation());
                            player.playSound(player, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);

                            Utils.sendChatMessageFromPlayer(player, Component.text("Баубилиус!")
                                    .color(TextColor.color(14, 187, 235)));
                        }
                    } break;

                    case AKCIO: {
                        Location eyeLocation = player.getEyeLocation();
                        Vector direction = eyeLocation.getDirection();

                        RayTraceResult rayTrace = world.rayTraceEntities(eyeLocation, direction, 64,
                         entity -> !(entity instanceof Player));

                        if(rayTrace != null) {
                            Entity entity = rayTrace.getHitEntity();
                            if(entity != null) {
                                Utils.pushEntityToPlayer(entity, player);
                                player.playSound(player, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);

                                Utils.sendChatMessageFromPlayer(player, Component.text("Акцио!")
                                        .color(TextColor.color(50, 168, 82)));
                            }
                        }
                    } break;

                    case TRANSGRESSION: {
                        Block targetBlock = player.getTargetBlock(null, 32);
                        Location targetLocation = targetBlock.getLocation();
                        targetLocation.setYaw(player.getLocation().getYaw());
                        targetLocation.setPitch(player.getLocation().getPitch());
                        player.teleport(targetLocation);
                        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, (float)Math.random());
                        Utils.teleportEffect(player);

                        Utils.sendChatMessageFromPlayer(player, Component.text("Трансгрессия!")
                                .color(TextColor.color(63, 12, 201)));
                    } break;

                    case TRANSFIGURATION: {
                        Location eyeLocation = player.getEyeLocation();
                        Vector direction = eyeLocation.getDirection();

                        RayTraceResult rayTrace = world.rayTraceEntities(eyeLocation, direction,
                                64, entity -> !(entity instanceof Player));

                        if(rayTrace != null) {
                            Entity entity = rayTrace.getHitEntity();
                            player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1, 1);
                            world.spawnEntity(entity.getLocation(), Utils.randomEnum(EntityType.class));
                            entity.remove();
                            Utils.sendChatMessageFromPlayer(player, Component.text("Трансфигурация!")
                                    .color(TextColor.color(201, 38, 60)));
                        }
                    } break;

                    case AVADA_KEDAVRA: {
                        Block targetBlock = player.getTargetBlock(null, 64);
                        Location targetLocation = targetBlock.getLocation();
                        Location eyeLocation = player.getEyeLocation();
                        Utils.spawnParticleBeam(Particle.VILLAGER_HAPPY, player.getEyeLocation(), targetLocation);
                        world.playSound(player, Sound.ENTITY_EVOKER_CAST_SPELL, 1, 1);
                        Vector direction = eyeLocation.getDirection();

                        RayTraceResult rayTrace = world.rayTraceEntities(eyeLocation, direction,
                                64, entity -> !(entity instanceof Player));
                        if(rayTrace != null) {
                            Entity entity = rayTrace.getHitEntity();
                            if(entity instanceof Mob)
                                ((Mob) entity).setHealth(0);
                        }

                        Utils.sendChatMessageFromPlayer(player, Component.text("Авада кедавра!")
                                .color(TextColor.color(0, 255, 0)));
                    } break;
                }
            }
        }
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if(spellsInventory != null && event.getInventory() == spellsInventory && event.getCurrentItem() != null){
            event.setCancelled(true);
            Set<NamespacedKey> keys = event.getCurrentItem().getItemMeta().getPersistentDataContainer().getKeys();
            Spells selectedSpell = Spells.NOTHING;
            Player player = (Player) event.getWhoClicked();

            Utils.breakBlockIfAbsent(lastLumusBlocks.get(player));

            for(NamespacedKey key : keys) {
                selectedSpell = switch (key.getKey()) {
                    case "bombardaspell" -> Spells.BOMBARDA;
                    case "spikeusspell" -> Spells.SPIKEUS;
                    case "baubiliusspell" -> Spells.BAUBILIUS;
                    case "akciospell" -> Spells.AKCIO;
                    case "lumusspell" -> Spells.LUMUS;
                    case "transgressionspell" -> Spells.TRANSGRESSION;
                    case "transfigurationspell" -> Spells.TRANSFIGURATION;
                    case "avadakedavraspell" -> Spells.AVADA_KEDAVRA;
                    default -> selectedSpell;
                };
            }

            selectedSpells.put(player, selectedSpell);
        }
    }

    private void openSpellsGUI(Player player) {
        Inventory inv = Bukkit.createInventory(player, 9,
                Component.text("Заклинания")
                        .color(TextColor.color(0, 255, 0)));

        spellsInventory = inv;

        ItemStack bombardaSpell = createButtonItem(Material.TNT, Component.text("Бомбарда")
                .color(TextColor.color(255, 0, 0)),
                Component.text("Создаёт взрыв на месте попадания")
                .color(TextColor.color(255, 255, 0)), "bombardaspell");

        ItemStack avadakedavraSpell = createButtonItem(Material.SKELETON_SKULL, Component.text("Авада кедавра")
                .color(TextColor.color(0, 255, 0)), Component.text("Убивает любого моба")
                .color(TextColor.color(255, 255, 0)), "avadakedavraspell");

        ItemStack baubiliusSpell = createButtonItem(Material.LIGHTNING_ROD, Component.text("Баубилиус")
                .color(TextColor.color(14, 187, 235)), Component.text("Призывает молнию")
                .color(TextColor.color(255, 255, 0)), "baubiliusspell");

        ItemStack transgressionSpell = createButtonItem(Material.ENDER_PEARL, Component.text("Трансгрессия")
                .color(TextColor.color(63, 12, 201)), Component.text("Телепортирует туда, куда смотрит волшебник")
                .color(TextColor.color(255, 255, 0)), "transgressionspell");

        ItemStack transfigurationSpell = createButtonItem(Material.GOLD_INGOT, Component.text("Трансфигурация")
                .color(TextColor.color(201, 38, 60)), Component.text("Превращает существо в другое случайное существо")
                .color(TextColor.color(255, 255, 0)), "transfigurationspell");

        ItemStack akcioSpell = createButtonItem(Material.WATER_BUCKET, Component.text("Акцио")
                .color(TextColor.color(50, 168, 82)), Component.text("Притягивает предмет или существо")
                .color(TextColor.color(255, 255, 0)), "akciospell");

        ItemStack lumusSpell = createButtonItem(Material.LIGHT, Component.text("Люмус")
                .color(TextColor.color(255, 255, 0)), Component.text("Волшебная палочка светится")
                .color(TextColor.color(255, 255, 0)), "lumusspell");

        ItemStack spikeusSpell = createButtonItem(Material.ICE, Component.text("Спайкус")
                .color(TextColor.color(32, 183, 189)), Component.text("Создаётся ледяная колонна, поддерживающая волшебника")
                .color(TextColor.color(255, 255, 0)), "spikeusspell");

        inv.setItem(0, bombardaSpell);
        inv.setItem(1, spikeusSpell);
        inv.setItem(2, baubiliusSpell);
        inv.setItem(3, akcioSpell);
        inv.setItem(4, lumusSpell);
        inv.setItem(5, transgressionSpell);
        inv.setItem(6, transfigurationSpell);
        inv.setItem(7, avadakedavraSpell);

        player.openInventory(inv);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();

        if(selectedSpells.get(player) == Spells.LUMUS && player.getInventory().getItemInMainHand().getType() ==
                Material.STICK) {

            Utils.breakBlockIfAbsent(lastLumusBlocks.get(player));

            Block block = event.getPlayer().getLocation().add(0, 1, 0).getBlock();

            if (block.getType() == Material.AIR)
                block.setType(Material.LIGHT);

            player.playSound(player, Sound.BLOCK_BEACON_AMBIENT, 0.1f, 1);

            lastLumusBlocks.put(player, block);
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItem(event.getNewSlot());
        if(heldItem == null || heldItem.getType() != Material.STICK)
            Utils.breakBlockIfAbsent(lastLumusBlocks.get(event.getPlayer()));
    }

    private ItemStack createButtonItem(Material itemMaterial, Component displayName,
                                       Component lore, String tag){
        ItemStack item = new ItemStack(itemMaterial, 1);
        item.editMeta(meta ->{
           meta.displayName(displayName);
           meta.lore(new ArrayList<>(){{add(lore);}});
           NamespacedKey key = new NamespacedKey("witchcraft", tag);
           meta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        });

        return item;
    }
}
