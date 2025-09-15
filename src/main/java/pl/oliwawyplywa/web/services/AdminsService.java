package pl.oliwawyplywa.web.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.oliwawyplywa.web.dto.admins.CreateAdminDTO;
import pl.oliwawyplywa.web.dto.admins.LoginDTO;
import pl.oliwawyplywa.web.dto.admins.TokenResponse;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.repositories.AdminsRepository;
import pl.oliwawyplywa.web.schemas.Admin;
import pl.oliwawyplywa.web.utils.JwtTokensUtil;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AdminsService {

    private final AdminsRepository adminsRepository;
    private final JwtTokensUtil jwtTokensUtil;

    public AdminsService(AdminsRepository adminsRepository, JwtTokensUtil jwtTokensUtil) {
        this.adminsRepository = adminsRepository;
        this.jwtTokensUtil = jwtTokensUtil;
    }

    public Mono<Admin> createAdmin(CreateAdminDTO createAdminDTO) {
        String username = createAdminDTO.getUsername().trim();
        String password = createAdminDTO.getPassword();

        return adminsRepository.findByUsername(username)
            .flatMap(existing -> Mono.<Admin>error(new HTTPException(HttpStatus.CONFLICT, "Username is taken")))
            .switchIfEmpty(Mono.defer(() -> {
                if (!password.equals(createAdminDTO.getPasswordRepeat())) {
                    return Mono.error(new HTTPException(HttpStatus.BAD_REQUEST, "Passwords do not match"));
                }

                return adminsRepository.save(new Admin(createAdminDTO.getUsername().trim(), BCrypt.hashpw(password, BCrypt.gensalt())));
            }));
    }

    public Mono<TokenResponse> login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        return adminsRepository.findByUsername(username)
            .flatMap(admin -> {
                if (!BCrypt.checkpw(password, admin.getPasswordHash())) {
                    return Mono.error(new HTTPException(HttpStatus.UNAUTHORIZED, "Password is incorrect"));
                }
                return jwtTokensUtil.generateToken(Map.of("admin_id", admin.getAdminId()));
            })
            .switchIfEmpty(Mono.defer(() ->
                Mono.error(new HTTPException(HttpStatus.UNAUTHORIZED, "Username is incorrect"))
            ));
    }
}
