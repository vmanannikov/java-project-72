package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

public class AppTest {
    static Javalin app;

    @BeforeAll
    static final void setUp() throws SQLException, IOException {
        app = App.getApp();
    }

    @Test
    @Order(1)
    void testMainPage() {
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        }));
    }

    @Test
    @Order(2)
    void testUrlsPage() {
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        }));
    }

    @Test
    @Order(3)
    void testAddUrl() {
        JavalinTest.test(app, (server, client) -> {
            var request = "url=https://urls-checker.onrender.com";
            client.post(NamedRoutes.urlsPath(), request);
            var response = client.get(NamedRoutes.urlsPath());
            var body = response.body().string();

            assertEquals("https://urls-checker.onrender.com",
                    UrlRepository.findByName("https://urls-checker.onrender.com")
                            .get()
                            .getName());
            assertThat(response.code()).isEqualTo(200);
            assertThat(body).contains("Сайты");
            assertThat(body).contains("https://urls-checker.onrender.com");
        });
    }
}
