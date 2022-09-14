package cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.SQLBackup;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
public class UserSQL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer id;
    private String username;
    private String email;
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_has_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleSQL> roles = new HashSet<>();
    @OneToMany(mappedBy = "userSQL", fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = GameSQL.class, cascade = CascadeType.ALL)
    private Set<GameSQL> games = new HashSet<>();

    public UserSQL(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
