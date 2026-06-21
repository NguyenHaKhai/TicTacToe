package vgu.hihi.ttt.basic.settings;

import java.time.Duration;

public final class Constant {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 1234;
    public static final String DEFAULT_START = "1";
    public static final int THREAD_POOL_SIZE = 4;

    public static final int HUMAN_ID = 1;
    public static final int COMPUTER_ID = 2;
    public static final String HUMAN_SYMBOL = "X";
    public static final String COMPUTER_SYMBOL = "O";
    public static final String EMPTY_SYMBOL = "-";


    public static final Duration TOKEN_TTL = Duration.ofSeconds(10);
    public static final Duration CLEANUP_PERIOD = Duration.ofSeconds(5);
    public static final int MAX_BATCH_SIZE = 1_000;
    public static final int NONCE_BYTES = 24;
    public static final String HMAC_ALGORITHM = "HmacSHA256";

    private Constant() {
    }
}
