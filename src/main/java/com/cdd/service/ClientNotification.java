package com.cdd.service;

import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class ClientNotification {
    private static ClientNotification instance;
    private Notification notification;
    public Project project;
    public static NotificationGroup notificationGroup = new NotificationGroup("NotificationClient.Notify", NotificationDisplayType.BALLOON, false);
    public static NotificationGroup balloonGroup = new NotificationGroup("NotificationClient.Balloon", NotificationDisplayType.STICKY_BALLOON, false);

    private ClientNotification() {
    }

    public static ClientNotification getInstance() {
        if (instance == null) {
            instance = new ClientNotification();
        }
        return instance;
    }

    public void notify(int currentComplexity, int limitOfComplexity) {
        if (this.notification != null)
            this.notification.expire();
        // TODO: replace for clear()
        this.notification = new Notification("Plugins Suggestion", "Limit exceeded", "<strong> <span style='color: #BA6F25; font-size: 12px'>"+currentComplexity+"</span>/<span style='color: #589DF6;'>"+limitOfComplexity+"</span> cognitive load.</strong><br /><strong>It's time to refactor your code.</strong>", NotificationType.ERROR);
        AnAction action = new NotificationAction.Simple(() -> "Open rules",(anActionEvent, notification1) -> {}, "asdasd");
        this.notification.addAction(action);
        this.notification.setCollapseActionsDirection(Notification.CollapseActionsDirection.KEEP_RIGHTMOST);
        this.notification.notify(project);
    }

    public void clear() {
        NotificationsManager mgr =
                NotificationsManager.getNotificationsManager();
        Notification[] all = mgr.getNotificationsOfType(
                Notification.class, project);
        for (Notification notification : all) {
            notification.expire();
        }
    }


    public void popup(String content) {
        BalloonBuilder balloonBuilder = JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(content, MessageType.INFO, null);
        Balloon balloon = balloonBuilder.setFadeoutTime(TimeUnit.SECONDS.toMillis(1)).createBalloon();
        Balloon.Position pos = Balloon.Position.above;
        balloon.show(RelativePoint.fromScreen(new Point(0, 0)), pos);

    }


}
