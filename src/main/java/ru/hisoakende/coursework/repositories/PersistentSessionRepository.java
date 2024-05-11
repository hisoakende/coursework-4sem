package ru.hisoakende.coursework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hisoakende.coursework.models.PersistentSession;

@Repository
public interface PersistentSessionRepository extends JpaRepository<PersistentSession, Integer> {
}