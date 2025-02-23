package net.geforcemods.securitycraft.entity.camera;

import net.geforcemods.securitycraft.SCContent;
import net.geforcemods.securitycraft.SecurityCraft;
import net.geforcemods.securitycraft.api.IModuleInventory;
import net.geforcemods.securitycraft.blockentities.SecurityCameraBlockEntity;
import net.geforcemods.securitycraft.blocks.SecurityCameraBlock;
import net.geforcemods.securitycraft.misc.ModuleType;
import net.geforcemods.securitycraft.network.client.SetCameraView;
import net.geforcemods.securitycraft.network.server.GiveNightVision;
import net.geforcemods.securitycraft.network.server.SetCameraPowered;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.network.PacketDistributor;

public class SecurityCamera extends Entity {
	public int screenshotSoundCooldown = 0;
	protected int redstoneCooldown = 0;
	protected int toggleNightVisionCooldown = 0;
	private boolean shouldProvideNightVision = false;
	protected float zoomAmount = 1F;
	protected boolean zooming = false;

	public SecurityCamera(EntityType<SecurityCamera> type, Level level) {
		super(SCContent.SECURITY_CAMERA_ENTITY.get(), level);
		noPhysics = true;
	}

	public SecurityCamera(Level level, BlockPos pos) {
		this(SCContent.SECURITY_CAMERA_ENTITY.get(), level);

		if (!(level.getBlockEntity(pos) instanceof SecurityCameraBlockEntity cam)) {
			discard();
			return;
		}

		double x = pos.getX() + 0.5D;
		double y = pos.getY() + 0.5D;
		double z = pos.getZ() + 0.5D;

		if (cam.down)
			y += 0.25D;

		setPos(x, y, z);
		setInitialPitchYaw();
	}

	public SecurityCamera(Level level, BlockPos pos, SecurityCamera oldCamera) {
		this(level, pos);
		oldCamera.discard();
	}

	private void setInitialPitchYaw() {
		setXRot(30F);

		Direction facing = level.getBlockState(blockPosition()).getValue(SecurityCameraBlock.FACING);

		if (facing == Direction.NORTH)
			setYRot(180F);
		else if (facing == Direction.WEST)
			setYRot(90F);
		else if (facing == Direction.SOUTH)
			setYRot(0F);
		else if (facing == Direction.EAST)
			setYRot(270F);
		else if (facing == Direction.DOWN)
			setXRot(75F);
	}

	@Override
	protected boolean repositionEntityAfterLoad() {
		return false;
	}

	@Override
	public void tick() {
		if (level.isClientSide) {
			if (screenshotSoundCooldown > 0)
				screenshotSoundCooldown--;

			if (redstoneCooldown > 0)
				redstoneCooldown--;

			if (toggleNightVisionCooldown > 0)
				toggleNightVisionCooldown--;

			if (shouldProvideNightVision)
				SecurityCraft.channel.sendToServer(new GiveNightVision());
		}
		else if (level.getBlockState(blockPosition()).getBlock() != SCContent.SECURITY_CAMERA.get())
			discard();
	}

	public void toggleRedstonePower() {
		BlockPos pos = blockPosition();

		if (((IModuleInventory) level.getBlockEntity(pos)).isModuleEnabled(ModuleType.REDSTONE))
			SecurityCraft.channel.sendToServer(new SetCameraPowered(pos, !level.getBlockState(pos).getValue(SecurityCameraBlock.POWERED)));
	}

	public void toggleNightVision() {
		toggleNightVisionCooldown = 30;
		shouldProvideNightVision = !shouldProvideNightVision;
	}

	public float getZoomAmount() {
		return zoomAmount;
	}

	public boolean isCameraDown() {
		return level.getBlockEntity(blockPosition()) instanceof SecurityCameraBlockEntity cam && cam.down;
	}

	public void setRotation(float yaw, float pitch) {
		setRot(yaw, pitch);
	}

	@Override
	public void remove(RemovalReason pReason) {
		super.remove(pReason);
		discardCamera();
	}

	public void stopViewing(ServerPlayer player) {
		if (!level.isClientSide) {
			discard();
			player.camera = player;
			SecurityCraft.channel.send(PacketDistributor.PLAYER.with(() -> player), new SetCameraView(player));
		}
	}

	/**
	 * @deprecated Prefer calling {@link #discard()}
	 */
	@Deprecated
	public void discardCamera() {
		if (!level.isClientSide) {
			if (level.getBlockEntity(blockPosition()) instanceof SecurityCameraBlockEntity camBe)
				camBe.stopViewing();

			SectionPos chunkPos = SectionPos.of(blockPosition());
			int chunkLoadingDistance = CameraController.getChunkLoadingDistance() <= 0 ? level.getServer().getPlayerList().getViewDistance() : CameraController.getChunkLoadingDistance();

			for (int x = chunkPos.getX() - chunkLoadingDistance; x <= chunkPos.getX() + chunkLoadingDistance; x++) {
				for (int z = chunkPos.getZ() - chunkLoadingDistance; z <= chunkPos.getZ() + chunkLoadingDistance; z++) {
					ForgeChunkManager.forceChunk((ServerLevel) level, SecurityCraft.MODID, this, x, z, false, false);
				}
			}
		}
	}

	@Override
	protected void defineSynchedData() {}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}

	@Override
	public boolean isAlwaysTicking() {
		return true;
	}
}
