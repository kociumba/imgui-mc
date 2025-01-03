package xyz.breadloaf.imguimc.imguiInternal;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGuiIO;

/**
 * the callback used for loading fonts and other custom initialization
 */
@FunctionalInterface
public interface InitCallback {
    void execute(ImGuiIO io, ImFontAtlas fontAtlas, ImFontConfig fontConfig, short[] glyphRanges);
}