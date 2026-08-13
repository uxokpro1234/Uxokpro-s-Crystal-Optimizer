package uxokpro1234.optimizer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import uxokpro1234.optimizer.command.EnableOptimizerCommand;

public class CrystalOptimizer implements ClientModInitializer {


    /**
     * @Author uxokpro1234
     * Instant place for crystalz
     * 15.05.2023
     */

    public static MinecraftClient mc;

    @Override
    public void onInitializeClient() { // lambada, instant crystal place
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);

            if (state.getBlock() == Blocks.OBSIDIAN || state.getBlock() == Blocks.BEDROCK && heldItem.getItem() == Items.END_CRYSTAL) {
                EndCrystalEntity enderCrystal = EntityType.END_CRYSTAL.create(world);
                enderCrystal.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0);
                world.spawnEntity(enderCrystal);

                heldItem.decrement(1);

            }

            return false;
        });


        mc = MinecraftClient.getInstance();
        EnableOptimizerCommand command = new EnableOptimizerCommand();
        command.initializeToggleCommands();
    }
}