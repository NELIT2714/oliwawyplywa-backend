package pl.oliwawyplywa.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.oliwawyplywa.web.schemas.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM tbl_users WHERE username = :username", nativeQuery = true)
    Optional<User> getByUsername(String username);

    @Query(value = "SELECT * FROM tbl_users WHERE id_personal_data = :id_personal_data", nativeQuery = true)
    Optional<User> getByPersonalData(@Param("id_personal_data") int idPersonalData);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.personalData p WHERE u.username = :usernameOrEmail OR p.email = :usernameOrEmail")
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

}
