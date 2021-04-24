package com.example.demo;

import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MyIdeaPopupList extends BaseListPopupStep<String> {
    String title;
    List<String> list;


    public MyIdeaPopupList(String title, List<String> list) {
        super(title, list);
        this.title = title;
        this.list = list;
    }

    @Override
    public boolean isSelectable(String value) {
        return true;
    }

    @Override
    public @Nullable PopupStep<?> onChosen(String selectedValue, boolean finalChoice) {
        return super.onChosen(selectedValue, finalChoice);
    }


}
