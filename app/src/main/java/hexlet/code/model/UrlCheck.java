package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UrlCheck {
    private Long id;
    private int statusCode;
    private String title;
    private String h1;
    private String description;
    private Long urlId;
    private Timestamp createdAt;

    public UrlCheck(int statusCode, Timestamp createdAt, Long urlId) {
        this.statusCode = statusCode;
        this.createdAt = createdAt;
        this.urlId = urlId;
    }

    public UrlCheck(int statusCode, Timestamp createdAt) {
        this.statusCode = statusCode;
        this.createdAt = createdAt;
    }

}


