package com.cdd.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(name = "LimitOfComplexityState", storages = {@Storage("limit-of-complexity.xml")})
public class LimitOfComplexitySettings implements PersistentStateComponent<LimitOfComplexityState> {
    LimitOfComplexityState pluginState = new LimitOfComplexityState();

    @Override
    public @Nullable LimitOfComplexityState getState() {
        return this.pluginState;
    }

    @Override
    public void loadState(@NotNull LimitOfComplexityState state) {
        this.pluginState = state;
    }

    public static PersistentStateComponent<LimitOfComplexityState> getInstance() {
        return ServiceManager.getService(LimitOfComplexitySettings.class);
    }

}
