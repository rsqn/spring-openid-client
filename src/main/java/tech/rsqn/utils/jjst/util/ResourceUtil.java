package tech.rsqn.utils.jjst.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ResourceUtil {
    private static Logger log = LoggerFactory.getLogger(ResourceUtil.class);

    public static final String loadContentFromResource(String path) throws IOException {

        log.info("Loading {} from classpath", path);

        InputStream inputStream = ResourceUtil.class.getResourceAsStream(path);

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }

    }
}
