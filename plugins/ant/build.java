import static org.jboss.noxius.plugins.download.Downloader.download;

import java.io.File;

public class build {
    public static void main(final String[] args) throws Exception {
        new File("lib").mkdirs();

        download("http://repo1.maven.org/maven2/org/apache/ant/ant/1.8.2/ant-1.8.2.jar", "lib/ant-1.8.2.jar");
        download("http://repo1.maven.org/maven2/org/apache/ant/ant-launcher/1.8.2/ant-launcher-1.8.2.jar", "lib/ant-launcher-1.8.2.jar");

        download("http://repo1.maven.org/maven2/junit/junit/4.8.2/junit-4.8.2.jar", "lib/junit-4.8.2.jar");
    }
}
