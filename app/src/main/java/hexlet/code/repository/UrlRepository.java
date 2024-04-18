package hexlet.code.repository;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.util.Utils;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UrlRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) values (?, ?)";
        try (var conn = dataSource.getConnection();
            var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var timestamp = Utils.getDateFormat(Timestamp.valueOf(LocalDateTime.now()), "yyyy-MM-dd hh:mm:ss");
            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, timestamp);
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
                var created = resultSet.getTimestamp("created_at");
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
                var createdAt = resultSet.getTimestamp("created_at");
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
            return result.stream()
                    .sorted(Comparator.comparing(Url::getId).reversed())
                    .collect(Collectors.toList());
        }
    }

    public static void saveCheck(UrlCheck urlCheck) throws SQLException {
        String sql = "INSERT INTO url_checks (url_id, status_code, title, h1, description, created_at) "
                + "values (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
            var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var timestamp = Timestamp.valueOf(LocalDateTime.now());
            var createdAt = Utils.getDateFormat(timestamp, "yyyy-MM-dd hh:mm:ss");
            preparedStatement.setLong(1, urlCheck.getUrlId());
            preparedStatement.setInt(2, urlCheck.getStatusCode());
            preparedStatement.setString(3, urlCheck.getTitle());
            preparedStatement.setString(4, urlCheck.getH1());
            preparedStatement.setString(5, urlCheck.getDescription());
            preparedStatement.setTimestamp(6, createdAt);
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Произошла ошибка при извлечении идентификтора");
            }
        }
    }

    public static List<UrlCheck> findChecksById(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks where url_id = ? ORDER BY created_at DESC";
        try (var conn = dataSource.getConnection();
            var statement = conn.prepareStatement(sql)) {
            statement.setLong(1, urlId);
            var resultSet = statement.executeQuery();
            var result = new ArrayList<UrlCheck>();

            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getInt("status_code");
                var created = resultSet.getTimestamp("created_at");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var urlChecks = new UrlCheck(statusCode, title, h1, description, id, created);
                urlChecks.setId(id);
                result.add(urlChecks);
            }

            return result;
        }
    }

    public static List<Url> findLastCheck(List<Url> urls) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC LIMIT 1";
        try (var conn = dataSource.getConnection();
             var statement = conn.prepareStatement(sql)) {
             var listChecks = new ArrayList<UrlCheck>();
             for (var url : urls) {
                 statement.setLong(1, url.getId());
                 var resultSet = statement.executeQuery();

                 if (resultSet.next()) {
                     var statusCode = resultSet.getInt("status_code");
                     var created = resultSet.getTimestamp("created_at");
                     url.setLastStatusCodeCheck(statusCode);
                     url.setLastDateCheck(created);
                     listChecks.add(new UrlCheck(statusCode, created));
                 }
                 url.setUrlCheckList(listChecks);
             }

             return urls;
        }
    }
}
