package cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.integrationtests;

import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.controller.PlayerController;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.Game;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.SQLBackup.GameSQL;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.SQLBackup.UserSQL;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.User;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.request.GameDTO;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.request.UsernameChangeRequest;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.repository.UserRepository;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.repository.UserRepositorySQL;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.service.PlayerService;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.service.utils.Mapper;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.utils.MockSpringSecurityFilter;
import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.utils.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.utils.SecurityRequestPostProcessors.user;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PlayerService.class, Mapper.class, PlayerController.class})
@AutoConfigureMockMvc
public class PlayerControllerTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserRepositorySQL userRepositorySQL;

    @Spy
    @InjectMocks
    private PlayerService playerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    private User user1;
    private User user2;
    private User user3;
    private HashSet<Game> games1;
    private HashSet<Game> games2;
    private HashSet<Game> games3;
    private UserSQL userSQL1;
    private HashSet<GameSQL> gamesSQL1;
    private static final String API_URL = "/api/players";

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
    }

    @Test
    void shouldReturnUnauthorised() throws Exception {

        //given
        given(userRepository.findAll(Sort.by(Sort.Order.asc("username"))))
                .willReturn(Arrays.asList(user1, user2, user3));

        //then
        mockMvc.perform(get(API_URL + "/"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user1", password = "password1", roles = "USER")
    void shouldReturnPlayerList() throws Exception {

        //given
        given(userRepository.findAll(Sort.by(Sort.Order.asc("username"))))
                .willReturn(Arrays.asList(user1, user2, user3));

        //then
        mockMvc.perform(get(API_URL + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user1", password = "password1", roles = "USER")
    void shouldReturnPlayersRanking() throws Exception {

        //given
        games1.add(new Game(1, 2, LocalDateTime.now())); //Lost
        user1.setGames(games1);
        games2.add(new Game(5, 2, LocalDateTime.now())); //Won
        user2.setGames(games2);
        given(userRepository.findAll(Sort.by(Sort.Order.asc("username"))))
                .willReturn(Arrays.asList(user1, user2));

        //then
        mockMvc.perform(get(API_URL + "/ranking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("50.0"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user1", password = "password1", roles = "USER")
    void shouldReturnNoPlayers() throws Exception {

        //given
        given(userRepository.findAll(Sort.by(Sort.Order.asc("username"))))
                .willReturn(Collections.emptyList());

        //then
        mockMvc.perform(get(API_URL + "/ranking"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("There are no players in the system"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user1", password = "password1", roles = "USER")
    void shouldReturnNoGames() throws Exception {

        //given
        given(userRepository.findAll(Sort.by(Sort.Order.asc("username"))))
                .willReturn(Arrays.asList(user1, user2));

        //then
        mockMvc.perform(get(API_URL + "/ranking"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("There are no games recorded yet"))
                .andDo(print());
    }

    @Test
    void shouldPostNewGameAndReturnPlayer() throws Exception {

        //given
        user1.setId("1");
        given(userRepository.findById(anyString())).willReturn(Optional.of(user1));
        given(userRepository.save(user1)).willReturn(user1);
        userSQL1.setId(1);
        given(userRepositorySQL.findByUsername(anyString())).willReturn(Optional.of(userSQL1));
        given(userRepositorySQL.save(userSQL1)).willReturn(userSQL1);

        //then
        RequestBuilder request = post(API_URL + "/1/games").with(user(user1).roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new GameDTO("3", "4")));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity(new MockSpringSecurityFilter()))
                .build();

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user1.getId()))
                .andExpect(jsonPath("$.username").value(user1.getUsername()))
                .andExpect(jsonPath("$.email").value(user1.getEmail()))
                .andExpect(jsonPath("$.games.size()").value(1))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    void shouldUpdateUsernameAndReturnPlayer() throws Exception {

        //given
        user1.setId("1");
        given(userRepository.findById(anyString())).willReturn(Optional.of(user1));
        given(userRepository.save(user1)).willReturn(user1);
        userSQL1.setId(1);
        given(userRepositorySQL.findByUsername(anyString())).willReturn(Optional.of(userSQL1));
        given(userRepositorySQL.save(userSQL1)).willReturn(userSQL1);

        //then
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity(new MockSpringSecurityFilter()))
                .build();

        RequestBuilder request = put(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UsernameChangeRequest("newUsername")));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user1.getId()))
                .andExpect(jsonPath("$.username").value("newUsername"))
                .andExpect(jsonPath("$.email").value(user1.getEmail()))
                .andExpect(jsonPath("$.games.size()").value(0))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user1", password = "password1", roles = "USER")
    void shouldReturnBestPlayers() throws Exception {

        //given
        games1.add(new Game(1, 2, LocalDateTime.now())); //Lost
        user1.setGames(games1);
        games2.add(new Game(5, 2, LocalDateTime.now())); //Won
        user2.setGames(games2);
        games3.add(new Game(3, 4, LocalDateTime.now())); //Won
        user3.setGames(games3);
        given(userRepository.findAll())
                .willReturn(Arrays.asList(user1, user2, user3));

        //then
        mockMvc.perform(get(API_URL + "/ranking/winner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$.[*].username", Matchers.containsInAnyOrder("user2", "user3")))
                .andExpect(jsonPath("$.[*].successPercentage", Matchers.containsInAnyOrder("100.0", "100.0")))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user1", password = "password1", roles = "USER")
    void shouldReturnWorstPlayers() throws Exception {

        //given
        games1.add(new Game(1, 2, LocalDateTime.now())); //Lost
        user1.setGames(games1);
        games2.add(new Game(5, 2, LocalDateTime.now())); //Won
        user2.setGames(games2);
        games3.add(new Game(3, 4, LocalDateTime.now())); //Won
        user3.setGames(games3);
        given(userRepository.findAll())
                .willReturn(Arrays.asList(user1, user2, user3));

        //then
        mockMvc.perform(get(API_URL + "/ranking/loser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].username").value("user1"))
                .andExpect(jsonPath("$.[0].successPercentage").value("0.0"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user1", password = "password1", roles = "USER")
    void shouldDeleteGamesAndReturnPlayer() throws Exception {

        //given
        user1.setId("1");
        games1.add(new Game(1, 2, LocalDateTime.now())); //Lost
        user1.setGames(games1);
        given(userRepository.findById(anyString())).willReturn(Optional.of(user1));
        given(userRepository.save(user1)).willReturn(user1);
        userSQL1.setId(1);
        gamesSQL1.add(new GameSQL(1, 2, LocalDateTime.now()));
        userSQL1.setGames(gamesSQL1);
        given(userRepositorySQL.findByUsername(anyString())).willReturn(Optional.of(userSQL1));
        given(userRepositorySQL.save(userSQL1)).willReturn(userSQL1);

        //then
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity(new MockSpringSecurityFilter()))
                .build();

        mockMvc.perform(delete(API_URL + "/1/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1.getId()))
                .andExpect(jsonPath("$.username").value(user1.getUsername()))
                .andExpect(jsonPath("$.email").value(user1.getEmail()))
                .andExpect(jsonPath("$.games.size()").value(0))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user1", password = "password1", roles = "USER")
    void shouldReturnPlayersGames() throws Exception {

        //given
        games1.add(new Game(1, 2, LocalDateTime.now()));
        games1.add(new Game(3, 6, LocalDateTime.now()));
        user1.setGames(games1);
        user1.setId("1");
        given(userRepository.findById(anyString())).willReturn(Optional.of(user1));

        //then
        mockMvc.perform(get(API_URL + "/1/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$.[*].firstThrow", Matchers.containsInAnyOrder(1, 3)))
                .andExpect(jsonPath("$.[*].secondThrow", Matchers.containsInAnyOrder(2, 6)))
                .andDo(print());
    }
}
