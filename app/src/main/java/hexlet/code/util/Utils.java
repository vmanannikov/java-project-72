package hexlet.code.util;

import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Utils {
    public static String formatURL(URL url) {
        var port = url.getPort() == -1 ? "" : ":" + String.valueOf(url.getPort());
        String protocol = url.getProtocol();
        String authority = url.getAuthority();
        return protocol.concat("://").concat(authority).concat(port);
    }

    public static Timestamp getDateFormat(Timestamp timestamp, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return Timestamp.valueOf(formatter.format(timestamp));
    }
}
