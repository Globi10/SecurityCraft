package net.geforcemods.securitycraft.items;

import net.geforcemods.securitycraft.SCContent;
import net.minecraft.block.Block;

public class ItemScannerDoor extends ItemSpecialDoor
{
	@Override
	public Block getDoorBlock()
	{
		return SCContent.scannerDoor;
	}
}