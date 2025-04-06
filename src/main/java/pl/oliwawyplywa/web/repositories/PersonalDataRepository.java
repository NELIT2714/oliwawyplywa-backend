package pl.oliwawyplywa.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.oliwawyplywa.web.schemas.PersonalData;

import java.util.Optional;

@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData, Integer> {

    @Query(value = "SELECT * FROM tbl_personal_data WHERE email = :email", nativeQuery = true)
    Optional<PersonalData> getByEmail(String email);

}
