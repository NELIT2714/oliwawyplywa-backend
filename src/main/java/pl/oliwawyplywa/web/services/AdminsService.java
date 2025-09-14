package pl.oliwawyplywa.web.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.oliwawyplywa.web.dto.admins.CreateAdminDTO;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.repositories.AdminsRepository;
import pl.oliwawyplywa.web.schemas.Admin;
import pl.oliwawyplywa.web.schemas.Category;
import reactor.core.publisher.Mono;

@Service
public class AdminsService {

    private final AdminsRepository adminsRepository;

    public AdminsService(AdminsRepository adminsRepository) {
        this.adminsRepository = adminsRepository;
    }

    public Mono<Admin> createAdmin(CreateAdminDTO createAdminDTO) {
        return adminsRepository.findByUsername(createAdminDTO.getUsername().trim())
            .flatMap(existing -> Mono.<Admin>error(new HTTPException(HttpStatus.CONFLICT, "Username is taken")))
            .switchIfEmpty(Mono.defer(() -> {
                if (!createAdminDTO.getPassword().equals(createAdminDTO.getPasswordRepeat())) {
                    return Mono.error(new HTTPException(HttpStatus.BAD_REQUEST, "Passwords do not match"));
                }

                return adminsRepository.save(new Admin(createAdminDTO.getUsername().trim(), createAdminDTO.getPassword()));
            }));
    }

}
