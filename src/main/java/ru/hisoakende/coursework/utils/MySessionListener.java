package ru.hisoakende.coursework.utils;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.hisoakende.coursework.models.PersistentSession;
import ru.hisoakende.coursework.repositories.PersistentSessionRepository;

import java.time.LocalDateTime;

@Data
@Component
public class MySessionListener implements HttpSessionListener {

    private PersistentSessionRepository persistentSessionRepository;

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {


        String sessionId = se.getSession().getId();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        PersistentSession persistentSession = new PersistentSession();
        persistentSession.setSeries(sessionId);
        persistentSession.setUsername(username);

        persistentSession.setLastUsed(LocalDateTime.now());

        persistentSessionRepository.save(persistentSession);
    }
}