package icebook;

public final class Utils {

    private Utils() {
        privateConstructor(getClass());
    }

    public static void privateConstructor(final Class<?> clazz) {
        notNull(clazz, "clazz");
        throw new AssertionError("No " + clazz + " instances for you!");
    }

    public static <T> T notNull(final T nullable, final String variableName) {
        if (null == nullable) {
            throw new NullPointerException((null == variableName ?
                    "notprovided" : variableName) + " cannot be null");
        }
        return nullable;
    }

    public static void checkArgument(final boolean condition, final String
            message) {
        notNull(message, "message");
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkState(final boolean condition, final String
            message) {
        notNull(message, "message");
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
