package com.mrbysco.paperclippy.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrbysco.paperclippy.entity.Paperclip;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PaperclipCommands {
	public static void initializeCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("paperclippy");

		root.requires((source) -> source.hasPermission(2))
				.then(Commands.literal("set_crafting")
						.then(Commands.argument("paperclip", EntityArgument.entity())
								.then(Commands.argument("result", ItemArgument.item(context))
										.executes(PaperclipCommands::setCrafting)
								)
						)
				)
				.then(Commands.literal("clear_crafting")
						.then(Commands.argument("paperclip", EntityArgument.entity())
								.executes(PaperclipCommands::clearCrafting)
						)
				)
				.then(Commands.literal("set_target")
						.then(Commands.argument("paperclip", EntityArgument.entity())
								.then(Commands.argument("target", EntityArgument.entity())
										.executes(PaperclipCommands::setTarget)
								)
						)
				)
				.then(Commands.literal("clear_target")
						.then(Commands.argument("paperclip", EntityArgument.entity())
								.executes(PaperclipCommands::clearTarget)
						)
				);

		dispatcher.register(root);
	}

	private static int setCrafting(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		final ServerLevel serverLevel = ctx.getSource().getLevel();
		final Entity entity = EntityArgument.getEntity(ctx, "paperclip");
		if (entity instanceof Paperclip paperclip) {
			List<Player> players = serverLevel.getNearbyPlayers(TargetingConditions.forNonCombat().range(10).ignoreLineOfSight(),
					paperclip, paperclip.getBoundingBox().inflate(10D));
			for (Player player : players) {
				player.displayClientMessage(Component.literal(paperclip.getChatName()).withStyle(ChatFormatting.YELLOW)
						.append(" ").append(Component.translatable("paperclippy.line.accept").withStyle(ChatFormatting.WHITE)), false);
			}
			final ItemInput result = ItemArgument.getItem(ctx, "result");
			paperclip.setCraftingResult(result.createItemStack(1, false));

		} else {
			ctx.getSource().sendFailure(Component.literal("The selected entity is not a Paperclip!"));
			return 0;
		}
		return 1;
	}

	private static int clearCrafting(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		final ServerLevel serverLevel = ctx.getSource().getLevel();
		final Entity entity = EntityArgument.getEntity(ctx, "paperclip");
		if (entity instanceof Paperclip paperclip) {
			List<Player> players = serverLevel.getNearbyPlayers(TargetingConditions.forNonCombat().range(10).ignoreLineOfSight(),
					paperclip, paperclip.getBoundingBox().inflate(10D));
			for (Player player : players) {
				player.displayClientMessage(Component.literal(paperclip.getChatName()).withStyle(ChatFormatting.YELLOW)
						.append(" ").append(Component.translatable("paperclippy.line.decline").withStyle(ChatFormatting.WHITE)), false);
			}
			paperclip.setCraftingResult(ItemStack.EMPTY);

		} else {
			ctx.getSource().sendFailure(Component.literal("The selected entity is not a Paperclip!"));
			return 0;
		}
		return 1;
	}

	private static int setTarget(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		final ServerLevel serverLevel = ctx.getSource().getLevel();
		final Entity entity = EntityArgument.getEntity(ctx, "paperclip");
		if (entity instanceof Paperclip paperclip) {
			final Entity target = EntityArgument.getEntity(ctx, "target");
			if (target != paperclip && target.isAlive() && target instanceof LivingEntity livingtarget) {
				paperclip.setTarget(livingtarget);
				List<Player> players = serverLevel.getNearbyPlayers(TargetingConditions.forNonCombat().range(10).ignoreLineOfSight(),
						paperclip, paperclip.getBoundingBox().inflate(10D));
				for (Player player : players) {
					player.displayClientMessage(Component.literal(paperclip.getChatName()).withStyle(ChatFormatting.YELLOW)
							.append(" ").append(Component.translatable("paperclippy.line.accept").withStyle(ChatFormatting.WHITE)), false);
				}
			} else {
				ctx.getSource().sendFailure(Component.literal("The target entity is invalid!"));
				return 0;
			}

		} else {
			ctx.getSource().sendFailure(Component.literal("The selected entity is not a Paperclip!"));
			return 0;
		}
		return 1;
	}

	private static int clearTarget(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		final ServerLevel serverLevel = ctx.getSource().getLevel();
		final Entity entity = EntityArgument.getEntity(ctx, "paperclip");
		if (entity instanceof Paperclip paperclip) {
			paperclip.setTarget(null);
			List<Player> players = serverLevel.getNearbyPlayers(TargetingConditions.forNonCombat().range(10).ignoreLineOfSight(),
					paperclip, paperclip.getBoundingBox().inflate(10D));
			for (Player player : players) {
				player.displayClientMessage(Component.literal(paperclip.getChatName()).withStyle(ChatFormatting.YELLOW)
						.append(" ").append(Component.translatable("paperclippy.line.accept").withStyle(ChatFormatting.WHITE)), false);
			}

		} else {
			ctx.getSource().sendFailure(Component.literal("The selected entity is not a Paperclip!"));
			return 0;
		}
		return 1;
	}
}
