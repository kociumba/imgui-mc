package xyz.breadloaf.imguimc.mixin;

import com.mojang.blaze3d.shaders.ShaderType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.breadloaf.imguimc.imguiInternal.ImguiLoader;

import java.util.function.BiFunction;

@Mixin(value = RenderSystem.class, remap = false)
public class TailRenderMixin {
    @Inject(at = @At("HEAD"), method="flipFrame")
    private static void runTickTail(CallbackInfo ci) {
        Profiler.get().push("ImGui Render");
        ImguiLoader.onFrameRender();
        Profiler.get().pop();
    }

    @Inject(method = "initRenderer(JIZLjava/util/function/BiFunction;Z)V", at = @At("RETURN"))
    private static void onInitRenderer(long l, int i, boolean bl, BiFunction<ResourceLocation, ShaderType, String> biFunction, boolean bl2, CallbackInfo ci) {
        ImguiLoader.onGlfwInit(l);
    }
}
