package xyz.breadloaf.imguimc.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.breadloaf.imguimc.imguiInternal.WindowScaling;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @WrapOperation(method = "getProjectionMatrix", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;perspective(FFFF)Lorg/joml/Matrix4f;", remap = false))
    public Matrix4f overridePerspectiveProjection(Matrix4f instance, float fovy, float aspect, float zNear, float zFar, Operation<Matrix4f> original) {
        return original.call(instance, fovy, (float) WindowScaling.WIDTH/WindowScaling.HEIGHT, zNear, zFar);
    }

}