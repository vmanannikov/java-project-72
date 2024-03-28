package gg.jte.generated.ondemand.urls;
import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.util.NamedRoutes;
public final class JteindexGenerated {
	public static final String JTE_NAME = "urls/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,3,3,6,6,9,9,25,25,28,28,28,31,31,31,31,31,31,31,31,31,31,31,31,34,34,34,39,39,45,45,45};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, BasePage page, UrlsPage urlsPage) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <div class=\"container mt-5\">\n            <div class=\"row\">\n                <div>\n                    <h1>Сайты</h1>\n                    <table class=\"table table-bordered table-hover mt-3\">\n                        <thead>\n                        <tr>\n                            <th class=\"col-1\">ID</th>\n                            <th>Имя</th>\n                            <th class=\"col-2\">Последняя проверка</th>\n                            <th class=\"col-1\">Код ответа</th>\n                        </tr>\n                        </thead>\n                        <tbody>\n\n                        ");
				for (var url : urlsPage.getUrls()) {
					jteOutput.writeContent("\n                            <tr>\n                                <td>\n                                    ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(url.getId());
					jteOutput.writeContent("\n                                </td>\n                                <td>\n                                    <a");
					var __jte_html_attribute_0 = NamedRoutes.urlPath(url.getId());
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
						jteOutput.writeContent(" href=\"");
						jteOutput.setContext("a", "href");
						jteOutput.writeUserContent(__jte_html_attribute_0);
						jteOutput.setContext("a", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">");
					jteOutput.setContext("a", null);
					jteOutput.writeUserContent(url.getName());
					jteOutput.writeContent("</a>\n                                </td>\n                                <td>\n                                    ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(String.valueOf(url.getCreatedAt()));
					jteOutput.writeContent("\n                                </td>\n                                <td>\n                                </td>\n                            </tr>\n                        ");
				}
				jteOutput.writeContent("\n                        </tbody>\n                    </table>\n                </div>\n            </div>\n        </div>\n    ");
			}
		}, page);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		BasePage page = (BasePage)params.get("page");
		UrlsPage urlsPage = (UrlsPage)params.get("urlsPage");
		render(jteOutput, jteHtmlInterceptor, page, urlsPage);
	}
}