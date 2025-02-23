package net.geforcemods.securitycraft.blockentities;

import net.geforcemods.securitycraft.SCContent;
import net.geforcemods.securitycraft.api.ICodebreakable;
import net.geforcemods.securitycraft.api.ILockable;
import net.geforcemods.securitycraft.api.Option;
import net.geforcemods.securitycraft.api.Option.BooleanOption;
import net.geforcemods.securitycraft.api.Option.DisabledOption;
import net.geforcemods.securitycraft.api.Option.IntOption;
import net.geforcemods.securitycraft.blocks.KeycardReaderBlock;
import net.geforcemods.securitycraft.inventory.KeycardReaderMenu;
import net.geforcemods.securitycraft.misc.ModuleType;
import net.geforcemods.securitycraft.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class KeycardReaderBlockEntity extends DisguisableBlockEntity implements MenuProvider, ILockable, ICodebreakable {
	private boolean[] acceptedLevels = {
			true, false, false, false, false
	};
	private int signature = 0;
	private BooleanOption sendMessage = new BooleanOption("sendMessage", true);
	private IntOption signalLength = new IntOption(this::getBlockPos, "signalLength", 60, 5, 400, 5, true); //20 seconds max
	private DisabledOption disabled = new DisabledOption(false);

	public KeycardReaderBlockEntity(BlockPos pos, BlockState state) {
		super(SCContent.KEYCARD_READER_BLOCK_ENTITY.get(), pos, state);
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);

		CompoundTag acceptedLevelsTag = new CompoundTag();

		for (int i = 1; i <= 5; i++) {
			acceptedLevelsTag.putBoolean("lvl" + i, acceptedLevels[i - 1]);
		}

		tag.put("acceptedLevels", acceptedLevelsTag);
		tag.putInt("signature", signature);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);

		//carry over old data
		if (tag.contains("passLV")) {
			boolean oldRequiresExactKeycard = false;
			int oldPassLV = tag.getInt("passLV") - 1; //old data was 1-indexed, new one is 0-indexed

			if (tag.contains("requiresExactKeycard"))
				oldRequiresExactKeycard = tag.getBoolean("requiresExactKeycard");

			for (int i = 0; i < 5; i++) {
				acceptedLevels[i] = oldRequiresExactKeycard ? i == oldPassLV : i >= oldPassLV;
			}
		}

		//don't try to load this data if it doesn't exist, otherwise everything will be "false"
		if (tag.contains("acceptedLevels", Tag.TAG_COMPOUND)) {
			CompoundTag acceptedLevelsTag = tag.getCompound("acceptedLevels");

			for (int i = 1; i <= 5; i++) {
				acceptedLevels[i - 1] = acceptedLevelsTag.getBoolean("lvl" + i);
			}
		}

		signature = tag.getInt("signature");
	}

	@Override
	public boolean shouldAttemptCodebreak(BlockState state, Player player) {
		if (isDisabled()) {
			player.displayClientMessage(Utils.localize("gui.securitycraft:scManual.disabled"), true);
			return false;
		}

		return !state.getValue(KeycardReaderBlock.POWERED);
	}

	@Override
	public void useCodebreaker(BlockState state, Player player) {
		if (!level.isClientSide && getBlockState().getBlock() instanceof KeycardReaderBlock block)
			block.activate(level, worldPosition, signalLength.get());
	}

	public void setAcceptedLevels(boolean[] acceptedLevels) {
		this.acceptedLevels = acceptedLevels;
		setChanged();
	}

	public boolean[] getAcceptedLevels() {
		return acceptedLevels;
	}

	public void setSignature(int signature) {
		this.signature = signature;
		setChanged();
	}

	public int getSignature() {
		return signature;
	}

	@Override
	public ModuleType[] acceptedModules() {
		return new ModuleType[] {
				ModuleType.ALLOWLIST, ModuleType.DENYLIST, ModuleType.DISGUISE, ModuleType.SMART
		};
	}

	@Override
	public Option<?>[] customOptions() {
		return new Option[] {
				sendMessage, signalLength, disabled
		};
	}

	public boolean sendsMessages() {
		return sendMessage.get();
	}

	public int getSignalLength() {
		return signalLength.get();
	}

	public boolean isDisabled() {
		return disabled.get();
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
		return new KeycardReaderMenu(windowId, inv, level, worldPosition);
	}

	@Override
	public Component getDisplayName() {
		return super.getDisplayName();
	}
}
