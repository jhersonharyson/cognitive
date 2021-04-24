package com.example.demo.listener;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.AsyncFileListener;
import com.intellij.openapi.vfs.VirtualFileManagerListener;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MyListenerClass implements AsyncFileListener {

//    @Override
//    public void beforeRefreshStart(boolean asynchronous) {
//        System.out.println(asynchronous);
//    }
//
//    @Override
//    public void afterRefreshFinish(boolean asynchronous) {
//        System.out.println(asynchronous);
//    }

    public void after(@NotNull List<? extends VFileEvent> events) {
        events.get(0);
    }

    @Override
    public @Nullable ChangeApplier prepareChange(@NotNull List<? extends VFileEvent> events) {
        events.get(0);
        return null;
    }
}
