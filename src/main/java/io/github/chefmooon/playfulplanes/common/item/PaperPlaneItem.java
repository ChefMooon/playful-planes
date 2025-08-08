package io.github.chefmooon.playfulplanes.common.item;

import io.github.chefmooon.playfulplanes.common.entity.projectile.PaperPlaneEntity;
import io.github.chefmooon.playfulplanes.common.registry.ModItems;
import io.github.chefmooon.playfulplanes.common.registry.ModSounds;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import java.util.List;

public class PaperPlaneItem extends Item implements ProjectileItem {
	public static final int MIN_DRAW_DURATION = 10;
	public static final float ATTACK_DAMAGE = 4.0F;
	public static final float THROW_SPEED = 2.5F;
	public PaperPlaneItem(Item.Settings settings) {
		super(settings);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((itemGroup) -> itemGroup.add(ModItems.WHITE_PAPER_PLANE));
	}

	public static AttributeModifiersComponent createAttributeModifiers() {
		return AttributeModifiersComponent.builder().add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 8.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, -2.9000000953674316, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
	}

	public static ToolComponent createToolComponent() {
		return new ToolComponent(List.of(), 1.0F, 2, false);
	}

	public UseAction getUseAction(ItemStack stack) {
		return UseAction.SPEAR; // TODO: add a custom use action for paper planes?
	}

	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return 46000;
	}

	public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof PlayerEntity playerEntity) {
			int useTicks = this.getMaxUseTime(stack, user) - remainingUseTicks;
			if (useTicks < 10) {
				return false;
			} else {
				if (stack.willBreakNextUse()) {
					return false;
				} else {
//					RegistryEntry<SoundEvent> registryEntry = (RegistryEntry)EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.TRIDENT_SOUND).orElse(SoundEvents.ITEM_TRIDENT_THROW);
					playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
					if (world instanceof ServerWorld serverWorld) {
						stack.damage(1, playerEntity);
						ItemStack itemStack = stack.splitUnlessCreative(1, playerEntity);
						PaperPlaneEntity paperPlaneEntity = (PaperPlaneEntity)ProjectileEntity.spawnWithVelocity(PaperPlaneEntity::new, serverWorld, itemStack, playerEntity, 0.0F, 1.5F, 0.5F);
						if (playerEntity.isInCreativeMode()) {
							paperPlaneEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
						}

						// TODO: does sound change based on enchantment?
//							world.playSoundFromEntity((Entity)null, paperPlaneEntity, (SoundEvent)registryEntry.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
						world.playSoundFromEntity((Entity)null, paperPlaneEntity, ModSounds.ENTITY_PAPER_PLANE_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
						return true;
					}
				}
			}
		} else {
			return false;
		}
		return false;
	}

	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (itemStack.willBreakNextUse()) {
			return ActionResult.FAIL;
		} else {
			user.setCurrentHand(hand);
			return ActionResult.CONSUME;
		}
	}

	@Override
	public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
		PaperPlaneEntity paperPlaneEntity = new PaperPlaneEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
		paperPlaneEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
		return paperPlaneEntity;
	}

//	@Override
//	public ActionResult use(World world, PlayerEntity user, Hand hand) {
//		ItemStack itemStack = user.getStackInHand(hand);
//		world.playSound((Entity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
//		if (world instanceof ServerWorld serverWorld) {
//			ProjectileEntity.spawnWithVelocity((world2, shooter, stack) -> new PaperPlaneEntityNew(user, world, user.getPos().getX(), user.getEyePos().getY(), user.getPos().getZ()), serverWorld, itemStack, user, 0.0F, 1.5F, 1.0F);
//		}
//
//		user.incrementStat(Stats.USED.getOrCreateStat(this));
//		itemStack.decrementUnlessCreative(1, user);
//		return ActionResult.SUCCESS;
//	}

//	@Override
//	public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
//		Random random = world.getRandom();
//		double d = random.nextTriangular((double)direction.getOffsetX(), 0.11485000000000001);
//		double e = random.nextTriangular((double)direction.getOffsetY(), 0.11485000000000001);
//		double f = random.nextTriangular((double)direction.getOffsetZ(), 0.11485000000000001);
//		Vec3d vec3d = new Vec3d(d, e, f);
//		PaperPlaneEntity paperPlaneEntity = new PaperPlaneEntity(world, pos.getX(), pos.getY(), pos.getZ(), vec3d.normalize());
//		paperPlaneEntity.setItem(stack);
//		return paperPlaneEntity;
//	}
}
