package auctionsniper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.LogManager;

import static org.assertj.core.api.Assertions.assertThat;

class AuctionLogDriver {

    public static final String LOG_FILE_NAME = "auction-sniper.log";
    private final File logFile = new File(LOG_FILE_NAME);

    public void hasEntry(String message) throws IOException {
        assertThat(FileUtils.readFileToString(logFile, Charset.defaultCharset())).contains(message);
    }

    public void clearLog() {
        logFile.delete();
        LogManager.getLogManager().reset();
    }
}
