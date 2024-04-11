package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.Utils;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.io.IOException;
import java.sql.SQLException;

public final class AppTest {
    Javalin app;
    MockWebServer mockWebServer;
    MockResponse mockResponse;

    @BeforeEach
    void setUp() throws SQLException, IOException {
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
    void testMockRequest() throws IOException {
        var htmlPage = Utils.readResourceFile("fixtures/mock_url_check.html");
        mockWebServer = new MockWebServer();
        mockResponse = new MockResponse().setBody(htmlPage);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.start();

        var mockUrl = mockWebServer.url("/").toString();

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + mockUrl;
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);

            var urlName = String.format("%s://%s", mockWebServer.url("/").url().getProtocol(),
                    mockWebServer.url("/").url().getAuthority());
            var addUrl = UrlRepository.findByName(urlName);
            assertThat(addUrl).isNotNull();

            //var response2 = client.post("/urls/" + addUrl.get().getId() + "/checks");
            //assertThat(response2.code()).isEqualTo(200);

            //var urlCheck = UrlRepository.findChecksById(addUrl.get().getId());
            //var title = urlCheck.get(0).getTitle();
            //var h1 = urlCheck.get(0).getH1();
            //var description = urlCheck.get(0).getDescription();

            //assertThat(title).as("Check title").isEqualTo("This is a title");
            //assertThat(h1).as("Check h1").isEqualTo("This is a header");
            //assertThat(description).as("Check description").isEqualTo("This is a description");
            mockWebServer.close();
        });
    }

    @AfterEach
    void tearDown() throws IOException {
        app.stop();

        if (BaseRepository.dataSource != null) {
            BaseRepository.dataSource.close();
        }
    }
}
