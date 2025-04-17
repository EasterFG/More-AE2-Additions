package com.easterfg.mae2a.util.strategy;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import appeng.api.util.AEColor;

import com.easterfg.mae2a.util.CableType;

/**
 * @author EasterFG on 2025/4/12
 */
public class InventoryStrategy implements IStrategy {

    private final Inventory inventory;
    private final Player player;

    public InventoryStrategy(@NotNull Player player, @NotNull Inventory inventory) {
        this.player = player;
        this.inventory = inventory;
    }

    @Override
    public int consume(int expected, CableType cableType) {
        if (expected < 1)
            return 0;
        int fetched = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            var stack = inventory.getItem(i);
            if (stack.isEmpty())
                continue;
            if (cableType.isValid(stack.getItem())) {
                int extracted = Math.min(expected - fetched, stack.getCount());
                stack.shrink(extracted);
                fetched += extracted;
                if (fetched >= expected)
                    break;
            }
        }
        return fetched;
    }

    @Override
    public int refund(int count, CableType cableType) {
        if (count < 1)
            return 0;
        int index = (int) Math.ceil(count / 64D);
        var item = cableType.getItems().item(AEColor.TRANSPARENT);
        int remaining = count;
        for (; index > 0; index--) {
            int current = index == 1 ? count % 64 : 64;
            ItemStack stack = new ItemStack(item, current);
            if (!inventory.add(stack))
                break;
            remaining -= current;
        }
        if (remaining > 0) {
            Level level = player.getCommandSenderWorld();
            var entity = new ItemEntity(level, player.getX(), player.getY() + 0.5, player.getZ(),
                    new ItemStack(item, remaining));
            level.addFreshEntity(entity);
        }
        return 0;
    }
}
