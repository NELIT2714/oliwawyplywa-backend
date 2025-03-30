package pl.oliwawyplywa.web.controllers;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.oliwawyplywa.web.dto.users.CreateUser;
import pl.oliwawyplywa.web.dto.users.Login;
import pl.oliwawyplywa.web.dto.users.Logout;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.schemas.User;
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
    public User get(@RequestHeader("Authorization") String authorizationHeader) {
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new HTTPException(HttpStatus.UNAUTHORIZED, "Authorization header is invalid");
        }



//        return usersService.get(userId);
    }

    @PostMapping
    public User create(@Valid @RequestBody CreateUser userDto) {
        return usersService.create(userDto);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> login(@RequestBody Login loginDto) {
        String jwtToken = usersService.login(loginDto.getUsernameOrEmail(), loginDto.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", jwtToken));
    }

    @DeleteMapping(path = "/logout")
    public void logout(@RequestBody Logout logoutDto) {
        usersService.logout(logoutDto.getToken());
    }

}
