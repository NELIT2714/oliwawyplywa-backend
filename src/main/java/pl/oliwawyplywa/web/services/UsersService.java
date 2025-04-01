package pl.oliwawyplywa.web.services;

import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.oliwawyplywa.web.dto.sessions.SessionDTO;
import pl.oliwawyplywa.web.dto.users.CreateUserDTO;
import pl.oliwawyplywa.web.dto.users.PersonalDataDTO;
import pl.oliwawyplywa.web.dto.users.UserResponse;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.repositories.PersonalDataRepository;
import pl.oliwawyplywa.web.repositories.UsersRepository;
import pl.oliwawyplywa.web.schemas.PersonalData;
import pl.oliwawyplywa.web.schemas.Session;
import pl.oliwawyplywa.web.schemas.User;

import java.util.List;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final PersonalDataRepository personalDataRepository;
    private final SessionsService sessionsService;

    public UsersService(UsersRepository usersRepository, PersonalDataRepository personalDataRepository, SessionsService sessionsService) {
        this.usersRepository = usersRepository;
        this.personalDataRepository = personalDataRepository;
        this.sessionsService = sessionsService;
    }

    public UserResponse getUserByToken(String jwtToken) {
        Session session = sessionsService.getSession(jwtToken);
        User user = session.getUser();

        if (user == null) {
            throw new HTTPException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        List<SessionDTO> sessions = sessionsService.getValidSessionsByUser(user)
            .stream()
            .map(s -> new SessionDTO(s.getToken(), s.getExpiresAt()))
            .toList();

        PersonalData userPersonalData = user.getPersonalData();
        PersonalDataDTO personalDataDTO = new PersonalDataDTO(
            userPersonalData.getEmail(),
            userPersonalData.getFirstName(),
            userPersonalData.getLastName(),
            userPersonalData.getPhoneNumber()
        );

        return new UserResponse(user.getUsername(), personalDataDTO, user.getShippingAddresses(), sessions);
    }

    @Transactional
    public String create(CreateUserDTO userDto) {
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
        User user = usersRepository.save(new User(username, personalData, BCrypt.hashpw(password, BCrypt.gensalt())));

        return sessionsService.createSession(user).getToken();
    }

    public String login(String usernameOrEmail, String password) {
        User user = usersRepository.findByUsernameOrEmail(usernameOrEmail).orElseThrow(() -> new HTTPException(HttpStatus.UNAUTHORIZED, "Invalid username or email"));

        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            throw new HTTPException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        return sessionsService.createSession(user).getToken();
    }

    public void logout(String jwtToken) {
        sessionsService.deleteSession(jwtToken);
    }
}
