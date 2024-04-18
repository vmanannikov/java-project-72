package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import hexlet.code.util.Utils;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

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
        var urlsWithCheck = UrlRepository.findLastCheck(urls);
        var urlsPage = new UrlsPage(urlsWithCheck);
        ctx.render("urls/index.jte", Collections.singletonMap("urlsPage", urlsPage));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Сайт не найден!"));
        var checks = UrlRepository.findChecksById(id);
        var urlPage = new UrlPage(url, checks);
        ctx.render("urls/show.jte", Collections.singletonMap("urlPage", urlPage));
    }

    public static void check(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Сайт не найден"));
        var page = new BasePage();
        List<UrlCheck> checks = new ArrayList<>();
        try {
            var response = Unirest.get(url.getName()).asString();
            var body = response.getBody();
            var html = Jsoup.parse(body);
            var title = html.title();
            var h1 = (html.selectFirst("h1") == null)
                    ? null : html.selectFirst("h1").text();
            var description = (html.selectFirst("meta[name=description]") == null)
                    ? null : html.selectFirst("meta[name=description]").attr("content");
            var statusCode = response.getStatus();
            var urlCheck = new UrlCheck(statusCode, title, h1, description, id, url.getCreatedAt());
            urlCheck.setUrlId(id);
            UrlRepository.saveCheck(urlCheck);
            checks = UrlRepository.findChecksById(url.getId());
            page.setFlash("Страница успешно проверена");
            page.setFlashType("success");
        } catch (UnirestException e) {
            page.setFlash("Некорреткный адрес");
            page.setFlashType("danger");
        }
        var urlPage = new UrlPage(url, checks);
        ctx.render("urls/show.jte", Map.of("page", page, "urlPage", urlPage));
    }
}
