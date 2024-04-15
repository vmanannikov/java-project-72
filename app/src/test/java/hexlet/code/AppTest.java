package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.Utils;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public final class AppTest {

    Javalin app;
    MockWebServer mockWebServer;
    MockResponse mockResponse;

    @BeforeEach
    void setUp() throws SQLException, IOException {
        app = App.getApp();
    }

    @AfterEach
    void tearDown() {
        app.stop();

        if (BaseRepository.dataSource != null) {
            BaseRepository.dataSource.close();
        }
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
            var responseBody = response.body().string();

            assertEquals("https://urls-checker.onrender.com",
                    UrlRepository.findByName("https://urls-checker.onrender.com")
                            .get()
                            .getName());
            assertThat(response.code()).isEqualTo(200);
            assertThat(responseBody).contains("Сайты");
            assertThat(responseBody).contains("https://urls-checker.onrender.com");
        });
    }

    @Test
    @Order(4)
    public void testUrlPage() throws SQLException {
        var url = new Url("https://google.com", new Timestamp(System.currentTimeMillis()));
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
            var responseUrl = client.get(NamedRoutes.urlPath(url.getId()));
            var body = responseUrl.body().string();
            assertThat(responseUrl.code()).isEqualTo(200);
            assertThat(body.contains("Сайт: https://google.com"));
            assertThat(body.contains("Проверки"));
        });
    }

    @Test
    @Order(5)
    public void testServerError() {
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get("/urls/000");
            assertThat(response.code()).isEqualTo(404);
        }));
    }

    @Test
    @Order(6)
    void testMockRequest() throws IOException {
        var htmlResponse = Utils.readResourceFile("fixtures/mock_url_check.html");
        mockWebServer = new MockWebServer();
        mockResponse = new MockResponse()
                .setBody(htmlResponse)
                .setResponseCode(200);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.start();

        var mockUrl = mockWebServer.url("/").toString();

        JavalinTest.test(app, (server, client) -> {
            var addedUrl = new Url(mockUrl);
            UrlRepository.save(addedUrl);

            var id = addedUrl.getId();
            client.post(NamedRoutes.urlCheckPath(id));
            var checks = UrlRepository.findChecksById(id);

            var title = checks.get(0).getTitle();
            var h1 = checks.get(0).getH1();

            assertThat(title).isEqualTo("Анализатор страниц");
            assertThat(h1).isEqualTo("Сайт https://www.example.com");

        });

        mockWebServer.close();
    }
}
