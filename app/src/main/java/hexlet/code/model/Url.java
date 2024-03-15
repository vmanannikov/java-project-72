package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Url {
    private Long id;
    private String name;
    private String createdAt;
}


