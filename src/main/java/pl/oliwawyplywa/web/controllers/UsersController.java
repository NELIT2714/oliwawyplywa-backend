package pl.oliwawyplywa.web.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.oliwawyplywa.web.dto.users.CreateUserDTO;
import pl.oliwawyplywa.web.dto.users.LoginDTO;
import pl.oliwawyplywa.web.dto.users.UpdateUserDTO;
import pl.oliwawyplywa.web.dto.users.UserResponse;
import pl.oliwawyplywa.web.services.UsersService;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Validated
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping(path = "/me")
    public UserResponse get(@RequestHeader("Authorization") @Pattern(regexp = "^Bearer .+$") String authorizationHeader) {
        return usersService.getUserByToken(authorizationHeader.substring("Bearer ".length()));
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody CreateUserDTO userDTO) {
        String jwtToken = usersService.create(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", jwtToken));
    }

    @PatchMapping
    public void update(@Valid @RequestBody UpdateUserDTO userDTO) {

    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) {
        String jwtToken = usersService.login(loginDTO.getUsernameOrEmail(), loginDTO.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", jwtToken));
    }

    @DeleteMapping(path = "/logout")
    public void logout(@RequestHeader("Authorization") @Pattern(regexp = "^Bearer .+$") String authorizationHeader) {
        usersService.logout(authorizationHeader.substring("Bearer ".length()));
    }

}
