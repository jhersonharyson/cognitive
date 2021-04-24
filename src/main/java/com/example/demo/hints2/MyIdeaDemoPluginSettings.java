package com.example.demo.hints2;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(name = "MyIdeaDemo", storages = {@Storage("my-idea-demo.xml")})
public class MyIdeaDemoPluginSettings implements PersistentStateComponent<MyIdeaDemoPluginState> {
    MyIdeaDemoPluginState pluginState = new MyIdeaDemoPluginState();

    @Override
    public @Nullable MyIdeaDemoPluginState getState() {
        return this.pluginState;
    }

    @Override
    public void loadState(@NotNull MyIdeaDemoPluginState state) {
        this.pluginState = state;
    }

    public static PersistentStateComponent<MyIdeaDemoPluginState> getInstance(){
        return ServiceManager.getService(MyIdeaDemoPluginSettings.class);
    }

}
