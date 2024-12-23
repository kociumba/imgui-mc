package xyz.breadloaf.imguimc.imguiInternal;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGuiIO;

/**
 * let's see if this works
 */
@FunctionalInterface
public interface InitCallback {
    void execute(ImGuiIO io, ImFontAtlas fontAtlas, ImFontConfig fontConfig);
}