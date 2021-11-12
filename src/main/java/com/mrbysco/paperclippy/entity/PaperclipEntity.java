package com.mrbysco.paperclippy.entity;

import com.mrbysco.paperclippy.entity.goal.FollowPlayerGoal;
import com.mrbysco.paperclippy.event.FightClickEvent;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PaperclipEntity extends CreatureEntity {
	protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.defineId(PaperclipEntity.class, DataSerializers.OPTIONAL_UUID);

	public float jumpAmount;
	public float jumpFactor;
	public float prevJumpFactor;
	private boolean wasOnGround;

	private int tipCooldown;
	private int lastHurtMessageTime;

	public PaperclipEntity(EntityType<? extends PaperclipEntity> entityType, World worldIn) {
		super(entityType, worldIn);
		this.moveControl = new PaperclipMovementController(this);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(1, new PaperclipEntity.FloatGoal(this));
		this.goalSelector.addGoal(2, new PaperclipEntity.PaperclipAttackGoal(this));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(5, new PaperclipEntity.HopGoal(this));
		this.goalSelector.addGoal(4, new FollowPlayerGoal(this, 1.0D, 10.0F, 2.0F));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.registerTargetGoals();
	}

	private void registerTargetGoals() {
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(PaperclipEntity.class));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 16.0D)
				.add(Attributes.FOLLOW_RANGE, 30.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.2F)
				.add(Attributes.FOLLOW_RANGE, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.ARMOR, 2.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		if(!this.dead && !this.level.isClientSide) {
			if(this.tipCooldown == 0) {
				this.tipCooldown = 200;
				LivingEntity owner = getOwner();
				if(owner instanceof PlayerEntity) {
					PlayerEntity player = (PlayerEntity)owner;

					boolean recentlyAttacked = (player.tickCount - player.getLastHurtMobTimestamp()) < 200;
					boolean containerOpen = player.containerMenu.menuType != null;
					if(containerOpen) {
						ResourceLocation registryName = player.containerMenu.menuType.getRegistryName();
						if(registryName != null && registryName.toString().equals("minecraft:crafting")) {
							System.out.println(player.containerMenu.getItems());
						}
					}

					if(recentlyAttacked) {
						String name = getName().getString();
						IFormattableTextComponent baseComponent = getBaseChatComponent();
						IFormattableTextComponent textComponent = new TranslationTextComponent("paperclippy.line.fighting").withStyle(TextFormatting.WHITE);
						IFormattableTextComponent yesComponent = new StringTextComponent("Yes");
						yesComponent.setStyle(textComponent.getStyle()
								.withClickEvent(new FightClickEvent("/tellraw @a [\"\",{\"text\":\"" + getChatName() + "\",\"color\":\"yellow\"},{\"text\":\" " +
										I18n.get("paperclippy.line.accept") + "\\\"}]", this)));
						yesComponent.withStyle(TextFormatting.GREEN);
						IFormattableTextComponent betweenComponent = new StringTextComponent(", ");
						IFormattableTextComponent noComponent = new StringTextComponent("No");
						noComponent.setStyle(textComponent.getStyle()
								.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tellraw @a [\"\",{\"text\":\"" + getChatName() + "\",\"color\":\"yellow\"},{\"text\":\" " +
										I18n.get("paperclippy.line.decline") + "\"}]")));
						noComponent.withStyle(TextFormatting.RED);
						baseComponent.append(textComponent).append(yesComponent).append(betweenComponent).append(noComponent);

						player.sendMessage(baseComponent, Util.NIL_UUID);
					}
				}
			}
		}
		return null;
	}

	@Override
	protected void doPush(Entity entityIn) {
		super.doPush(entityIn);
		LivingEntity target = this.getTarget();
		if(this.isAlive() && target != null && target != this && target == entityIn) {
			if (this.distanceToSqr(entityIn) < 0.6D * 2 * 0.6D * 2 && this.canSee(entityIn) && entityIn.hurt(DamageSource.mobAttack(this), (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
				this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
				this.doEnchantDamageEffects(this, entityIn);
			}
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		LivingEntity owner = getOwner();
		if(owner instanceof PlayerEntity && !level.isClientSide && (this.lastHurtMessageTime == 0 || (this.tickCount - this.lastHurtMessageTime) > 100)) {
			this.lastHurtMessageTime = this.tickCount;
			PlayerEntity player = (PlayerEntity) owner;
			IFormattableTextComponent baseComponent = getBaseChatComponent();
			IFormattableTextComponent textComponent = new TranslationTextComponent("paperclippy.line.hurt").withStyle(TextFormatting.WHITE);
			baseComponent.append(textComponent);
			player.sendMessage(baseComponent, Util.NIL_UUID);
		}
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		LivingEntity owner = getOwner();
		if(owner instanceof PlayerEntity && !level.isClientSide) {
			PlayerEntity player = (PlayerEntity) owner;
			IFormattableTextComponent baseComponent = getBaseChatComponent();
			IFormattableTextComponent textComponent = new TranslationTextComponent("paperclippy.line.death").withStyle(TextFormatting.WHITE);
			baseComponent.append(textComponent);
			player.sendMessage(baseComponent, Util.NIL_UUID);
		}
		return null;
	}

	protected String getChatName() {
		return "<" + getName() + ">";
	}

	protected IFormattableTextComponent getBaseChatComponent() {
		return new StringTextComponent(getChatName() + " ").withStyle(TextFormatting.YELLOW);
	}

	@Nullable
	public UUID getOwnerId() {
		return this.entityData.get(OWNER_UNIQUE_ID).orElse((UUID)null);
	}

	public void setOwnerId(@Nullable UUID p_184754_1_) {
		this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
	}

	@Nullable
	public LivingEntity getOwner() {
		try {
			UUID uuid = this.getOwnerId();
			return uuid == null ? null : this.level.getPlayerByUUID(uuid);
		} catch (IllegalArgumentException illegalargumentexception) {
			return null;
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("wasOnGround", this.wasOnGround);
		compound.putInt("tipCooldown", this.tipCooldown);

		if (this.getOwnerId() != null) {
			compound.putUUID("Owner", this.getOwnerId());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.wasOnGround = compound.getBoolean("wasOnGround");
		this.tipCooldown = compound.getInt("tipCooldown");

		UUID uuid;
		if (compound.hasUUID("Owner")) {
			uuid = compound.getUUID("Owner");
		} else {
			String s = compound.getString("Owner");
			uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
		}

		if (uuid != null) {
			this.setOwnerId(uuid);
		}
	}

	@Override
	public void tick() {
		if(this.tipCooldown > 0) {
			this.tipCooldown--;
		}

		this.jumpFactor += (this.jumpAmount - this.jumpFactor) * 0.5F;
		this.prevJumpFactor = this.jumpFactor;
		super.tick();

		if (this.onGround && !this.wasOnGround) {
			int i = 2;
			for(int j = 0; j < i * 8; ++j) {
				float f = this.random.nextFloat() * ((float)Math.PI * 2F);
				float f1 = this.random.nextFloat() * 0.5F + 0.5F;
				float f2 = MathHelper.sin(f) * (float)i * 0.5F * f1;
				float f3 = MathHelper.cos(f) * (float)i * 0.5F * f1;
				World world = this.level;
				IParticleData iparticledata = ParticleTypes.FIREWORK;
				double d0 = this.getX() + (double)f2;
				double d1 = this.getZ() + (double)f3;
				world.addParticle(iparticledata, d0, this.getBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D);
			}

			this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			this.jumpAmount = -0.5F;
		} else if (!this.onGround && this.wasOnGround) {
			this.jumpAmount = 1.0F;
		}

		this.wasOnGround = this.onGround;
		this.alterJumpAmount();
	}

	protected void alterJumpAmount() {
		this.jumpAmount *= 0.6F;
	}

	protected void jumpFromGround() {
		Vector3d vec3d = this.getDeltaMovement();
		this.setDeltaMovement(vec3d.x, (double)0.42F, vec3d.z);
		this.hasImpulse = true;
	}

	/**
	 * Gets the amount of time the clippy needs to wait between jumps.
	 */
	protected int getJumpDelay() {
		return this.random.nextInt(20) + 10;
	}

	protected SoundEvent getJumpSound() {
		return PaperRegistry.boing.get();
	}

	public PlayerEntity getNearestPlayer(int range) {
		AxisAlignedBB axisalignedbb = (new AxisAlignedBB(getX(), getY(), getZ(), getX() + 1, getY() + 1, getZ() + 1)).inflate(range);
		List<PlayerEntity> list = level.getEntitiesOfClass(PlayerEntity.class, axisalignedbb);
		return !list.isEmpty() ? list.get(0) : null;
	}

	public boolean isPlayerNearby(int range) {
		AxisAlignedBB axisalignedbb = (new AxisAlignedBB(getX(), getY(), getZ(), getX() + 1, getY() + 1, getZ() + 1)).inflate(range);
		List<PlayerEntity> list = level.getEntitiesOfClass(PlayerEntity.class, axisalignedbb);
		return !list.isEmpty();
	}

	static class FloatGoal extends Goal {
		private final PaperclipEntity paperclip;

		public FloatGoal(PaperclipEntity paperclipIn) {
			this.paperclip = paperclipIn;
			this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
			paperclipIn.getNavigation().setCanFloat(true);
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			return (this.paperclip.isInWater() || this.paperclip.isInLava()) && this.paperclip.getMoveControl() instanceof PaperclipEntity.PaperclipMovementController;
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			if (this.paperclip.getRandom().nextFloat() < 0.8F) {
				this.paperclip.getJumpControl().jump();
			}

			((PaperclipEntity.PaperclipMovementController)this.paperclip.getMoveControl()).setSpeed(1.2D);
		}
	}

	static class HopGoal extends Goal {
		private final PaperclipEntity paperclip;

		public HopGoal(PaperclipEntity paperclipIn) {
			this.paperclip = paperclipIn;
			this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean canUse() {
			return true;
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			((PaperclipEntity.PaperclipMovementController)this.paperclip.getMoveControl()).setSpeed(1.0D);
		}
	}

	static class PaperclipMovementController extends MovementController {
		private float yRot;
		private int jumpDelay;
		private final PaperclipEntity paperclip;
		private boolean isAggressive;

		public PaperclipMovementController(PaperclipEntity paperclipIn) {
			super(paperclipIn);
			this.paperclip = paperclipIn;
			this.yRot = 180.0F * paperclipIn.yRot / (float)Math.PI;
		}

		public void setDirection(float p_179920_1_, boolean p_179920_2_) {
			this.yRot = p_179920_1_;
			this.isAggressive = p_179920_2_;
		}

		public void setSpeed(double speedIn) {
			this.speedModifier = speedIn;
			this.operation = MovementController.Action.MOVE_TO;
		}

		public void tick() {
			this.mob.yRot = this.rotlerp(this.mob.yRot, this.yRot, 90.0F);
			this.mob.yHeadRot = this.mob.yRot;
			this.mob.yBodyRot = this.mob.yRot;

			if (this.operation != MovementController.Action.MOVE_TO) {
				this.mob.setZza(0.0F);
			} else {
				this.operation = MovementController.Action.WAIT;

				if (this.mob.isOnGround()) {
					this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));

					if (this.jumpDelay-- <= 0) {
						this.jumpDelay = this.paperclip.getJumpDelay();

						if (this.isAggressive) {
							this.jumpDelay /= 3;
						}

						this.paperclip.getJumpControl().jump();

						this.paperclip.playSound(this.paperclip.getJumpSound(), 0.5F, ((this.paperclip.getRandom().nextFloat() - this.paperclip.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
					} else {
						this.paperclip.xxa = 0.0F;
						this.paperclip.zza = 0.0F;
						this.mob.setSpeed(0.0F);
					}
				} else {
					this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
				}
			}
		}
	}

	static class PaperclipAttackGoal extends Goal {
		private final PaperclipEntity paperclip;
		private int growTieredTimer;

		public PaperclipAttackGoal(PaperclipEntity paperclipIn) {
			this.paperclip = paperclipIn;
			this.setFlags(EnumSet.of(Goal.Flag.LOOK));
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean canUse() {
			LivingEntity LivingEntity = this.paperclip.getTarget();
			if (LivingEntity == null) {
				return false;
			} else if (!LivingEntity.isAlive()) {
				return false;
			} else {
				return (!(LivingEntity instanceof PlayerEntity) || !((PlayerEntity) LivingEntity).abilities.invulnerable) && this.paperclip.getMoveControl() instanceof PaperclipMovementController;
			}
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start() {
			this.growTieredTimer = 300;
			super.start();
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean canContinueToUse() {
			LivingEntity LivingEntity = this.paperclip.getTarget();

			if (LivingEntity == null) {
				return false;
			} else if (!LivingEntity.isAlive()) {
				return false;
			} else if (LivingEntity instanceof PlayerEntity && ((PlayerEntity)LivingEntity).abilities.invulnerable) {
				return false;
			} else {
				return --this.growTieredTimer > 0;
			}
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			this.paperclip.lookAt(this.paperclip.getTarget(), 10.0F, 10.0F);
			((PaperclipMovementController)this.paperclip.getMoveControl()).setDirection(this.paperclip.yRot, true);
		}
	}
}
