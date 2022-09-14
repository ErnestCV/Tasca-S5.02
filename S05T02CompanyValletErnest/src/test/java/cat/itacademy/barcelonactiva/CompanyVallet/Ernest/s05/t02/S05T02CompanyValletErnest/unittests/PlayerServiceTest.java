package cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.unittests;

import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.Game;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.SQLBackup.GameSQL;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.SQLBackup.UserSQL;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.User;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.request.GameDTO;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.request.UsernameChangeRequest;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.MessageResponseWrapper;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.SuccessPercentageDTO;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response.UserDTO;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.repository.UserRepository;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.repository.UserRepositorySQL;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.security.service.UserDetailsImpl;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRepositorySQL userRepositorySQL;

    @Mock
    private Authentication auth;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private PlayerService playerService;

    private User user1;
    private User user2;
    private User user3;
    private HashSet<Game> games1;
    private HashSet<Game> games2;
    private HashSet<Game> games3;
    private UserSQL userSQL1;
    private HashSet<GameSQL> gamesSQL1;
    private SuccessPercentageDTO successPercentageDTO1;
    private SuccessPercentageDTO successPercentageDTO2;
    private SuccessPercentageDTO successPercentageDTO3;

    @BeforeEach
    public void setup() {
        user1 = new User("user1", "email1@email.com", "password1");
        user2 = new User("user2", "email2@email.com", "password2");
        user3 = new User("user3", "email3@email.com", "password3");
        games1 = new HashSet<>();
        games2 = new HashSet<>();
        games3 = new HashSet<>();
        userSQL1 = new UserSQL("userSQL1", "emailSQL1@email.com", "password1");
        gamesSQL1 = new HashSet<>();
        successPercentageDTO1 = new SuccessPercentageDTO("user1", "0.0");
        successPercentageDTO2 = new SuccessPercentageDTO("user2", "100.0");
        successPercentageDTO3 = new SuccessPercentageDTO("user3", "100.0");
    }

    @Test
    public void givenEmployeeList_whenGetPlayerList_thenReturnPlayerList() {

        //given
        games1.add(new Game(1, 2, LocalDateTime.now()));
        user1.setGames(games1);
        games2.add(new Game(5, 2, LocalDateTime.now()));
        user2.setGames(games2);

        given(userRepository.findAll(Sort.by(Sort.Order.asc("username"))))
                .willReturn(Arrays.asList(user1, user2));

        //when
        List<SuccessPercentageDTO> result = playerService.getPlayerListSuccessPercentage();

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList(successPercentageDTO1, successPercentageDTO2));
        verify(userRepository).findAll(Sort.by(Sort.Order.asc("username")));
    }

    @Test
    public void givenEmptyEmployeeList_whenGetPlayerList_thenReturnPlayerList() {

        //given
        given(userRepository.findAll(Sort.by(Sort.Order.asc("username"))))
                .willReturn(Collections.emptyList());

        //when
        List<SuccessPercentageDTO> result = playerService.getPlayerListSuccessPercentage();

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
        verify(userRepository).findAll(Sort.by(Sort.Order.asc("username")));
    }

    @Test
    public void givenUser_whenPostNewGame_thenReturnUser() {

        //given
        user1.setId("1");
        given(userRepository.findById(anyString())).willReturn(Optional.of(user1));
        given(userRepository.save(user1)).willReturn(user1);
        given(userRepositorySQL.findByUsername(anyString())).willReturn(Optional.of(userSQL1));
        given(userRepositorySQL.save(userSQL1)).willReturn(userSQL1);

        //when
        UserDTO result = playerService.postNewGame("1", new GameDTO("1", "2"));

        //then
        games1.add(new Game(1, 2, LocalDateTime.now()));
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(new UserDTO("1", "user1", "email1@email.com", games1));
        verify(userRepository).save(user1);
    }

    @Test
    public void givenNonExistingUser_whenPostNewGame_thenThrowException() {

        //given
        user1.setId("1");
        given(userRepository.findById(anyString())).willReturn(Optional.empty());

        //then
        games1.add(new Game(1, 2, LocalDateTime.now()));
        assertThatThrownBy(() -> playerService.postNewGame("1", new GameDTO("1", "2")))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User with id: 1 not found");
    }

    @Test
    public void givenUser_whenUpdateUsername_thenReturnUser() {

        //given
        user1.setId("1");
        SecurityContextHolder.getContext().setAuthentication(auth);
        given(auth.getPrincipal())
                .willReturn(new UserDetailsImpl(user1.getId(),
                        user1.getUsername(),
                        user1.getEmail(),
                        user1.getPassword(),
                        Collections.emptyList()));
        given(userRepository.findById(anyString())).willReturn(Optional.of(user1));
        given(userRepository.save(user1)).willReturn(user1);
        given(userRepositorySQL.findByUsername(anyString())).willReturn(Optional.of(userSQL1));
        given(userRepositorySQL.save(userSQL1)).willReturn(userSQL1);

        //when
        UserDTO result = playerService.updateUsername(new UsernameChangeRequest("test"));

        //then
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(new UserDTO("1", "test", "email1@email.com", games1));
        verify(userRepository).save(user1);
    }

    @Test
    public void givenPlayerList_whenGetAverageRanking_thenReturnRanking() {

        //given
        games1.add(new Game(1, 2, LocalDateTime.now())); //Game lost
        user1.setGames(games1);
        games2.add(new Game(5, 2, LocalDateTime.now())); //Game won
        user2.setGames(games2);

        given(userRepository.findAll(Sort.by(Sort.Order.asc("username"))))
                .willReturn(Arrays.asList(user1, user2));

        //when
        MessageResponseWrapper result = playerService.getAveragePlayerRanking();

        //then
        assertThat(result).isNotNull();
        assertThat(result.getMessageResponse().getMessage()).isEqualTo("50.0");
        verify(userRepository).findAll(Sort.by(Sort.Order.asc("username")));
    }

    @Test
    public void givenEmptyPlayerList_whenGetAverageRanking_thenReturnMessage() {

        //given
        given(userRepository.findAll(Sort.by(Sort.Order.asc("username"))))
                .willReturn(Collections.emptyList());

        //when
        MessageResponseWrapper result = playerService.getAveragePlayerRanking();

        //then
        assertThat(result).isNotNull();
        assertThat(result.getMessageResponse().getMessage()).isEqualTo("There are no players in the system");
        verify(userRepository).findAll(Sort.by(Sort.Order.asc("username")));
    }

    @Test
    public void givenNoGames_whenGetAverageRanking_thenReturnMessage() {

        //given
        given(userRepository.findAll(Sort.by(Sort.Order.asc("username"))))
                .willReturn(Arrays.asList(user1, user2, user3));

        //when
        MessageResponseWrapper result = playerService.getAveragePlayerRanking();

        //then
        assertThat(result).isNotNull();
        assertThat(result.getMessageResponse().getMessage()).isEqualTo("There are no games recorded yet");
        verify(userRepository).findAll(Sort.by(Sort.Order.asc("username")));
    }

    @Test
    public void givenNoPlayers_whenGetBestPlayers_thenReturnEmptyList() {

        //given
        given(userRepository.findAll()).willReturn(Collections.emptyList());

        //when
        List<SuccessPercentageDTO> result = playerService.getBestPlayers();

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
        verify(userRepository).findAll();
    }

    @Test
    public void givenOneBestPlayer_whenGetBestPlayers_thenReturnBestPlayer() {

        //given
        games1.add(new Game(1, 2, LocalDateTime.now()));
        user1.setGames(games1);
        games2.add(new Game(5, 2, LocalDateTime.now())); //Best player
        user2.setGames(games2);

        given(userRepository.findAll()).willReturn(Arrays.asList(user1, user2));

        //when
        List<SuccessPercentageDTO> result = playerService.getBestPlayers();

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(successPercentageDTO2);
        verify(userRepository).findAll();
    }

    @Test
    public void givenTwoBestPlayers_whenGetBestPlayers_thenReturnBestPlayerList() {

        //given
        games1.add(new Game(1, 2, LocalDateTime.now()));
        user1.setGames(games1);
        games2.add(new Game(5, 2, LocalDateTime.now())); //Best player
        user2.setGames(games2);
        games3.add(new Game(6, 1, LocalDateTime.now())); //Best player
        user3.setGames(games3);

        given(userRepository.findAll()).willReturn(Arrays.asList(user1, user2, user3));

        //when
        List<SuccessPercentageDTO> result = playerService.getBestPlayers();

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList(successPercentageDTO2, successPercentageDTO3));
        verify(userRepository).findAll();
    }

    @Test
    public void givenNoPlayers_whenGetWorstPlayers_thenReturnEmptyList() {

        //given
        given(userRepository.findAll()).willReturn(Collections.emptyList());

        //when
        List<SuccessPercentageDTO> result = playerService.getWorstPlayers();

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
        verify(userRepository).findAll();
    }

    @Test
    public void givenOneWorstPlayer_whenGetWorstPlayers_thenReturnWorstPlayer() {

        //given
        games1.add(new Game(1, 2, LocalDateTime.now())); //Worst player
        user1.setGames(games1);
        games2.add(new Game(5, 2, LocalDateTime.now()));
        user2.setGames(games2);

        given(userRepository.findAll()).willReturn(Arrays.asList(user1, user2));

        //when
        List<SuccessPercentageDTO> result = playerService.getWorstPlayers();

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(successPercentageDTO1);
        verify(userRepository).findAll();
    }

    @Test
    public void givenTwoWorstPlayers_whenGetBestPlayers_thenReturnBestPlayerList() {

        //given
        games1.add(new Game(1, 2, LocalDateTime.now())); //Worst player
        user1.setGames(games1);
        games2.add(new Game(2, 3, LocalDateTime.now())); //Worst player
        user2.setGames(games2);
        games3.add(new Game(6, 1, LocalDateTime.now()));
        user3.setGames(games3);

        given(userRepository.findAll()).willReturn(Arrays.asList(user1, user2, user3));

        //when
        List<SuccessPercentageDTO> result = playerService.getWorstPlayers();

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList(successPercentageDTO1, new SuccessPercentageDTO("user2", "0.0")));
        verify(userRepository).findAll();
    }

    @Test
    public void givenUserId_whenDeletePlayerGames_thenReturnUserWithNoGames() {

        //given
        games1.add(new Game(1, 2, LocalDateTime.now()));
        user1.setGames(games1);
        given(userRepository.findById(anyString())).willReturn(Optional.of(user1));
        gamesSQL1.add(new GameSQL(1, 2, LocalDateTime.now()));
        userSQL1.setGames(gamesSQL1);
        given(userRepositorySQL.findByUsername(anyString())).willReturn(Optional.of(userSQL1));

        //when
        UserDTO result = playerService.deletePlayerGames("1");

        //then
        assertThat(result).isNotNull();
        assertThat(result.getGames()).isEmpty();
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(new UserDTO(user1.getId(), user1.getUsername(), user1.getEmail(), Collections.emptySet()));
    }

    @Test
    public void givenUserId_whenGetPlayerGames_thenReturnGameList() {

        //given
        games1.add(new Game(1, 2, LocalDateTime.now()));
        games1.add(new Game(5, 2, LocalDateTime.now()));
        user1.setGames(games1);
        given(userRepository.findById(anyString())).willReturn(Optional.of(user1));

        //when
        List<Game> result = playerService.getPlayerGames("1");

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrderElementsOf(games1);
    }

    @Test
    public void givenUserIdAndEmptyGamesList_whenGetPlayerGames_thenReturnEmptyList() {

        //given
        given(userRepository.findById(anyString())).willReturn(Optional.of(user1));

        //when
        List<Game> result = playerService.getPlayerGames("1");

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
    }
}
