package uxokpro1234.optimizer.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uxokpro1234.optimizer.command.EnableOptimizerCommand;

import static uxokpro1234.optimizer.CrystalOptimizer.mc;


/**
 * @Author uxokpro1234
 * Kills krystallz instantly.
 * 10.05.2023
 */

@Mixin(PlayerEntity.class)
public abstract class MixinEnderCrystalEntity {

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void onAttack(Entity target, CallbackInfo info) {

        if (EnableOptimizerCommand.fastCrystal && target.getType() == EntityType.END_CRYSTAL) {
            EndCrystalEntity crystal = (EndCrystalEntity) target;

            if (!crystal.isInvulnerable() && canKillC()) {

                mc.interactionManager.attackEntity(mc.player, crystal);
                mc.player.swingHand(Hand.MAIN_HAND);
                crystal.kill();
                crystal.setRemoved(Entity.RemovalReason.KILLED);
                crystal.onRemoved();
                info.cancel();
            }
        }
    }

    public boolean canKillC() {
        if (!mc.player.hasStatusEffect(StatusEffects.WEAKNESS)) {
            return true;
        }

        ItemStack stack = mc.player.getMainHandStack();

        Multimap<EntityAttribute, EntityAttributeModifier> modifiers =
                stack.getAttributeModifiers(EquipmentSlot.MAINHAND);

        double damage = 0.0;

        for (EntityAttributeModifier modifier :
                modifiers.get(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
            damage += modifier.getValue();
        }
        System.out.println(damage);
        return damage > 1.0;
    }
}