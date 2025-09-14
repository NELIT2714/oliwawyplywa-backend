package pl.oliwawyplywa.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.oliwawyplywa.web.dto.admins.CreateAdminDTO;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/v1/admins")
@Validated
public class AdminsController {

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> createAdmin(@Valid @RequestBody CreateAdminDTO createAdminDTO) {

    }

}
