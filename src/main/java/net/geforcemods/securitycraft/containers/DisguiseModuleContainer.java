package net.geforcemods.securitycraft.containers;

import net.geforcemods.securitycraft.SCContent;
import net.geforcemods.securitycraft.inventory.ModuleItemInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class DisguiseModuleContainer extends Container {

	private ModuleItemInventory inventory;

	public DisguiseModuleContainer(int windowId, PlayerInventory playerInventory, ModuleItemInventory moduleInventory) {
		super(SCContent.cTypeDisguiseModule, windowId);
		inventory = moduleInventory;
		addSlot(new AddonSlot(inventory, 0, 79, 20));

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for(int i = 0; i < 9; i++)
			addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		ItemStack slotStackCopy = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if(slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			slotStackCopy = slotStack.copy();

			if(index < inventory.size) {
				if(!mergeItemStack(slotStack, inventory.size, 37, true))
					return ItemStack.EMPTY;

				slot.onSlotChange(slotStack, slotStackCopy);
			}
			else if(index >= inventory.size)
				if(!mergeItemStack(slotStack, 0, inventory.size, false))
					return ItemStack.EMPTY;

			if(slotStack.getCount() == 0)
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();

			if(slotStack.getCount() == slotStackCopy.getCount())
				return ItemStack.EMPTY;

			slot.onTake(player, slotStack);
		}

		return slotStackCopy;
	}

	@Override
	public ItemStack slotClick(int slot, int dragType, ClickType clickType, PlayerEntity player)
	{
		if(slot >= 0 && getSlot(slot) != null && ((!player.getHeldItemMainhand().isEmpty() && getSlot(slot).getStack() == player.getHeldItemMainhand() && player.getHeldItemMainhand().getItem() == SCContent.DISGUISE_MODULE.get())))
			return ItemStack.EMPTY;

		return super.slotClick(slot, dragType, clickType, player);
	}

	@Override
	public boolean canInteractWith(PlayerEntity player) {
		return true;
	}

	public static class AddonSlot extends Slot {

		public AddonSlot(ModuleItemInventory inventory, int index, int xPos, int yPos) {
			super(inventory, index, xPos, yPos);
		}

		@Override
		public boolean isItemValid(ItemStack itemStack) {
			return itemStack.getItem() instanceof BlockItem;
		}

		@Override
		public int getSlotStackLimit() {
			return 1;
		}
	}

}
