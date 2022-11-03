package com.example.demo.utils;


import com.cdd.integrations.code.smell.jdeodorant.core.distance.ProjectInfo;
import com.cdd.integrations.code.smell.jdeodorant.ide.ui.AbstractRefactoringPanel;
import com.cdd.integrations.code.smell.jdeodorant.utils.PsiUtils;
import com.cdd.service.AnalyzerService;
import com.cdd.service.ClientNotificationService;
import com.cdd.utils.Debouncer;
import com.intellij.ide.DataManager;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.impl.DummyProject;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.file.impl.FileManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class RealtimeState {
    private static final NotificationGroup STICKY_GROUP =
            new NotificationGroup("demo.notifications.stickyBalloon",
                    NotificationDisplayType.STICKY_BALLOON, true);
    private static final String UPDATE_STATE_BY_FILE = "update.state.by.file";

    private static RealtimeState realtimeState;
    private RealtimeLambdaState listener;
    private VirtualFile file;

    private int currentComplexity = 0;
    private int lastComplexity = -1;
    private int limitOfComplexity = 0;
    private boolean showComplexityInTheCode = true;
    private boolean limitExceededNotification = true;

    private Debouncer<String> debouncer;
    private int DEBOUNCE_INTERVAL = 3_000;

    private RealtimeState() {
        debouncer = updateStateByFileDebouncer();
        ProjectManager.getInstance().getDefaultProject().getMessageBus().connect().subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
    @Override
    public void after(@NotNull List<? extends VFileEvent> events) {
            events.get(0);
            }
        });
    }

    public static RealtimeState getInstance() {
        if (realtimeState == null) {
            realtimeState = new RealtimeState();
        }
        return realtimeState;
    }

    public void setListener(RealtimeLambdaState listener) {
        this.listener = listener;
    }

    public void setCurrentComplexity(int currentComplexity) {
        this.currentComplexity = currentComplexity;
        if (this.currentComplexity != this.lastComplexity) {
            this.update(currentComplexity, this.limitOfComplexity, this.file);
            this.verifyLimitExceeding();
        }
        this.lastComplexity = this.currentComplexity;
    }

    public void setFile(VirtualFile file) {
        this.file = file;
        this.update(this.currentComplexity, this.limitOfComplexity, file);
    }

    public void updateState(int currentComplexity, int limitOfComplexity, VirtualFile file) {
        this.setCurrentComplexity(currentComplexity);
        this.setLimitOfComplexity(limitOfComplexity);
        this.setFile(file);
    }

    public void updateStateByFile(VirtualFile file) {
        setFile(file);
        setCurrentComplexity(0);
        ApplicationManager.getApplication().runReadAction(() -> debouncer.call(UPDATE_STATE_BY_FILE+"___"+file.toString()));
    }

    private Debouncer<String> updateStateByFileDebouncer() {
        return new Debouncer<>(d -> {

            ApplicationManager.getApplication().invokeLater(() -> {
//                ProjectManager.getInstance().getOpenProjects()[0];

                try {
                    DataContext dataContext = DataManager.getInstance().getDataContext();
                    Project project = (Project) dataContext.getData(DataConstants.PROJECT);
                    var psiFile = PsiManager.getInstance(project).findFile(VirtualFileManager.getInstance().findFileByUrl(d.split("___")[1]));

                    var complexityCounter = new AnalyzerService().readPsiFile(psiFile);
                    var currentComplexity = complexityCounter.compute();
                    var limitOfComplexity = complexityCounter.getMetrics().getLimitOfComplexity();
                    this.updateState(currentComplexity, limitOfComplexity, file);
                } catch (ResourceNotFoundException ignored) {
                    log.info("resource not found");
                } catch (Exception e){
                    log.info("error 1", e);
                }
            });
        }, DEBOUNCE_INTERVAL);
    }

    public int getCurrentComplexity() {
        return currentComplexity;
    }

    public VirtualFile getVirtualFile() {
        return this.file;
    }

    private void update(int currentComplexity, int limitOfComplexity, VirtualFile file) {
        if (this.listener != null) {
            this.listener.applyCurrentClass(file);
            this.listener.applyCurrentComplexity(currentComplexity);
            this.listener.applyLimitOfComplexity(limitOfComplexity);
        }
    }

    public int getLimitOfComplexity() {
        return limitOfComplexity;
    }

    public void setLimitOfComplexity(int limitOfComplexity) {
        this.limitOfComplexity = limitOfComplexity;
        this.update(currentComplexity, limitOfComplexity, this.file);
    }

    private void verifyLimitExceeding() {
        if (this.limitOfComplexity > 0 && this.currentComplexity >= this.limitOfComplexity) {
            ClientNotificationService.getInstance().notify(this.currentComplexity, this.limitOfComplexity);
        }
    }

    public boolean isShowComplexityInTheCode() {
        return showComplexityInTheCode;
    }

    public void setShowComplexityInTheCode(boolean showComplexityInTheCode) {
        this.showComplexityInTheCode = showComplexityInTheCode;
        Arrays.stream(FileEditorManager.getInstance(ProjectManager.getInstance().getOpenProjects()[0]).getSelectedFiles()).forEach( f -> {
            FileDocumentManager.getInstance().reloadFiles(f);
        });
    }

    public boolean isLimitExceededNotification() {
        return limitExceededNotification;
    }

    public void setLimitExceededNotification(boolean limitExceededNotification) {
        this.limitExceededNotification = limitExceededNotification;
    }
}
