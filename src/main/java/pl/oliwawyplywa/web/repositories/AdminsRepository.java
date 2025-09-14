package pl.oliwawyplywa.web.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.oliwawyplywa.web.schemas.Admin;
import reactor.core.publisher.Mono;

@Repository
public interface AdminsRepository extends ReactiveCrudRepository<Admin, Integer> {
    Mono<Admin> findByUsername(String username);
}
