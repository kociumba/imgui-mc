package xyz.breadloaf.imguimc.imguiInternal;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.joml.Vector2d;

/**
 * Credit to this new version of the original fork: <a href="https://github.com/AlignedCookie88/imgui-mc/tree/1.21.3-1.1.0">1.21.3-1.1.0</a>
 */
public class WindowScaling {

    public static int X_OFFSET = 0;
    public static int Y_OFFSET = 0;
    public static int Y_TOP_OFFSET = 0;

    public static int WIDTH = 0;
    public static int HEIGHT = 0;

    public static boolean DISABLE_POST_PROCESSORS = false;

    private static Window getGameWindow() {
        return Minecraft.getInstance().getWindow();
    }

    public static Vector2d scalePoint(Vector2d point) {
        return scalePoint(point.x, point.y);
    }

    public static Vector2d scalePoint(double x, double y) {
        Window window = getGameWindow();

        float x_scale = (float) WIDTH / window.getScreenWidth();
        float y_scale = (float) HEIGHT / window.getScreenHeight();

        x *= x_scale;
        y *= y_scale;

        x += X_OFFSET;
        y += Y_OFFSET;

        return new Vector2d(x, y);
    }

    public static Vector2d unscalePoint(Vector2d point) {
        return unscalePoint(point.x, point.y);
    }

    public static Vector2d unscalePoint(double x, double y) {
        Window window = getGameWindow();

        float x_scale = (float) WIDTH / window.getScreenWidth();
        float y_scale = (float) HEIGHT / window.getScreenHeight();

        x -= X_OFFSET;
        y -= Y_OFFSET;

        x /= x_scale;
        y /= y_scale;

        return new Vector2d(x, y);
    }

    public static Vector2d scaleWidthHeight(double width, double height) {
        Window window = getGameWindow();

        float x_scale = (float) WIDTH / window.getScreenWidth();
        float y_scale = (float) HEIGHT / window.getScreenHeight();

        width *= x_scale;
        height *= y_scale;

        return new Vector2d(width, height);
    }

    public static Vector2d unscaleWidthHeight(double width, double height) {
        Window window = getGameWindow();

        float x_scale = (float) WIDTH / window.getScreenWidth();
        float y_scale = (float) HEIGHT / window.getScreenHeight();

        width /= x_scale;
        height /= y_scale;

        return new Vector2d(width, height);
    }

    public static boolean isChanged() {
        Window window = getGameWindow();
        return !(window.getWidth() == WIDTH && window.getHeight() == HEIGHT && X_OFFSET == 0 && Y_OFFSET == 0);
    }


    public static void update() {
        DISABLE_POST_PROCESSORS = isChanged();
    }

}