package com.easterfg.mae2a.util;

import java.util.*;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import appeng.api.config.Actionable;
import appeng.api.parts.PartHelper;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;
import appeng.api.util.AEColor;
import appeng.core.definitions.AEBlocks;
import appeng.items.parts.ColoredPartItem;

import com.easterfg.mae2a.common.items.ItemCablePlaceTool;
import com.easterfg.mae2a.common.menu.host.CablePlaceToolHost;
import com.easterfg.mae2a.util.strategy.AEStrategy;
import com.easterfg.mae2a.util.strategy.IStrategy;
import com.easterfg.mae2a.util.strategy.InventoryStrategy;

/**
 * @author EasterFG on 2025/4/11
 */
public class CableToolHelper {
    public static void place(ItemStack stack, Player player, ServerLevel level, GlobalPos bindPos,
            List<BlockPos> nodeList,
            CablePlaceToolHost.Settings settings) {
        if (!(stack.getItem() instanceof ItemCablePlaceTool placeTool)) {
            return;
        }
        Set<BlockPos> path = VectorHelper.generatePath(nodeList);
        int size = path.size();
        var cable = CableType.values()[settings.getCable()];
        // consume power
        double power;
        if (settings.getColor() == AEColor.TRANSPARENT) {
            power = size * 1D;
        } else {
            power = size * 4D;
        }
        if (placeTool.extractAEPower(stack, power, Actionable.SIMULATE) < power) {
            return;
        }
        placeTool.extractAEPower(stack, power, Actionable.MODULATE);
        int available;
        List<IStrategy> queues = new LinkedList<>();
        queues.add(new AEStrategy(player, level, bindPos));
        queues.add(new InventoryStrategy(player, player.getInventory()));
        if (player.isCreative()) {
            available = size;
        } else {
            var result = 0;
            for (IStrategy strategy : queues) {
                if (result >= size)
                    break;
                result += strategy.consume(size, cable);
            }
            available = result;
        }
        for (var pos : path) {
            if (available <= 0) {
                break;
            }
            var part = placeCable(player, level, cable.getItems().item(settings.getColor()), pos, settings.isReplace());
            if (part) {
                available--;
            }
        }
        // refund
        if (available > 0) {
            for (IStrategy strategy : queues) {
                available -= strategy.refund(available, cable);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean placeCable(Player player, Level level, ColoredPartItem<?> cable, BlockPos pos,
            boolean replace) {
        var host = PartHelper.getOrPlacePartHost(level, pos, replace, player);
        if (host == null) {
            return false;
        }

        var addPart = host.addPart(cable, null, player);
        if (addPart == null) {
            if (host.isEmpty()) {
                host.cleanup();
            }
            return false;
        }

        var ss = AEBlocks.CABLE_BUS.block().getSoundType(AEBlocks.CABLE_BUS.block().defaultBlockState());
        level.playSound(null, pos, ss.getPlaceSound(), SoundSource.BLOCKS, (ss.getVolume() + 1.0F) / 2.0F,
                ss.getPitch() * 0.8F);
        return true;
    }

    public static Map<CableType, Long> getCableCount(MEStorage storage) {
        Map<CableType, Long> result = new HashMap<>();
        for (CableType cable : CableType.values()) {
            var value = result.getOrDefault(cable, 0L);
            for (AEColor color : AEColor.values()) {
                var itemKey = AEItemKey.of(cable.getItems().item(color));
                value += storage.extract(itemKey, Long.MAX_VALUE, Actionable.SIMULATE, null);
                result.put(cable, value);
            }
        }
        return result;
    }
}
