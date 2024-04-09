package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class Url {
    private Long id;
    private String name;
    private Timestamp createdAt;
    List<UrlCheck> urlCheckList;

    public Url(String name, Timestamp createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }

    public Url(String name) {
        this.name = name;
    }

    public Timestamp getLastCheck() {
        return urlCheckList.stream()
                .sorted(Comparator.comparing(UrlCheck::getCreatedAt).reversed())
                .collect(Collectors.toList())
                .get(0).getCreatedAt();
    }

    public Integer getCheckStatusCode() {
        return urlCheckList.stream()
                .sorted(Comparator.comparingInt(UrlCheck::getStatusCode).reversed())
                .collect(Collectors.toList())
                .get(0).getStatusCode();
    }
}


