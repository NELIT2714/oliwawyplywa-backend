package pl.oliwawyplywa.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.oliwawyplywa.web.schemas.Session;
import pl.oliwawyplywa.web.schemas.User;

import java.util.Optional;

public interface SessionsRepository extends JpaRepository<Session, String> {

    Optional<Session> getSessionByToken(String token);
    Optional<Session> getSessionByUser(User user);
}
