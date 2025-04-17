package com.easterfg.mae2a.util;

import java.util.*;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import appeng.api.config.Actionable;
import appeng.api.parts.PartHelper;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;
import appeng.api.util.AEColor;
import appeng.core.definitions.AEBlocks;
import appeng.items.parts.ColoredPartItem;

import com.easterfg.mae2a.common.menu.host.CablePlaceToolHost;
import com.easterfg.mae2a.util.strategy.AEStrategy;
import com.easterfg.mae2a.util.strategy.IStrategy;
import com.easterfg.mae2a.util.strategy.InventoryStrategy;

/**
 * @author EasterFG on 2025/4/11
 */
public class CableToolHelper {
    public static void place(Player player, Level level, BlockPos bindPos, BlockPos start, BlockPos end,
            CablePlaceToolHost.Settings settings) {
        Set<BlockPos> path = VectorHelper.generatePath(start, end);
        int size = path.size();
        var item = CableType.values()[settings.getCable()];
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
                result += strategy.consume(size, item);
            }
            available = result;
        }
        for (var pos : path) {
            if (available <= 0) {
                break;
            }
            var part = placeCable(player, level, item.getItems().item(settings.getColor()), pos, settings.isReplace());
            if (part) {
                available--;
            }
        }
        // 退还消耗失败部分
        if (available > 0) {
            for (IStrategy strategy : queues) {
                available -= strategy.refund(available, item);
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
