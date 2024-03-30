package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import hexlet.code.util.Utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map;

public class UrlController {
    public static void create(Context ctx) throws SQLException {
        var page = new BasePage();
        try {
            var url = ctx.formParamAsClass("url", String.class).get();
            var formattedUrl = Utils.formatURL(new URI(url).toURL());
            if (UrlRepository.findByName(formattedUrl).isPresent()) {
                page.setFlash("Страница уже существует");
                page.setFlashType("warning");
            } else {
                var urlName = new Url(url, new Timestamp(System.currentTimeMillis()));
                UrlRepository.save(urlName);
                page.setFlash("Страница успешно добавлена");
                page.setFlashType("success");
            }
            ctx.render("urls/index.jte", Map.of("page", page, "urlsPage", new UrlsPage(UrlRepository.getEntities())));
        } catch (MalformedURLException | URISyntaxException e) {
            page.setFlash("Некорректный URL");
            page.setFlashType("danger");
            ctx.render(NamedRoutes.rootPath());
        }
    }

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var urlsPage = new UrlsPage(urls);
        ctx.render("urls/index.jte", Collections.singletonMap("urlsPage", urlsPage));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Сайт не найден"));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }
}
