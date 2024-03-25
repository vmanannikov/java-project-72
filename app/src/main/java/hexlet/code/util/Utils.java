package hexlet.code.util;

import java.net.URL;

public class Utils {
    public static String formatURL(URL url) {
        var port = url.getPort() == -1 ? "" : ":" + String.valueOf(url.getPort());
        String protocol = url.getProtocol();
        String authority = url.getAuthority();
        return protocol + "//:" + authority + port;
    }
}
