package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository{
    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) values (?, ?)";

        try (var conn = dataSource.getConnection();
        var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Произошла ошибка при извлечении идентификтора");
            }
        }
    }

    public static Optional<Url> findById(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var statement = conn.prepareStatement(sql)) {
             statement.setLong(1, id);
             var resultSet = statement.executeQuery();
             if (resultSet.next()) {
                 var name = resultSet.getString("name");
                 var created = resultSet.getTimestamp("created_at");;
                 var url = new Url(name, created);
                 url.setId(id);
                 return Optional.of(url);
             }
             return Optional.empty();
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var statement = conn.prepareStatement(sql)) {
             statement.setString(1, name);
             var resultSet = statement.executeQuery();
             if (resultSet.next()) {
                 var id = resultSet.getLong("id");
                 Timestamp createdAt = resultSet.getTimestamp("created_at");
                 var url = new Url(name);
                 url.setId(id);
                 url.setCreatedAt(createdAt);
                 return Optional.of(url);
             }
            return Optional.empty();
        }
    }

    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT * FROM urls";
        try (var conn = dataSource.getConnection();
             var statement = conn.prepareStatement(sql)) {
             var resultSet = statement.executeQuery();
             var result = new ArrayList<Url>();
             while (resultSet.next()) {
                 var id = resultSet.getLong("id");
                 var name = resultSet.getString("name");
                 var created = resultSet.getTimestamp("created_at");
                 var url = new Url(name, created);
                 url.setId(id);
                 result.add(url);
             }
             return result;
        }
    }
}
