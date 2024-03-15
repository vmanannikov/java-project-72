package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public class App {

    public static void main(String[] args) throws SQLException, IOException {
        var app = getApp();
        app.start(getPort());
    }

    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }
    public static Javalin getApp() throws IOException, SQLException {
        String databaseUrl = System.getenv()
                .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(databaseUrl);

        var datasource = new HikariDataSource(hikariConfig);
        var sql = readResourceFile("schema.sql");

        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        })
                .get("/", ctx -> ctx.result("Hello World"))
                .start(getPort());
        return app;
    }
}
