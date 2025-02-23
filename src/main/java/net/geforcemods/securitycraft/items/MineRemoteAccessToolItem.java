package net.geforcemods.securitycraft.items;

import java.util.List;

import net.geforcemods.securitycraft.ClientHandler;
import net.geforcemods.securitycraft.SCContent;
import net.geforcemods.securitycraft.SecurityCraft;
import net.geforcemods.securitycraft.api.IExplosive;
import net.geforcemods.securitycraft.api.IOwnable;
import net.geforcemods.securitycraft.network.client.UpdateNBTTagOnClient;
import net.geforcemods.securitycraft.util.BlockUtils;
import net.geforcemods.securitycraft.util.PlayerUtils;
import net.geforcemods.securitycraft.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public class MineRemoteAccessToolItem extends Item {
	public MineRemoteAccessToolItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (level.isClientSide)
			ClientHandler.displayMRATScreen(player.getItemInHand(hand));

		return InteractionResultHolder.consume(player.getItemInHand(hand));
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext ctx) {
		return onItemUseFirst(ctx.getPlayer(), ctx.getLevel(), ctx.getClickedPos(), stack, ctx.getClickedFace(), ctx.getClickLocation().x, ctx.getClickLocation().y, ctx.getClickLocation().z);
	}

	public InteractionResult onItemUseFirst(Player player, Level level, BlockPos pos, ItemStack stack, Direction facing, double hitX, double hitY, double hitZ) {
		if (level.getBlockState(pos).getBlock() instanceof IExplosive) {
			if (!isMineAdded(stack, pos)) {
				int availSlot = getNextAvailableSlot(stack);

				if (availSlot == 0) {
					PlayerUtils.sendMessageToPlayer(player, Utils.localize(SCContent.REMOTE_ACCESS_MINE.get().getDescriptionId()), Utils.localize("messages.securitycraft:mrat.noSlots"), ChatFormatting.RED);
					return InteractionResult.FAIL;
				}

				if (level.getBlockEntity(pos) instanceof IOwnable ownable && !ownable.isOwnedBy(player)) {
					if (level.isClientSide)
						ClientHandler.displayMRATScreen(stack);

					return InteractionResult.SUCCESS;
				}

				if (stack.getTag() == null)
					stack.setTag(new CompoundTag());

				stack.getTag().putIntArray(("mine" + availSlot), BlockUtils.posToIntArray(pos));

				if (!level.isClientSide && !stack.isEmpty())
					SecurityCraft.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new UpdateNBTTagOnClient(stack));

				PlayerUtils.sendMessageToPlayer(player, Utils.localize(SCContent.REMOTE_ACCESS_MINE.get().getDescriptionId()), Utils.localize("messages.securitycraft:mrat.bound", Utils.getFormattedCoordinates(pos)), ChatFormatting.GREEN);
				return InteractionResult.SUCCESS;
			}
			else {
				removeTagFromItemAndUpdate(stack, pos, player);
				PlayerUtils.sendMessageToPlayer(player, Utils.localize(SCContent.REMOTE_ACCESS_MINE.get().getDescriptionId()), Utils.localize("messages.securitycraft:mrat.unbound", Utils.getFormattedCoordinates(pos)), ChatFormatting.RED);
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		if (stack.getTag() == null)
			return;

		for (int i = 1; i <= 6; i++) {
			int[] coords = stack.getTag().getIntArray("mine" + i);

			if (coords.length != 3)
				list.add(Component.literal(ChatFormatting.GRAY + "---"));
			else
				list.add(Utils.localize("tooltip.securitycraft:mine").append(Component.literal(" " + i + ": ")).append(Utils.getFormattedCoordinates(new BlockPos(coords[0], coords[1], coords[2]))).setStyle(Utils.GRAY_STYLE));
		}
	}

	private void removeTagFromItemAndUpdate(ItemStack stack, BlockPos pos, Player player) {
		if (stack.getTag() == null)
			return;

		for (int i = 1; i <= 6; i++) {
			int[] coords = stack.getTag().getIntArray("mine" + i);

			if (coords.length == 3 && coords[0] == pos.getX() && coords[1] == pos.getY() && coords[2] == pos.getZ()) {
				stack.getTag().remove("mine" + i);

				if (!player.level.isClientSide && !stack.isEmpty())
					SecurityCraft.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new UpdateNBTTagOnClient(stack));

				return;
			}
		}

		return;
	}

	private boolean isMineAdded(ItemStack stack, BlockPos pos) {
		if (stack.getTag() == null)
			return false;

		for (int i = 1; i <= 6; i++) {
			int[] coords = stack.getTag().getIntArray("mine" + i);

			if (coords.length == 3 && coords[0] == pos.getX() && coords[1] == pos.getY() && coords[2] == pos.getZ())
				return true;
		}

		return false;
	}

	private int getNextAvailableSlot(ItemStack stack) {
		if (stack.getTag() == null)
			return 1;

		for (int i = 1; i <= 6; i++) {
			if (stack.getTag().getIntArray("mine" + i).length != 3)
				return i;
		}

		return 0;
	}
}