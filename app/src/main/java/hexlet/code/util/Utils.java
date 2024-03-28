package hexlet.code.util;

import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Utils {
    public static String formatURL(URL url) {
        var port = url.getPort() == -1 ? "" : ":" + String.valueOf(url.getPort());
        String protocol = url.getProtocol();
        String authority = url.getAuthority();
        var host = protocol.concat("://").concat(authority).concat(port);
        return host;
    }

    public static Timestamp getDateFormat(Timestamp timestamp, String pattern) {
        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        return Timestamp.valueOf(formater.format(timestamp));
    }
}
