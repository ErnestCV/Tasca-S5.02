package cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.response;

import cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String id;
    private String username;
    private String email;
    private Set<Game> games;
}
