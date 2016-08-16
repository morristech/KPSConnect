package org.xdevs23.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class DownloadUtils {
    public static InputStream getInputStreamForConnection(String url) throws IOException {
        URL ur = new URL(url);
        URLConnection connection = ur.openConnection();

        connection.connect();

        return new BufferedInputStream(ur.openStream(), 8192);
    }
}