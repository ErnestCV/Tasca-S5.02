package cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.controller;

import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.request.LoginRequest;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.request.SignupRequest;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.JwtResponse;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.MessageResponse;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.MessageResponseWrapper;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signin(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        MessageResponseWrapper responseWrapper = authService.registerUser(signupRequest);
        return new ResponseEntity<>(responseWrapper.getMessageResponse(), responseWrapper.getStatus());
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAll() {
        return ResponseEntity.ok().body("Contingut públic");
    }
}