package cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.controller;

import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.Game;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.request.GameDTO;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.request.UsernameChangeRequest;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.MessageResponse;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.MessageResponseWrapper;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.SuccessPercentageDTO;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.UserDTO;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok().body(playerService.getAllPlayers());
    }

    @GetMapping("/user")
    public ResponseEntity<String> user() {
        return new ResponseEntity<>("Hola User", HttpStatus.OK);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> admin() {
        return new ResponseEntity<>("Hola Admin", HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<SuccessPercentageDTO>> getAllPercentages() {
        return ResponseEntity.ok().body(playerService.getPlayerListSuccessPercentage());
    }

    @PostMapping("/{id}/games")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<UserDTO> newGame(@PathVariable("id") String id, @Valid @RequestBody GameDTO gameDTO) {
        return new ResponseEntity<>(playerService.postNewGame(id, gameDTO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDTO> changeUsername(@RequestBody UsernameChangeRequest newUsername) {
        return new ResponseEntity<>(playerService.updateUsername(newUsername), HttpStatus.CREATED);
    }

    @GetMapping("/ranking")
    public ResponseEntity<MessageResponse> getAverageRanking() {
        MessageResponseWrapper responseWrapper = playerService.getAveragePlayerRanking();
        return new ResponseEntity<>(responseWrapper.getMessageResponse(), responseWrapper.getStatus());
    }

    @GetMapping("/ranking/winner")
    public ResponseEntity<List<SuccessPercentageDTO>> getRankingBest() {
        return ResponseEntity.ok().body(playerService.getBestPlayers());
    }

    @GetMapping("/ranking/loser")
    public ResponseEntity<List<SuccessPercentageDTO>> getRankingWorst() {
        return ResponseEntity.ok().body(playerService.getWorstPlayers());
    }

    @DeleteMapping("/{id}/games")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> deleteGames(@PathVariable("id") String id) {
        return new ResponseEntity<>(playerService.deletePlayerGames(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/games")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<List<Game>> getGames(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(playerService.getPlayerGames(id));
    }
}
