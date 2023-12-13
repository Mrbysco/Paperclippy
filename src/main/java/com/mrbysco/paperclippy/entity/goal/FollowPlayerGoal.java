package com.mrbysco.paperclippy.entity.goal;

import com.mrbysco.paperclippy.entity.Paperclip;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

import java.util.EnumSet;

public class FollowPlayerGoal extends Goal {
	protected final Paperclip paperclip;
	private LivingEntity owner;
	protected final LevelReader world;
	private final double followSpeed;
	private final PathNavigation navigator;
	private int timeToRecalcPath;
	private final float maxDist;
	private final float minDist;
	private float oldWaterCost;

	public FollowPlayerGoal(Paperclip paperclipIn, double followSpeedIn, float minDistIn, float maxDistIn) {
		this.paperclip = paperclipIn;
		this.world = paperclipIn.level;
		this.followSpeed = followSpeedIn;
		this.navigator = paperclipIn.getNavigation();
		this.minDist = minDistIn;
		this.maxDist = maxDistIn;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		if (!(paperclipIn.getNavigation() instanceof GroundPathNavigation) && !(paperclipIn.getNavigation() instanceof FlyingPathNavigation)) {
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
		this.oldWaterCost = this.paperclip.getPathfindingMalus(BlockPathTypes.WATER);
		this.paperclip.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void stop() {
		this.owner = null;
		this.navigator.stop();
		this.paperclip.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
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
			this.paperclip.moveTo((double)x + 0.5D, (double)y, (double)z + 0.5D, this.paperclip.getYRot(), this.paperclip.getXRot());
			this.navigator.stop();
			return true;
		}
	}

	private boolean isTeleportFriendlyBlock(BlockPos pos) {
		BlockPathTypes pathnodetype = WalkNodeEvaluator.getBlockPathTypeStatic(this.world, pos.mutable());
		if (pathnodetype != BlockPathTypes.WALKABLE) {
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
