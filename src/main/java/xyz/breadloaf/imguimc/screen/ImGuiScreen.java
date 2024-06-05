package xyz.breadloaf.imguimc.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xyz.breadloaf.imguimc.Imguimc;

import java.util.List;

public class ImGuiScreen extends Screen {

    List<ImGuiWindow> windows;

    boolean closeWhenNoWindows;

    protected ImGuiScreen(Component component, boolean closeWhenNoWindows) {
        super(component);
        this.closeWhenNoWindows = closeWhenNoWindows;
    }

    protected List<ImGuiWindow> initImGui() {
        return List.of();
    }

    @Override
    protected void init() {
        super.init();
        windows = initImGui();
        for (ImGuiWindow window : windows) {
            Imguimc.pushRenderable(window);
        }
    }

    @Override
    public void onClose() {
        for (ImGuiWindow window : windows) {
            Imguimc.pullRenderable(window);
        }
        super.onClose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);

        if (closeWhenNoWindows) {
            boolean foundOpen = false;
            for (ImGuiWindow window : windows) {
                if (window.open.get()) {
                    foundOpen = true;
                }
            }
            if (!foundOpen)
                onClose();
        }
    }
}
