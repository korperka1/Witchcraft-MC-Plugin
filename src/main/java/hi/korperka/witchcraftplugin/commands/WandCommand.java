package hi.korperka.witchcraftplugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class WandCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            ItemStack item = new ItemStack(Material.STICK, 1);
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.displayName(Component.text("Волшебная палочка").color(TextColor.color(255, 255, 0)));
            item.setItemMeta(meta);

            ((Player) sender).getInventory().addItem(item);
        }

        else{
            sender.sendMessage("Команда должна быть использована игроком");
        }

        return true;
    }
}
