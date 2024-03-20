package com.mrbysco.paperclippy.entity;

import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.clickevent.FightClickEvent;
import com.mrbysco.paperclippy.entity.goal.FollowPlayerGoal;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Paperclip extends PathfinderMob {
	protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(Paperclip.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Boolean> CRAFTING = SynchedEntityData.defineId(Paperclip.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<ItemStack> CRAFTING_RESULT = SynchedEntityData.defineId(Paperclip.class, EntityDataSerializers.ITEM_STACK);

	public float jumpAmount;
	public float jumpFactor;
	public float prevJumpFactor;
	private boolean wasOnGround;

	public int tipCooldown;
	private int lastHurtMessageTime;
	private final List<RecipeHolder<CraftingRecipe>> cachedRecipes = new ArrayList<>();

	public Paperclip(EntityType<? extends Paperclip> entityType, Level level) {
		super(entityType, level);
		this.moveControl = new PaperclipMovementController(this);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
		this.entityData.define(CRAFTING, false);
		this.entityData.define(CRAFTING_RESULT, ItemStack.EMPTY);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(1, new Paperclip.FloatGoal(this));
		this.goalSelector.addGoal(2, new Paperclip.PaperclipAttackGoal(this));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new FollowPlayerGoal(this, 1.0D, 2.0F, 10.0F));
		this.goalSelector.addGoal(4, new Paperclip.HopGoal(this));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.registerTargetGoals();
	}

	private void registerTargetGoals() {
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(Paperclip.class));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
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
		if (!this.dead && !this.level().isClientSide) {
			if (this.tipCooldown == 0) {
				this.tipCooldown = 200;
				LivingEntity owner = getOwner();
				if (owner instanceof Player player) {
					boolean recentlyAttacked = player.getLastHurtMob() != null && (player.tickCount - player.getLastHurtMobTimestamp()) < 200;
					if (recentlyAttacked && !(player.getLastHurtByMob() instanceof Paperclip)) {
						MutableComponent baseComponent = getBaseChatComponent();
						MutableComponent textComponent = Component.translatable("paperclippy.line.fighting").withStyle(ChatFormatting.WHITE);
						MutableComponent yesComponent = Component.literal("Yes");
						MutableComponent acceptComponent = Component.translatable("paperclippy.line.accept");
						yesComponent.setStyle(textComponent.getStyle()
								.withClickEvent(new FightClickEvent("/tellraw @a [\"\",{\"text\":\"" + getChatName() + "\",\"color\":\"yellow\"},{\"text\":\" " +
										acceptComponent.getString() + "\"}]", this)));
						yesComponent.withStyle(ChatFormatting.GREEN);
						MutableComponent betweenComponent = Component.literal(", ");
						MutableComponent noComponent = Component.literal("No");
						MutableComponent declineComponent = Component.translatable("paperclippy.line.decline");
						noComponent.setStyle(textComponent.getStyle()
								.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tellraw @a [\"\",{\"text\":\"" + getChatName() + "\",\"color\":\"yellow\"},{\"text\":\" " +
										declineComponent.getString() + "\"}]")));
						noComponent.withStyle(ChatFormatting.RED);
						baseComponent.append(textComponent).append(yesComponent).append(betweenComponent).append(noComponent);

						player.sendSystemMessage(baseComponent);
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
		if (this.isAlive() && target != null && target != this && target == entityIn) {
			if (this.distanceToSqr(entityIn) < 0.6D * 2 * 0.6D * 2 && this.hasLineOfSight(entityIn) && entityIn.hurt(damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
				this.playSound(PaperRegistry.PAPERCLIP_ATTACK.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
				this.doEnchantDamageEffects(this, entityIn);
			}
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		LivingEntity owner = getOwner();
		if (owner instanceof Player player && !this.level().isClientSide && (this.lastHurtMessageTime == 0 || (this.tickCount - this.lastHurtMessageTime) > 100)) {
			this.lastHurtMessageTime = this.tickCount;
			MutableComponent baseComponent = getBaseChatComponent();
			MutableComponent textComponent = Component.translatable("paperclippy.line.hurt").withStyle(ChatFormatting.WHITE);
			baseComponent.append(textComponent);
			player.sendSystemMessage(baseComponent);
		}
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		LivingEntity owner = getOwner();
		if (owner instanceof Player player && !this.level().isClientSide) {
			MutableComponent baseComponent = getBaseChatComponent();
			MutableComponent textComponent = Component.translatable("paperclippy.line.death").withStyle(ChatFormatting.WHITE);
			baseComponent.append(textComponent);
			player.sendSystemMessage(baseComponent);
		}
		return null;
	}

	public String getChatName() {
		return "<" + getName().getString() + ">";
	}

	public MutableComponent getBaseChatComponent() {
		return Component.literal(getChatName() + " ").withStyle(ChatFormatting.YELLOW);
	}

	@Nullable
	public UUID getOwnerId() {
		return this.entityData.get(OWNER_UNIQUE_ID).orElse((UUID) null);
	}

	public void setOwnerId(@Nullable UUID uuid) {
		this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
	}

	@Nullable
	public LivingEntity getOwner() {
		try {
			UUID uuid = this.getOwnerId();
			return uuid == null ? null : this.level().getPlayerByUUID(uuid);
		} catch (IllegalArgumentException illegalargumentexception) {
			return null;
		}
	}

	public ItemStack getCraftingResult() {
		return this.entityData.get(CRAFTING_RESULT);
	}

	public void setCraftingResult(ItemStack stack) {
		this.entityData.set(CRAFTING_RESULT, stack);
	}

	public boolean isCrafting() {
		return this.entityData.get(CRAFTING);
	}

	public void setCrafting(boolean crafting) {
		this.entityData.set(CRAFTING, crafting);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("wasOnGround", this.wasOnGround);
		compound.putInt("tipCooldown", this.tipCooldown);

		if (this.getOwnerId() != null) {
			compound.putUUID("Owner", this.getOwnerId());
		}

		ItemStack itemstack = getCraftingResult();
		if (!itemstack.isEmpty()) {
			compound.put("CraftResult", itemstack.save(new CompoundTag()));
		}

		compound.putBoolean("Crafting", isCrafting());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.wasOnGround = compound.getBoolean("wasOnGround");
		this.tipCooldown = compound.getInt("tipCooldown");

		UUID uuid;
		if (compound.hasUUID("Owner")) {
			uuid = compound.getUUID("Owner");
		} else {
			String s = compound.getString("Owner");
			uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
		}

		if (uuid != null) {
			this.setOwnerId(uuid);
		}

		ItemStack itemstack = ItemStack.of(compound.getCompound("CraftResult"));
		if (!itemstack.isEmpty()) {
			setCraftingResult(itemstack);
		}

		setCrafting(compound.getBoolean("Crafting"));
	}

	@Override
	public void tick() {
		if (this.tipCooldown > 0) {
			this.tipCooldown--;
		}

		this.jumpFactor += (this.jumpAmount - this.jumpFactor) * 0.5F;
		this.prevJumpFactor = this.jumpFactor;
		super.tick();

		if (this.onGround() && !this.wasOnGround) {
			int i = 2;
			for (int j = 0; j < i * 8; ++j) {
				float f = this.random.nextFloat() * ((float) Math.PI * 2F);
				float f1 = this.random.nextFloat() * 0.5F + 0.5F;
				float f2 = Mth.sin(f) * (float) i * 0.5F * f1;
				float f3 = Mth.cos(f) * (float) i * 0.5F * f1;
				ParticleOptions particleType = ParticleTypes.FIREWORK;
				double d0 = this.getX() + (double) f2;
				double d1 = this.getZ() + (double) f3;
				this.level().addParticle(particleType, d0, this.getBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D);
			}

			this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			this.jumpAmount = -0.5F;
		} else if (!this.onGround() && this.wasOnGround) {
			this.jumpAmount = 1.0F;
		}

		this.wasOnGround = this.onGround();
		this.alterJumpAmount();
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (!getCraftingResult().isEmpty()) {
			if (tickCount % 20 == 0) {
				List<RecipeHolder<CraftingRecipe>> recipes = getCraftingRecipes();
				List<ItemEntity> items = this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(2));
				setCrafting(!items.isEmpty());

				List<ItemStack> stacks = items.stream().map(ItemEntity::getItem).filter(stack -> !ItemStack.isSameItem(stack, getCraftingResult())).toList();
				if (items.isEmpty()) return;

				Container inventory = new SimpleContainer(stacks.toArray(new ItemStack[0]));
				for (RecipeHolder<CraftingRecipe> holder : recipes) {
					CraftingRecipe recipe = holder.value();
					if (recipe instanceof CustomRecipe) continue;

					NonNullList<Ingredient> ingredients = recipe.getIngredients();
					boolean isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
					StackedContents stackedcontents = new StackedContents();
					java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
					int i = 0;

					for (int j = 0; j < inventory.getContainerSize(); ++j) {
						ItemStack itemstack = inventory.getItem(j);
						if (!itemstack.isEmpty()) {
							++i;
							if (isSimple)
								stackedcontents.accountStack(itemstack, 1);
							else inputs.add(itemstack);
						}
					}

					if (i == ingredients.size() && (isSimple ? stackedcontents.canCraft(recipe, (IntList) null) : RecipeMatcher.findMatches(inputs, ingredients) != null)) {
						ItemStack result;
						try {
							result = recipe.assemble(null, this.level().registryAccess());
						} catch (Exception e) {
							result = recipe.getResultItem(this.level().registryAccess());
						}

						if (!result.isEmpty()) {
							for (ItemEntity item : items) {
								if (item.getItem().getCount() > 1) {
									ItemStack stack = item.getItem();
									stack.shrink(1);
									item.setItem(stack);
								} else {
									ItemStack stack = item.getItem();
									if (stack.getItem().hasCraftingRemainingItem()) {
										if (stack.is(Items.MILK_BUCKET) && random.nextDouble() < 0.3D) {
											Item bucket = Items.BUCKET;
											Optional<HolderSet.Named<Item>> oresTag = BuiltInRegistries.ITEM.getTag(PaperClippyMod.BUCKETS);
											if (oresTag.isPresent()) {
												HolderSet.Named<Item> tagSet = oresTag.get();
												Holder<Item> randomBucket = tagSet.getRandomElement(this.level().random).orElseGet(Items.WATER_BUCKET::builtInRegistryHolder);
												bucket = randomBucket.value();
											}
											item.setItem(new ItemStack(bucket));
										} else {
											item.setItem(stack.getCraftingRemainingItem().copy());
										}
									} else {
										item.discard();
									}
								}
							}

							ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), result);
							this.level().addFreshEntity(itementity);
						}
					}
				}
			}
		} else {
			if (isCrafting()) {
				setCrafting(false);
			}
		}
	}

	private List<RecipeHolder<CraftingRecipe>> getCraftingRecipes() {
		if (getCraftingResult().isEmpty()) {
			if (!cachedRecipes.isEmpty())
				cachedRecipes.clear();
			return new ArrayList<>();
		}
		if (cachedRecipes.isEmpty())
			cachedRecipes.addAll(this.level().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING).stream()
					.filter(recipeHolder -> ItemStack.isSameItem(recipeHolder.value().getResultItem(this.level().registryAccess()), getCraftingResult())).toList());
		return cachedRecipes;
	}

	protected void alterJumpAmount() {
		this.jumpAmount *= 0.6F;
	}

	protected void jumpFromGround() {
		Vec3 vec3d = this.getDeltaMovement();
		this.setDeltaMovement(vec3d.x, (double) 0.42F, vec3d.z);
		this.hasImpulse = true;
	}

	/**
	 * Gets the amount of time the clippy needs to wait between jumps.
	 */
	protected int getJumpDelay() {
		return this.random.nextInt(20) + 10;
	}

	protected SoundEvent getJumpSound() {
		return PaperRegistry.PAPERCLIP_BOING.get();
	}

	public Player getNearestPlayer(int range) {
		List<Player> list = getNearbyPlayers(range);
		return !list.isEmpty() ? list.get(0) : null;
	}

	public boolean isPlayerNearby(int range) {
		return !getNearbyPlayers(range).isEmpty();
	}

	private List<Player> getNearbyPlayers(int range) {
		AABB aabb = (new AABB(getX(), getY(), getZ(), getX() + 1, getY() + 1, getZ() + 1)).inflate(range);
		return this.level().getEntitiesOfClass(Player.class, aabb);
	}

	static class FloatGoal extends Goal {
		private final Paperclip paperclip;

		public FloatGoal(Paperclip paperclipIn) {
			this.paperclip = paperclipIn;
			this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
			paperclipIn.getNavigation().setCanFloat(true);
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			return (this.paperclip.isInWater() || this.paperclip.isInLava()) && this.paperclip.getMoveControl() instanceof Paperclip.PaperclipMovementController;
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			if (this.paperclip.getRandom().nextFloat() < 0.8F) {
				this.paperclip.getJumpControl().jump();
			}

			((Paperclip.PaperclipMovementController) this.paperclip.getMoveControl()).setSpeed(1.2D);
		}
	}

	static class HopGoal extends Goal {
		private final Paperclip paperclip;

		public HopGoal(Paperclip paperclipIn) {
			this.paperclip = paperclipIn;
			this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean canUse() {
			return !this.paperclip.isPassenger() && !this.paperclip.isCrafting();
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			((Paperclip.PaperclipMovementController) this.paperclip.getMoveControl()).setSpeed(1.0D);
		}
	}

	static class PaperclipMovementController extends MoveControl {
		private float yRot;
		private int jumpDelay;
		private final Paperclip paperclip;
		private boolean isAggressive;

		public PaperclipMovementController(Paperclip paperclipIn) {
			super(paperclipIn);
			this.paperclip = paperclipIn;
			this.yRot = 180.0F * paperclipIn.getYRot() / (float) Math.PI;
		}

		public void setDirection(float p_179920_1_, boolean p_179920_2_) {
			this.yRot = p_179920_1_;
			this.isAggressive = p_179920_2_;
		}

		public void setSpeed(double speedIn) {
			this.speedModifier = speedIn;
			this.operation = MoveControl.Operation.MOVE_TO;
		}

		public void tick() {
			this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
			this.mob.yHeadRot = this.mob.getYRot();
			this.mob.yBodyRot = this.mob.getYRot();

			if (this.paperclip.isCrafting()) return;

			if (this.operation != MoveControl.Operation.MOVE_TO) {
				this.mob.setZza(0.0F);
			} else {
				this.operation = MoveControl.Operation.WAIT;
				if (this.mob.onGround()) {
					this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));

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
					this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
				}
			}
		}
	}

	static class PaperclipAttackGoal extends Goal {
		private final Paperclip paperclip;
		private int growTieredTimer;

		public PaperclipAttackGoal(Paperclip paperclipIn) {
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
				return (!(LivingEntity instanceof Player) || !((Player) LivingEntity).getAbilities().invulnerable) && this.paperclip.getMoveControl() instanceof PaperclipMovementController;
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
			} else if (LivingEntity instanceof Player && ((Player) LivingEntity).getAbilities().invulnerable) {
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
			((PaperclipMovementController) this.paperclip.getMoveControl()).setDirection(this.paperclip.getYRot(), true);
		}
	}
}
