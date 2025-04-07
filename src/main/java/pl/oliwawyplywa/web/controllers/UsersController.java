package pl.oliwawyplywa.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.oliwawyplywa.web.dto.sessions.SessionDTO;
import pl.oliwawyplywa.web.dto.shippingAddresses.AddressResponseDTO;
import pl.oliwawyplywa.web.dto.users.*;
import pl.oliwawyplywa.web.schemas.PersonalData;
import pl.oliwawyplywa.web.schemas.User;
import pl.oliwawyplywa.web.services.SessionsService;
import pl.oliwawyplywa.web.services.ShippingAddressesService;
import pl.oliwawyplywa.web.services.UsersService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Validated
public class UsersController {

    private final UsersService usersService;
    private final SessionsService sessionsService;
    private final ShippingAddressesService shippingAddressesService;

    public UsersController(UsersService usersService, SessionsService sessionsService, ShippingAddressesService shippingAddressesService) {
        this.usersService = usersService;
        this.sessionsService = sessionsService;
        this.shippingAddressesService = shippingAddressesService;
    }

    @GetMapping(path = "/{id}")
    public UserResponse get(@PathVariable(name = "id") int userId) {
        User user = usersService.getUserById(userId);

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

        List<AddressResponseDTO> shippingAddresses = user.getShippingAddresses()
                .stream()
                .map(a -> new AddressResponseDTO(
                                a.getIdShippingAddress(), a.getCountry(),
                                a.getVoivodeship(), a.getCity(), a.getPostcode(), a.getStreet()
                        )
                )
                .toList();

        return new UserResponse(user.getIdUser(), user.getUsername(), personalDataDTO, shippingAddresses, sessions);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody CreateUserDTO userDTO) {
        String jwtToken = usersService.create(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", jwtToken));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UpdateUserDTO userDTO, @PathVariable(name = "id") int userId) {
        usersService.update(userDTO, userId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", true));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable(name = "id") int userId) {
        usersService.delete(userId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", true));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) {
        String jwtToken = usersService.login(loginDTO.getUsernameOrEmail(), loginDTO.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", jwtToken));
    }

    @DeleteMapping(path = "/logout")
    public ResponseEntity<Object> logout(@RequestBody LogoutDTO logoutDTO) {
        usersService.logout(logoutDTO.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", true));
    }

}
