package pl.oliwawyplywa.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.oliwawyplywa.web.schemas.Session;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionsRepository extends JpaRepository<Session, Integer> {

    @Query(value = "SELECT * FROM tbl_users_sessions WHERE id_user = :userId AND expires_at > NOW()", nativeQuery = true)
    List<Session> findValidSessionsByUser(int userId);

    Optional<Session> getSessionByToken(String token);
    Optional<Session> getSessionByIdSession(int idSession);

    void deleteByToken(String token);
}
