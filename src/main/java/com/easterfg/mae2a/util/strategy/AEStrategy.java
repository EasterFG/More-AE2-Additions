package com.easterfg.mae2a.util.strategy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import appeng.api.config.Actionable;
import appeng.api.implementations.blockentities.IWirelessAccessPoint;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;
import appeng.api.util.AEColor;

import com.easterfg.mae2a.util.CableType;

/**
 * @author EasterFG on 2025/4/12
 */
public class AEStrategy implements IStrategy {

    private final Player player;
    private final Level level;
    private final BlockPos bindPos;

    public AEStrategy(Player player, Level level, BlockPos bindPos) {
        this.player = player;
        this.level = level;
        this.bindPos = bindPos;
    }

    private MEStorage getMestorage() {
        if (bindPos == null)
            return null;
        var tile = level.getBlockEntity(bindPos);
        if (tile instanceof IWirelessAccessPoint wireless) {
            var grid = wireless.getGrid();
            if (grid != null) {
                return grid.getStorageService().getInventory();
            }
        }
        return null;
    }

    /**
     * 消耗特定存储中的内容
     *
     * @param expected  需求的数量
     * @param cableType 线缆类型
     * @return 取出数量
     */
    @Override
    public int consume(int expected, CableType cableType) {
        if (expected < 1)
            return 0;
        var storage = getMestorage();
        if (storage == null)
            return 0;
        long fetched = 0;
        for (var entry : storage.getAvailableStacks()) {
            if (!(entry.getKey() instanceof AEItemKey itemKey))
                continue;
            if (!cableType.isValid(itemKey.getItem()))
                continue;
            long extracted = storage.extract(itemKey, (expected - fetched), Actionable.MODULATE,
                    IActionSource.ofPlayer(player));
            fetched += extracted;
            if (fetched >= expected)
                break;
        }
        return (int) fetched;
    }

    @Override
    public int refund(int count, CableType cableType) {
        if (count < 1)
            return 0;
        var storage = getMestorage();
        if (storage == null)
            return 0;
        var extracted = storage.insert(AEItemKey.of(cableType.getItems().item(AEColor.TRANSPARENT)), count,
                Actionable.MODULATE, IActionSource.ofPlayer(player));
        return (int) extracted;
    }
}
