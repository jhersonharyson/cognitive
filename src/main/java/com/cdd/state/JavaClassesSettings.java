package com.cdd.state;

import com.example.demo.state.MyIdeaDemoPluginState;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(name = "JavaQualifiersState", storages = {@Storage("java-qualifiers-state.xml")})
public class JavaClassesSettings implements PersistentStateComponent<JavaClassesState> {
    JavaClassesState pluginState = new JavaClassesState();

    @Override
    public @Nullable JavaClassesState getState() {
        return this.pluginState;
    }

    @Override
    public void loadState(@NotNull JavaClassesState state) {
        this.pluginState = state;
    }

    public static PersistentStateComponent<JavaClassesState> getInstance() {
        return ServiceManager.getService(JavaClassesSettings.class);
    }

}
