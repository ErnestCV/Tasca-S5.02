package cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@NoArgsConstructor
@Getter
@Setter
public class Game {

    private int firstThrow;
    private int secondThrow;
    private String dateTime;

    public Game(int firstThrow, int secondThrow, LocalDateTime dateTime) {
        this.firstThrow = firstThrow;
        this.secondThrow = secondThrow;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        this.dateTime = dateTime.format(formatter);
    }
}
