package com.mrbysco.paperclippy.entity.goal;

import com.mrbysco.paperclippy.entity.PaperclipEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.EnumSet;

public class FollowPlayerGoal extends Goal {
	protected final PaperclipEntity paperclip;
	private LivingEntity owner;
	protected final IWorldReader world;
	private final double followSpeed;
	private final PathNavigator navigator;
	private int timeToRecalcPath;
	private final float maxDist;
	private final float minDist;
	private float oldWaterCost;

	public FollowPlayerGoal(PaperclipEntity paperclipIn, double followSpeedIn, float minDistIn, float maxDistIn) {
		this.paperclip = paperclipIn;
		this.world = paperclipIn.level;
		this.followSpeed = followSpeedIn;
		this.navigator = paperclipIn.getNavigation();
		this.minDist = minDistIn;
		this.maxDist = maxDistIn;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		if (!(paperclipIn.getNavigation() instanceof GroundPathNavigator) && !(paperclipIn.getNavigation() instanceof FlyingPathNavigator)) {
			throw new IllegalArgumentException("Unsupported mob type for FollowplayerGoal");
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean canUse() {
		LivingEntity livingentity = this.paperclip.getOwner();
		if (livingentity == null) {
			return false;
		} else if (livingentity.isSpectator()) {
			return false;
		} else if (this.paperclip.distanceToSqr(livingentity) < (double)(this.minDist * this.minDist)) {
			return false;
		} else {
			this.owner = livingentity;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean canContinueToUse() {
		return !this.navigator.isDone() && !(this.paperclip.distanceToSqr(this.owner) <= (double)(this.maxDist * this.maxDist));
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		this.timeToRecalcPath = 0;
		this.oldWaterCost = this.paperclip.getPathfindingMalus(PathNodeType.WATER);
		this.paperclip.setPathfindingMalus(PathNodeType.WATER, 0.0F);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void stop() {
		this.owner = null;
		this.navigator.stop();
		this.paperclip.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		this.paperclip.getLookControl().setLookAt(this.owner, 10.0F, (float)this.paperclip.getMaxHeadXRot());
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = 10;
			if (!this.paperclip.isLeashed() && !this.paperclip.isPassenger()) {
				if (this.paperclip.distanceToSqr(this.owner) >= 144.0D) {
					this.tryToTeleportNearEntity();
				} else {
					this.navigator.moveTo(this.owner, this.followSpeed);
				}

			}
		}
	}
	private void tryToTeleportNearEntity() {
		BlockPos blockpos = this.owner.blockPosition();

		for(int i = 0; i < 10; ++i) {
			int j = this.getRandomNumber(-3, 3);
			int k = this.getRandomNumber(-1, 1);
			int l = this.getRandomNumber(-3, 3);
			boolean flag = this.tryToTeleportToLocation(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
			if (flag) {
				return;
			}
		}
	}

	private boolean tryToTeleportToLocation(int x, int y, int z) {
		if (Math.abs((double)x - this.owner.getX()) < 2.0D && Math.abs((double)z - this.owner.getZ()) < 2.0D) {
			return false;
		} else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
			return false;
		} else {
			this.paperclip.moveTo((double)x + 0.5D, (double)y, (double)z + 0.5D, this.paperclip.yRot, this.paperclip.xRot);
			this.navigator.stop();
			return true;
		}
	}

	private boolean isTeleportFriendlyBlock(BlockPos pos) {
		PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(this.world, pos.mutable());
		if (pathnodetype != PathNodeType.WALKABLE) {
			return false;
		} else {
			BlockState blockstate = this.world.getBlockState(pos.below());
			if (blockstate.getBlock() instanceof LeavesBlock) { //Don't teleport to leaves
				return false;
			} else {
				BlockPos blockpos = pos.subtract(this.paperclip.blockPosition());
				return this.world.noCollision(this.paperclip, this.paperclip.getBoundingBox().move(blockpos));
			}
		}
	}

	private int getRandomNumber(int min, int max) {
		return this.paperclip.getRandom().nextInt(max - min + 1) + min;
	}
}
