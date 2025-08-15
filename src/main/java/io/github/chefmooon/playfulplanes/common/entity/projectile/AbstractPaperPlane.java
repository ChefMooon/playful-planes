package io.github.chefmooon.playfulplanes.common.entity.projectile;

import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import io.github.chefmooon.playfulplanes.common.data.PaperPlaneComponent;
import io.github.chefmooon.playfulplanes.common.data.types.PaperPlaneType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SideShapeType;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.TintedParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface AbstractPaperPlane {
	// TODO: Remove logger calls in production code
	default void applyBlockOnHit(PaperPlaneComponent paperPlaneComponent, World world, Entity owner, BlockHitResult blockHitResult) {
		if (paperPlaneComponent.paperPlaneType() == PaperPlaneType.TORCH) {
			tryPlaceTorch(world, blockHitResult);
		} else if (paperPlaneComponent.paperPlaneType() == PaperPlaneType.POTION) {
			tryPlacePotionEffect(world, paperPlaneComponent, owner, blockHitResult);
		}
	}

	default void applyEntityOnHit(PaperPlaneComponent paperPlaneComponent, Entity entity, Entity owner, EntityHitResult entityHitResult) {
		if (paperPlaneComponent.paperPlaneType() == PaperPlaneType.TORCH) {
			tryIgniteEntity(entity, entityHitResult);
			PlayfulPlanes.LOGGER.info("Applied torch effect to entity: {}", entity.getName().getString());
		} else if (paperPlaneComponent.paperPlaneType() == PaperPlaneType.POTION) {
			// Handle potion effect application here
			PotionContentsComponent potionContents = paperPlaneComponent.potionContentsComponent().get();
			PlayfulPlanes.LOGGER.info("potionContents: {}", potionContents);
			if (paperPlaneComponent.potionContentsComponent().isPresent()) {
				potionContents.getEffects().forEach((effect) -> {
					if (entity instanceof LivingEntity livingEntity) {
						livingEntity.addStatusEffect(effect, owner);
					}
					PlayfulPlanes.LOGGER.info("Applied {} | {} effect to entity: {}", effect.getTranslationKey(), effect.getDuration(), entity.getName().getString());
				});
			} else {
				PlayfulPlanes.LOGGER.warn("No potion effects to apply to entity: {}", entity.getName().getString());
			}
		}
	}

	// Type: Torch
	// Hit Block
	default void tryPlaceTorch(World world, BlockHitResult blockHitResult) {
		if (world.isClient) return;

		Direction side = blockHitResult.getSide();
		BlockPos blockPos = blockHitResult.getBlockPos();
		BlockPos torchPos = blockPos.offset(side);
		BlockState blockState = world.getBlockState(blockPos);

		// TODO change to place fire, paper plane + fire charge
		if (!blockState.isAir() && world.getBlockState(torchPos).isAir()) {
			if (side == Direction.UP) {
				world.setBlockState(torchPos, Blocks.TORCH.getDefaultState(), 3);
			} else if (side.getAxis().isHorizontal() && blockState.isSideSolid(world, blockPos, side.getOpposite(), SideShapeType.CENTER)) {
				world.setBlockState(torchPos, Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, side), 3);
			}
		}
		PlayfulPlanes.LOGGER.info("Placed torch at block position: {} facing: {}", blockPos, side);
	}

	default void tryPlacePotionEffect(World world, PaperPlaneComponent paperPlaneComponent, Entity owner, BlockHitResult blockHitResult) {
		if (world.isClient) return;

		Direction side = blockHitResult.getSide();
		BlockPos blockPos = blockHitResult.getBlockPos();
		BlockPos effectPos = blockPos.offset(side);

		// TODO: Improve potion effect placement logic
		if (side.getAxis() == Direction.Axis.Y) {
			// Place potion effect cloud above/below the block
			if (world.getBlockState(effectPos).isAir() &&world instanceof ServerWorld serverWorld) spawnPotionEffectCloud(serverWorld, paperPlaneComponent, owner, effectPos, blockHitResult);
		} else {
			// Place potion effect cloud inside the block
			if (world instanceof ServerWorld serverWorld) spawnPotionEffectCloud(serverWorld, paperPlaneComponent, owner, blockPos, blockHitResult);
		}
	}

	private void spawnPotionEffectCloud(ServerWorld world, PaperPlaneComponent paperPlaneComponent, Entity owner, BlockPos pos, BlockHitResult blockHitResult) {
		if (paperPlaneComponent.potionContentsComponent().isPresent()) {
			AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(world, pos.getX(), pos.getY(), pos.getZ());
			if (owner instanceof LivingEntity livingEntity) areaEffectCloudEntity.setOwner(livingEntity);

			areaEffectCloudEntity.setRadius(1.5F);
			areaEffectCloudEntity.setRadiusOnUse(-0.5F);
			areaEffectCloudEntity.setDuration(200);
			areaEffectCloudEntity.setWaitTime(10);
			areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / (float)areaEffectCloudEntity.getDuration());
			areaEffectCloudEntity.setPotionContents(paperPlaneComponent.potionContentsComponent().get());
			world.spawnEntity(areaEffectCloudEntity);
		}
	}

	// Hit Entity
	default void tryIgniteEntity(Entity entity, EntityHitResult entityHitResult) {
		entity.setOnFireFor(2);
	}

	default ParticleEffect getParticleType(PaperPlaneComponent paperPlaneComponent) {
		PaperPlaneType paperPlaneType = paperPlaneComponent.paperPlaneType();
		if (paperPlaneType == PaperPlaneType.TORCH) {
			return ParticleTypes.SMOKE;
		} else if (paperPlaneType == PaperPlaneType.POTION) {
			if (paperPlaneComponent.potionContentsComponent().isPresent()) {
				// TODO: figure out how to get SimpleParticleType from potion effects
				return TintedParticleEffect.create(ParticleTypes.ENTITY_EFFECT, paperPlaneComponent.potionContentsComponent().get().getColor());
			} else {
				return ParticleTypes.BUBBLE; // Temp particle, needs to be dynamic based on potion
			}
		}
		return null;
	}
}
