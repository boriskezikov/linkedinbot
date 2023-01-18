package com.tr.linkedinbot.logic;

import com.tr.linkedinbot.exception.IllegalLinkedInProfileException;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class LinkedInAccountServiceTest {

    private LinkedInAccountService service;

    private LinkedInProfileRepository repository;

    private ApplicationEventPublisher publisher;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(LinkedInProfileRepository.class);
        publisher = Mockito.mock(ApplicationEventPublisher.class);
        service = new LinkedInAccountService(repository, publisher);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.linkedin.com/in/username",
            "http://linkedin.com/in/username",
            "www.linkedin.com/in/username",
            "linkedin.com/in/username",
            "linkedin.com/in/username-123123123/"
    })
    void testSuccessCreateNewProfileWithValidUrl(String input) {
        Message message = new Message();
        message.setChat(new Chat(1L, "type"));
        message.setText(input);

        service.createNewProfile(message, "");

        verify(repository, times(1)).save(any());
        verify(publisher, times(1)).publishEvent(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.linkedin.com/posts/interestingengineering_5-biggest-tractors-activity-7019329892034981888-OSwl?utm_source=share&utm_medium=member_ios",
            "linkedin.com",
            "https://www.linkedin.com/company/username",
            "http://linkedin.com/jobs/username"
    })
    void testFailureCreateNewProfileWithInvalidUrl(String input) {
        Message message = new Message();
        message.setChat(new Chat(1L, "type"));
        message.setText(input);

        assertThrows(IllegalLinkedInProfileException.class, () -> service.createNewProfile(message, ""));
    }

}