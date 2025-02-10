package xyz.breadloaf.imguimc.imguiInternal;

import com.mojang.blaze3d.platform.Window;
import imgui.*;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
//import net.minecraft.util.profiling.Profiler;
import imgui.internal.ImGuiDockNode;
import xyz.breadloaf.imguimc.Imguimc;
import xyz.breadloaf.imguimc.icons.FontAwesomeIcons;
import xyz.breadloaf.imguimc.interfaces.Renderable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.glfw.GLFW.*;

public class ImguiLoader {
    private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();

    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private static long windowHandle;

    /**
     * overwrite to provide custom font loading
     */
    public static InitCallback initCallback;

    /**
     * overwrite to provide custom ini file name
     * <br>
     * <br>
     * this should be something like: "config/imgui/imgui-mc.ini"
     * to avoid any possible conflicts with other mods
     * <br>
     * <br>
     * if not set the settings will be saved to "config/imgui/imgui-mc.ini"
     * this can couse conflicts if the rare case of more than one mod using imgui-mc
     * with default settings occurs.
     * <br>
     * <br>
     * if the path you provided fails creation, the settings will not be saved,
     * until you provide a valid path
     */
    public static String iniFileName;

    public static void onGlfwInit(long handle) {
        initializeImGui(handle);
        imGuiGlfw.init(handle, true);
        imGuiGl3.init();
        windowHandle = handle;
    }

    public static void onFrameRender() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        setupDocking();

        //user render code

        for (Renderable renderable : Imguimc.renderstack) {
//            Profiler.get().push("ImGui Render/"+renderable.getName());
            renderable.getTheme().preRender();
            renderable.render();
            renderable.getTheme().postRender();
//            Profiler.get().pop();
        }

        for (Renderable renderable : Imguimc.toAdd) {
            Imguimc.pushRenderable(renderable);
        }
        Imguimc.toAdd.clear();

        for (Renderable renderable : Imguimc.toRemove) {
            Imguimc.pullRenderable(renderable);
        }
        Imguimc.toRemove.clear();

        //end of user code

        finishDocking();

        ImGui.render();
        endFrame(windowHandle);
    }

    private static void setupDocking() {
        int windowFlags = ImGuiWindowFlags.NoDocking;

        Window window = Imguimc.MINECRAFT.getWindow();

        ImGui.setNextWindowPos(window.getX(), window.getY(), ImGuiCond.Always);
        ImGui.setNextWindowSize(window.getWidth(), window.getHeight());
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus | ImGuiWindowFlags.NoBackground |
                ImGuiWindowFlags.NoNavInputs;

        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0, 0);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0);
        ImGui.begin("imgui-mc docking host window", windowFlags);
        ImGui.popStyleVar(2);

        int id = ImGui.dockSpace(Imguimc.getDockId(), 0, 0, ImGuiDockNodeFlags.PassthruCentralNode |
                ImGuiDockNodeFlags.NoCentralNode | ImGuiDockNodeFlags.NoDockingInCentralNode);

        ImGuiDockNode centre = imgui.internal.ImGui.dockBuilderGetCentralNode(id);
        WindowScaling.X_OFFSET = (int) centre.getPosX() - window.getX();
        WindowScaling.Y_OFFSET = (int) centre.getPosY() - window.getY();
        WindowScaling.Y_TOP_OFFSET = (int) (window.getHeight() - ((centre.getPosY() - window.getY()) + centre.getSizeY()));
        WindowScaling.WIDTH = (int) centre.getSizeX();
        WindowScaling.HEIGHT = (int) centre.getSizeY();
        WindowScaling.update();
    }

    private static void finishDocking() {
        ImGui.end();
    }

    private static void initializeImGui(long glHandle) {
        ImGui.createContext();

        final ImGuiIO io = ImGui.getIO();

        // check if the user provided a path
        iniFileName = iniFileName != null ? iniFileName : "config/imgui/imgui-mc.ini";
        if (!Files.exists(Paths.get(iniFileName).getParent())) {
            try {
                Files.createDirectories(Paths.get(iniFileName).getParent());
                io.setIniFilename(iniFileName); // We save the .ini to a user defined location [NEW]
            } catch (Exception e) {
                Imguimc.LOGGER.error("Failed to create directory for {}", iniFileName, e);
                io.setIniFilename(null);
            }
        }
//        io.setIniFilename(null);                             // We don't want to save .ini file [OLD]
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);     // Enable Docking
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);   // Enable Multi-Viewport / Platform Windows
        io.setConfigViewportsNoTaskBarIcon(true);

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

//        fontAtlas.setFreeTypeRenderer(true); // can't access FreeType, couse it's in versions 1.87+, which seem to be incompatible

        fontAtlas.addFontDefault();

        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder(); // Glyphs ranges provide
        rangesBuilder.addRanges(fontAtlas.getGlyphRangesDefault());
        rangesBuilder.addRanges(fontAtlas.getGlyphRangesCyrillic());
        rangesBuilder.addRanges(fontAtlas.getGlyphRangesJapanese());
        rangesBuilder.addRanges(FontAwesomeIcons._IconRange);

        fontConfig.setMergeMode(true); // When enabled, all fonts added with this config would be merged with the previously added font
//        fontConfig.setPixelSnapH(true);

        final short[] glyphRanges = rangesBuilder.buildRanges();
        fontAtlas.addFontFromMemoryTTF(loadFromResources("/fonts/icons/fa-regular-400.ttf"), 14, fontConfig, glyphRanges);
        fontAtlas.addFontFromMemoryTTF(loadFromResources("/fonts/icons/fa-solid-900.ttf"), 14, fontConfig, glyphRanges);

        // exposed callback here to allow for custom font loading
        if (initCallback != null) {
            initCallback.execute(io, fontAtlas, fontConfig, glyphRanges);
        }

        fontAtlas.build();
        fontConfig.destroy();

        if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final ImGuiStyle style = ImGui.getStyle();
            style.setWindowRounding(0.0f);
            style.setColor(ImGuiCol.WindowBg, ImGui.getColorU32(ImGuiCol.WindowBg, 1));
        }
    }

    private static void endFrame(long windowPtr) {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupWindowPtr);
        }

        //glfwSwapBuffers(windowPtr);
        //glfwPollEvents();
    }

    private static byte[] loadFromResources(String name) {
        try {
            return Files.readAllBytes(Paths.get(ImguiLoader.class.getResource(name).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
