package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Url {
    private Long id;
    private String name;
    private LocalDate createdAt;
}


