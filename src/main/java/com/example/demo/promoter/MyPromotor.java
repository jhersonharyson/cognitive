package com.example.demo.promoter;

import com.intellij.openapi.actionSystem.ActionPromoter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class MyPromotor {}

//
//        implements ActionPromoter {
//
//    @Override
//    public List<AnAction> promote(@NotNull List<? extends AnAction> actions, @NotNull DataContext context) {
//        AnAction action = ContainerUtil.findInstance(actions, MyChangeFileAction.class);
//        return action != null ? Collections.singletonList(action) : Collections.emptyList();
//    }
//

//}
