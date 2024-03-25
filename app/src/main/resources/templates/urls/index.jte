@import hexlet.code.dto.BasePage
@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.util.NamedRoutes
@param BasePage page
@param UrlsPage urlsPage

@template.layout.page(
    page = page,
    content = @`
        <div class="container mt-5">
            <div class="row">
                <div>
                    <h1>Сайты</h1>
                    <table class="table table-bordered table-hover mt-3">
                        <thead>
                        <tr>
                            <th class="col-1">ID</th>
                            <th>Имя</th>
                            <th class="col-2">Последняя проверка</th>
                            <th class="col-1">Код ответа</th>
                        </tr>
                        </thead>
                        <tbody>

                        @for(var url : urlsPage.getUrls())
                            <tr>
                                <td>
                                    ${url.getId()}
                                </td>
                                <td>
                                    <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                                </td>
                                <td>
                                    ${String.valueOf(url.getCreatedAt())}
                                </td>
                                <td>
                                </td>
                            </tr>
                        @endfor
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    `
)