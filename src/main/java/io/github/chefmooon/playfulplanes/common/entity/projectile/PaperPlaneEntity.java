package io.github.chefmooon.playfulplanes.common.entity.projectile;

import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import io.github.chefmooon.playfulplanes.common.registry.ModDamageSources;
import io.github.chefmooon.playfulplanes.common.registry.ModEntityTypes;
import io.github.chefmooon.playfulplanes.common.registry.ModItems;
import io.github.chefmooon.playfulplanes.common.registry.ModSounds;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class PaperPlaneEntity extends PersistentProjectileEntity {
	private static final TrackedData<Byte> LOYALTY = DataTracker.registerData(PaperPlaneEntity.class, TrackedDataHandlerRegistry.BYTE);
	private static final TrackedData<Boolean> ENCHANTED = DataTracker.registerData(PaperPlaneEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final float DRAG_IN_WATER = 0.99F;
	private static final boolean DEFAULT_DEALT_DAMAGE = false;
	private boolean dealtDamage = false;
	public int returnTimer;
	public PaperPlaneEntity(EntityType<? extends PaperPlaneEntity> entityType, World world) {
		super(entityType, world);
	}

	public PaperPlaneEntity(World world, LivingEntity owner, ItemStack stack) {
		super(ModEntityTypes.PAPER_PLANE, owner, world, stack, (ItemStack)null);
		this.dataTracker.set(LOYALTY, this.getLoyalty(stack));
		this.dataTracker.set(ENCHANTED, stack.hasGlint());
	}

	public PaperPlaneEntity(World world, double x, double y, double z, ItemStack stack) {
		super(ModEntityTypes.PAPER_PLANE, x, y, z, world, stack, stack);
		this.dataTracker.set(LOYALTY, this.getLoyalty(stack));
		this.dataTracker.set(ENCHANTED, stack.hasGlint());
	}

	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(LOYALTY, (byte)0);
		builder.add(ENCHANTED, false);
	}

	public void tick() {
		if (this.inGroundTime > 4) {
			this.dealtDamage = true;
		}

		Entity entity = this.getOwner();
		int i = (Byte)this.dataTracker.get(LOYALTY);
		if (i > 0 && (this.dealtDamage || this.isNoClip()) && entity != null) {
			if (!this.isOwnerAlive()) {
				World world = this.getWorld();
				if (world instanceof ServerWorld serverWorld) {
					if (this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
						this.dropStack(serverWorld, this.asItemStack(), 0.1F);
					}
				}

				this.discard();
			} else {
				if (!(entity instanceof PlayerEntity) && this.getPos().distanceTo(entity.getEyePos()) < (double)entity.getWidth() + 1.0) {
					this.discard();
					return;
				}

				this.setNoClip(true);
				Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
				this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * (double)i, this.getZ());
				double d = 0.05 * (double)i;
				this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));
				if (this.returnTimer == 0) {
					this.playSound(ModSounds.ENTITY_PAPER_PLANE_RETURN, 10.0F, 1.0F);
				}

				++this.returnTimer;
			}
		}

		super.tick();
	}

	private boolean isOwnerAlive() {
		Entity entity = this.getOwner();
		if (entity != null && entity.isAlive()) {
			return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
		} else {
			return false;
		}
	}

	public boolean isEnchanted() {
		return (Boolean)this.dataTracker.get(ENCHANTED);
	}

	@Nullable
	protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
		return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
	}

	@Override
	protected void onHit(LivingEntity target) {
		PlayfulPlanes.LOGGER.info("PaperPlane HIT!");
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		Entity entity = entityHitResult.getEntity();
		float f = 8.0F;
		Entity owner = this.getOwner();
		World world = this.getWorld();
		DamageSource damageSource = ModDamageSources.paperPlane(world, this, (Entity)(owner == null ? this : owner));
		if (world instanceof ServerWorld serverWorld) {
			f = EnchantmentHelper.getDamage(serverWorld, this.getWeaponStack(), entity, damageSource, f);
		}

		this.dealtDamage = true;
		if (entity.sidedDamage(damageSource, f)) {
			World world2 = this.getWorld();
			if (world2 instanceof ServerWorld serverWorld) {
				EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource, this.getWeaponStack(), (item) -> {
					this.kill(serverWorld);
				});
			}

			if (entity instanceof LivingEntity livingEntity) {
				this.knockback(livingEntity, damageSource);
				this.onHit(livingEntity);
			}
		}

		this.deflect(ProjectileDeflection.SIMPLE, entity, this.getOwner(), false);
		this.setVelocity(this.getVelocity().multiply(0.02, 0.2, 0.02));
		this.playSound(ModSounds.ENTITY_PAPER_PLANE_HIT, 1.0F, 1.0F);
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		// TODO: add onBlockHit effects
	}

	protected void onBlockHitEnchantmentEffects(ServerWorld world, BlockHitResult blockHitResult, ItemStack weaponStack) {
		Vec3d vec3d = blockHitResult.getBlockPos().clampToWithin(blockHitResult.getPos());
		Entity entity = this.getOwner();
		LivingEntity ownerEntity;
		if (entity instanceof LivingEntity livingEntity) {
			ownerEntity = livingEntity;
		} else {
			ownerEntity = null;
		}

		EnchantmentHelper.onHitBlock(world, weaponStack, ownerEntity, this, (EquipmentSlot)null, vec3d, world.getBlockState(blockHitResult.getBlockPos()), (item) -> {
			this.kill(world);
		});
	}

	public ItemStack getWeaponStack() {
		return this.getItemStack();
	}

	protected boolean tryPickup(PlayerEntity player) {
		return super.tryPickup(player) || this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
	}

	@Override
	protected ItemStack getDefaultItemStack() {
		return new ItemStack(ModItems.WHITE_PAPER_PLANE);
	}

	protected SoundEvent getHitSound() {
		return ModSounds.ENTITY_PAPER_PLANE_HIT_GROUND;
	}

	public void onPlayerCollision(PlayerEntity player) {
		if (this.isOwner(player) || this.getOwner() == null) {
			super.onPlayerCollision(player);
		}
	}

	protected void readCustomData(ReadView view) {
		super.readCustomData(view);
		this.dealtDamage = view.getBoolean("DealtDamage", false);
		this.dataTracker.set(LOYALTY, this.getLoyalty(this.getItemStack()));
	}

	protected void writeCustomData(WriteView view) {
		super.writeCustomData(view);
		view.putBoolean("DealtDamage", this.dealtDamage);
	}

	private byte getLoyalty(ItemStack stack) {
		World world = this.getWorld();
		if (world instanceof ServerWorld serverWorld) {
			// TODO: create getPaperPlaneReturnAcceleration method?
			return (byte) MathHelper.clamp(EnchantmentHelper.getTridentReturnAcceleration(serverWorld, stack, this), 0, 127);
		} else {
			return 0;
		}
	}

	public void age() {
		int i = (Byte)this.dataTracker.get(LOYALTY);
		if (this.pickupType != PersistentProjectileEntity.PickupPermission.ALLOWED || i <= 0) {
			super.age();
		}

	}

	protected float getDragInWater() {
		return DRAG_IN_WATER;
	}

	@Override
	public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
		return true;
	}
}
