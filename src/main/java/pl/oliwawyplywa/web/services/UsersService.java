package pl.oliwawyplywa.web.services;

import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.oliwawyplywa.web.dto.users.CreateUserDTO;
import pl.oliwawyplywa.web.dto.users.UpdateUserDTO;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.repositories.PersonalDataRepository;
import pl.oliwawyplywa.web.repositories.UsersRepository;
import pl.oliwawyplywa.web.schemas.PersonalData;
import pl.oliwawyplywa.web.schemas.Session;
import pl.oliwawyplywa.web.schemas.User;
import pl.oliwawyplywa.web.utils.mappers.UserMapper;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final PersonalDataRepository personalDataRepository;
    private final SessionsService sessionsService;
    private final UserMapper userMapper;

    public UsersService(UsersRepository usersRepository, PersonalDataRepository personalDataRepository, SessionsService sessionsService, UserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.personalDataRepository = personalDataRepository;
        this.sessionsService = sessionsService;
        this.userMapper = userMapper;
    }

    public User getUserById(int userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new HTTPException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    public User getUserByToken(String jwtToken) {
        Session session = sessionsService.getSession(jwtToken);
        User user = session.getUser();

        if (user == null) {
            throw new HTTPException(HttpStatus.UNAUTHORIZED, "User not found");
        }
        return user;
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

    public void update(UpdateUserDTO userDTO, int userId) {
        User user = getUserById(userId);

        userMapper.updateUserFromDTO(userDTO, user);

        if (userDTO.getPersonalData() != null) {
            PersonalData userPersonalData = user.getPersonalData();
            userMapper.updatePersonalDataFromDTO(userDTO.getPersonalData(), userPersonalData);
        }

        usersRepository.save(user);
    }

    public void delete(int userId) {
        usersRepository.delete(getUserById(userId));
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
