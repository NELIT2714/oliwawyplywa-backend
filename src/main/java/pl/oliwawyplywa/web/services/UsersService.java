package pl.oliwawyplywa.web.services;

import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.oliwawyplywa.web.dto.users.CreateUser;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.repositories.PersonalDataRepository;
import pl.oliwawyplywa.web.repositories.SessionsRepository;
import pl.oliwawyplywa.web.repositories.UsersRepository;
import pl.oliwawyplywa.web.schemas.PersonalData;
import pl.oliwawyplywa.web.schemas.Session;
import pl.oliwawyplywa.web.schemas.User;
import pl.oliwawyplywa.web.utils.JwtTokensUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final PersonalDataRepository personalDataRepository;
    private final SessionsRepository sessionsRepository;

    private final JwtTokensUtil jwtTokensUtil;

    public UsersService(UsersRepository usersRepository, PersonalDataRepository personalDataRepository, SessionsRepository sessionsRepository, JwtTokensUtil jwtTokensUtil) {
        this.usersRepository = usersRepository;
        this.personalDataRepository = personalDataRepository;
        this.sessionsRepository = sessionsRepository;
        this.jwtTokensUtil = jwtTokensUtil;
    }

    @Transactional
    public User checkTokenAndGetUser(String token) {
        Session userSession = sessionsRepository.getSessionByToken(token).orElseThrow(() -> new HTTPException(HttpStatus.UNAUTHORIZED, "Invalid token"));
        return userSession.getUser();
    }

    @Transactional
    public User create(CreateUser userDto) {
        String username = userDto.getUsername();
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        if (usersRepository.getByUsername(username).isPresent()) {
            throw new HTTPException(HttpStatus.CONFLICT, "Username already exists");
        }

        if (personalDataRepository.getByEmail(email).isPresent()) {
            throw new HTTPException(HttpStatus.CONFLICT, "Email already exists");
        }

        PersonalData personalData = new PersonalData(email);
        personalDataRepository.save(personalData);

        return usersRepository.save(new User(username, personalData, BCrypt.hashpw(password, BCrypt.gensalt())));
    }

    @Transactional
    public String login(String usernameOrEmail, String password) {
        User user = usersRepository.findByUsernameOrEmail(usernameOrEmail).orElseThrow(() -> new HTTPException(HttpStatus.UNAUTHORIZED, "Invalid username or email"));

        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            throw new HTTPException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        Optional<Session> userSession = sessionsRepository.getSessionByUser(user);
        if (userSession.isPresent()) {
            return userSession.get().getToken();
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id_user", user.getIdUser());
        String jwtToken = jwtTokensUtil.generateToken(claims);

        sessionsRepository.save(new Session(jwtToken, user));
        return jwtToken;
    }

    @Transactional
    public void logout(String jwtToken) {
        Session userSession = sessionsRepository.getSessionByToken(jwtToken);
        sessionsRepository.delete(userSession);
    }
}
