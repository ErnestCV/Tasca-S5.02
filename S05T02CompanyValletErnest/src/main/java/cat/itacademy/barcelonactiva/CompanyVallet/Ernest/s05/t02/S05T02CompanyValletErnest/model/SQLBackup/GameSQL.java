package cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model.SQLBackup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "games")
@NoArgsConstructor
@Getter
@Setter
public class GameSQL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_game")
    private Integer id;
    @Column(name = "firstthrow")
    private int firstThrow;
    @Column(name = "secondthrow")
    private int secondThrow;
    @Column(name = "datetime")
    private LocalDateTime datetime = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserSQL userSQL;

    public GameSQL(int firstThrow, int secondThrow, LocalDateTime datetime) {
        this.firstThrow = firstThrow;
        this.secondThrow = secondThrow;
        this.datetime = datetime;
    }
}
