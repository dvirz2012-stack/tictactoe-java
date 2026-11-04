package util;

public final class MathExtension {
    private MathExtension() { }

    public static float lerp(float val1, float val2, float t01) {
        t01 = Math.min(t01, 1f);
        t01 = Math.max(t01, 0f);
        return (1-t01) * val1 + t01 * val2;
    }
}
