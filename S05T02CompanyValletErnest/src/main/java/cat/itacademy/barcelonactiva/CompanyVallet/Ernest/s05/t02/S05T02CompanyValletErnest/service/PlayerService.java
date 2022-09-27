package cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.service;

import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.Game;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.SQLBackup.GameSQL;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.SQLBackup.UserSQL;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.User;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.request.GameDTO;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.request.UsernameChangeRequest;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.MessageResponse;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.MessageResponseWrapper;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.SuccessPercentageDTO;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.UserDTO;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.repository.UserRepository;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.repository.UserRepositorySQL;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.security.service.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.ERole.ROLE_ADMIN;

@Service
public class PlayerService {

    //El joc es guanya si obtenim un 7
    private static final int GAME_WIN_CONDITION = 7;

    //MongoDb
    @Autowired
    private UserRepository userRepository;

    //SQL (Backup)
    @Autowired
    private UserRepositorySQL userRepositorySQL;

    @Autowired
    private ModelMapper modelMapper;

    //Troba tots usuaris (inclou admin) i ordena per usuari i després els jocs per data, retorna DTO
    public List<UserDTO> getAllPlayers() {
        return userRepository.findAll(Sort.by(Sort.Order.asc("username"), Sort.Order.asc("games.dateTime")))
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    //Troba tots usuaris ordenats per usuari, no mostra admins
    public List<SuccessPercentageDTO> getPlayerListSuccessPercentage() {
        return userRepository.findAll(Sort.by(Sort.Order.asc("username"))).stream()
                .filter(this::isNotAdmin)
                .map(this::buildSuccessPercentageDTO)
                .collect(Collectors.toList());
    }

    public UserDTO postNewGame(String id, GameDTO gameDTO) {
        //MongoDB
        User user = getUser(id);
        Game game = new Game(Integer.parseInt(gameDTO.getFirstThrow()),
                Integer.parseInt(gameDTO.getSecondThrow()),
                LocalDateTime.now());
        user.getGames().add(game);
        userRepository.save(user);

        //SQL
        UserSQL userSQL = getUserFromBackup(user.getUsername());
        GameSQL gameSQL = new GameSQL(Integer.parseInt(gameDTO.getFirstThrow()),
                Integer.parseInt(gameDTO.getSecondThrow()),
                LocalDateTime.now());
        gameSQL.setUserSQL(userSQL);
        userSQL.getGames().add(gameSQL);
        userRepositorySQL.save(userSQL);

        return mapToDTO(user);
    }

    public UserDTO updateUsername(UsernameChangeRequest newUsername) {
        //MongoDB - Extreu l'usuari autenticat
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = getUser(userDetails.getId());
        user.setUsername(newUsername.getNewUsername());
        userRepository.save(user);

        //SQL
        UserSQL userSQL = getUserFromBackup(userDetails.getUsername());
        userSQL.setUsername(newUsername.getNewUsername());
        userRepositorySQL.save(userSQL);

        return mapToDTO(user);
    }

    //Retorna missatge i codi HTTP
    public MessageResponseWrapper getAveragePlayerRanking() {
        List<User> players = userRepository.findAll(Sort.by(Sort.Order.asc("username"))).stream()
                .filter(this::isNotAdmin)
                .toList();
        String message;
        HttpStatus status = HttpStatus.NO_CONTENT;


        //Mirem si hi ha jugadors, o si no hi ha jocs encara ho mostra. Si no, treu el valor mitjà del ranking
        if (players.isEmpty()) {
            message = "There are no players in the system";
        } else {
            double totalSum = players.stream()
                    .map(this::getPlayerSuccessPercentage)
                    .filter(percentage -> !percentage.equals("N/A"))
                    .mapToDouble(Double::parseDouble)
                    .reduce(0d, Double::sum);
            long count = players.stream()
                    .filter(player -> player.getGames().size() > 0)
                    .count();
            if (count == 0) {
                message = "There are no games recorded yet";
            } else {
                message = String.valueOf(totalSum / count);
                status = HttpStatus.OK;
            }
        }
        return new MessageResponseWrapper(status, new MessageResponse(message));
    }

    //Fa servir un TreeMap que ordena jugadors pel seu rànking, retorna llista dels que el tenen més elevat
    public List<SuccessPercentageDTO> getBestPlayers() {
        List<User> players = userRepository.findAll();
        List<SuccessPercentageDTO> result = new ArrayList<>();
        if (!players.isEmpty()) {
            result = groupPlayersBySuccessPercentage(players)
                    .lastEntry()
                    .getValue();
        }
        return result;
    }

    //Fa servir un TreeMap que ordena jugadors pel seu rànking, retorna llista dels que el tenen més baix
    public List<SuccessPercentageDTO> getWorstPlayers() {
        List<User> players = userRepository.findAll();
        List<SuccessPercentageDTO> result = new ArrayList<>();
        if (!players.isEmpty()) {
            result = groupPlayersBySuccessPercentage(players)
                    .firstEntry()
                    .getValue();
        }
        return result;
    }

    /*
    Compara el valor del rànking de cada jugador i els posa en un mapa on la clau és el seu ranking i el valor és la
    llista de jugadors amb aquest rànking
    */
    private TreeMap<String, List<SuccessPercentageDTO>> groupPlayersBySuccessPercentage(List<User> players) {
        Comparator<String> comparator = Comparator.comparingDouble(Double::parseDouble);
        return players.stream()
                .filter(this::isNotAdmin)
                .filter(player -> player.getGames().size() > 0)
                .map(this::buildSuccessPercentageDTO)
                .collect(Collectors.groupingBy(
                        SuccessPercentageDTO::getSuccessPercentage,
                        () -> new TreeMap<>(comparator),
                        Collectors.toList()
                ));
    }

    //Builder pattern
    private SuccessPercentageDTO buildSuccessPercentageDTO(User user) {
        return new SuccessPercentageDTO(user.getUsername(), getPlayerSuccessPercentage(user));
    }

    //Fa un update dels jocs (buit)
    public UserDTO deletePlayerGames(String id) {
        //MongoDB
        User user = getUser(id);
        user.setGames(new HashSet<>());
        userRepository.save(user);

        //SQL
        UserSQL userSQL = getUserFromBackup(user.getUsername());
        userSQL.getGames().removeAll(userSQL.getGames());
        userRepositorySQL.save(userSQL);

        return mapToDTO(user);
    }

    //Retorna llistat jocs ordenat per data
    public List<Game> getPlayerGames(String id) {
        return getUser(id).getGames().stream().sorted(Comparator.comparing(Game::getDateTime)).toList();
    }

    //MongoDB
    private User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with id: " + id + " not found"));
    }

    //SQL
    private UserSQL getUserFromBackup(String username) {
        return userRepositorySQL.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));
    }

    //Retorna percentatge d'èxit de cada jugador, o N/A si no ha jugat encara
    private String getPlayerSuccessPercentage(User user) {
        float gamesTotal = user.getGames() == null ? 0 : user.getGames().size();
        String percentage = "N/A";
        if (gamesTotal > 0) {
            float gamesWon = user.getGames().stream()
                    .filter(game -> game.getFirstThrow() + game.getSecondThrow() == GAME_WIN_CONDITION)
                    .count();
            float successPercentage = gamesWon / gamesTotal;
            percentage = String.valueOf(successPercentage * 100);
        }
        return percentage;
    }

    //Filtre admin
    private boolean isNotAdmin(User player) {
        return player.getRoles().stream().noneMatch(role -> role.getName().equals(ROLE_ADMIN));
    }

    //ModelMapper a DTO
    private UserDTO mapToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}