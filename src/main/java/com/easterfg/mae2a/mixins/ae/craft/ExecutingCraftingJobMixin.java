package com.easterfg.mae2a.mixins.ae.craft;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import appeng.api.crafting.IPatternDetails;
import appeng.crafting.execution.ExecutingCraftingJob;

import com.easterfg.mae2a.integration.mixin.ICrafting;

/**
 * @author EasterFG on 2025/4/4
 */
@Mixin(ExecutingCraftingJob.class)
public abstract class ExecutingCraftingJobMixin implements ICrafting {

    @Shadow(remap = false)
    @Final
    Map<IPatternDetails, ?> tasks;

    /**
     * Provides non-standard access to the private {@code value} field of
     * {@code appeng.crafting.execution.ExecutingCraftingJob.TaskProgress} using method handles.
     *
     * <h3>Implementation Strategy</h3>
     * <ul>
     * <li><b>Access Method:</b> MethodHandle-based field accessor</li>
     * <li><b>Lookup Context:</b> Private lookup via {@code MethodHandles.privateLookupIn}</li>
     * <li><b>Initialization:</b> Static block initialization (class loading phase)</li>
     * </ul>
     *
     * <h3>Key Characteristics</h3>
     * <ol>
     * <li>Bypasses normal access control through Java Reflection API</li>
     * <li>MethodHandle provides better performance than traditional reflection</li>
     * <li>Getter operation has JVM-level type safety checks</li>
     * </ol>
     *
     * <h3>Compatibility Risks</h3>
     * <ul>
     * <li>Fragile to class/member rename in future versions</li>
     * <li>Requires module system permissions (Jigsaw/JPMS)</li>
     * <li>May break under SecurityManager restrictions</li>
     * </ul>
     *
     * <h3>Exception Handling</h3> Failures during initialization will throw wrapped {@link RuntimeException} with
     * original exception as cause.
     */
    @Unique
    private static final MethodHandle mae2a_VALUE_GETTER;

    static {
        try {
            Class<?> clazz = Class.forName("appeng.crafting.execution.ExecutingCraftingJob$TaskProgress");
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(clazz, MethodHandles.lookup());
            mae2a_VALUE_GETTER = lookup.findGetter(clazz, "value", long.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean mae2a_hasTask(IPatternDetails details) {
        var task = tasks.get(details);
        if (task != null) {
            try {
                long value = (long) mae2a_VALUE_GETTER.invoke(task);
                return value <= 1;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
