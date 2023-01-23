package com.tr.linkedinbot.notifications.events;

import com.tr.linkedinbot.model.LinkedInProfile;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LinkedInProfileCreateEvent extends ApplicationEvent {

    private final LinkedInProfile linkedInProfile;

    public LinkedInProfileCreateEvent(Object source, LinkedInProfile linkedInLink) {
        super(source);
        this.linkedInProfile = linkedInLink;
    }
}
