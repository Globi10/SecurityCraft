package net.geforcemods.securitycraft.compat.waila;

import java.util.Optional;

import com.mojang.blaze3d.vertex.PoseStack;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IEventListener;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.ITooltipComponent;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaConstants;
import mcp.mobius.waila.api.WailaPlugin;
import mcp.mobius.waila.api.component.ItemComponent;
import me.shedaniel.math.Rectangle;
import net.geforcemods.securitycraft.ClientHandler;
import net.geforcemods.securitycraft.SecurityCraft;
import net.geforcemods.securitycraft.api.IModuleInventory;
import net.geforcemods.securitycraft.api.IOwnable;
import net.geforcemods.securitycraft.api.IPasswordProtected;
import net.geforcemods.securitycraft.blockentities.KeycardReaderBlockEntity;
import net.geforcemods.securitycraft.blocks.DisguisableBlock;
import net.geforcemods.securitycraft.compat.IOverlayDisplay;
import net.geforcemods.securitycraft.entity.sentry.Sentry;
import net.geforcemods.securitycraft.entity.sentry.Sentry.SentryMode;
import net.geforcemods.securitycraft.misc.ModuleType;
import net.geforcemods.securitycraft.util.PlayerUtils;
import net.geforcemods.securitycraft.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;

@WailaPlugin(id = SecurityCraft.MODID)
public class WTHITDataProvider extends WailaCompatConstants implements IWailaPlugin, IBlockComponentProvider, IEntityComponentProvider, IEventListener {
	public static final WTHITDataProvider INSTANCE = new WTHITDataProvider();

	@Override
	public void register(IRegistrar registrar) {
		registrar.addEventListener(INSTANCE);
		registrar.addSyncedConfig(SHOW_OWNER, true, true);
		registrar.addSyncedConfig(SHOW_MODULES, true, true);
		registrar.addSyncedConfig(SHOW_PASSWORDS, true, true);
		registrar.addSyncedConfig(SHOW_CUSTOM_NAME, true, true);
		registrar.addComponent((IBlockComponentProvider) INSTANCE, TooltipPosition.HEAD, IOverlayDisplay.class);
		registrar.addComponent((IBlockComponentProvider) INSTANCE, TooltipPosition.BODY, IOwnable.class);
		registrar.addComponent((IBlockComponentProvider) INSTANCE, TooltipPosition.TAIL, IOverlayDisplay.class);
		registrar.addIcon((IBlockComponentProvider) INSTANCE, IOverlayDisplay.class);
		registrar.addComponent((IEntityComponentProvider) INSTANCE, TooltipPosition.BODY, Sentry.class);
	}

	@Override
	public ITooltipComponent getIcon(IBlockAccessor data, IPluginConfig config) {
		ItemStack displayStack = ((IOverlayDisplay) data.getBlock()).getDisplayStack(data.getWorld(), data.getBlockState(), data.getPosition());

		if (displayStack != null)
			return new ItemComponent(displayStack);
		else
			return IBlockComponentProvider.super.getIcon(data, config);
	}

	@Override
	public void appendHead(ITooltip tooltip, IBlockAccessor data, IPluginConfig config) {
		ItemStack displayStack = ((IOverlayDisplay) data.getBlock()).getDisplayStack(data.getWorld(), data.getBlockState(), data.getPosition());

		if (displayStack != null)
			tooltip.setLine(WailaConstants.OBJECT_NAME_TAG, Component.translatable(displayStack.getDescriptionId()).setStyle(ITEM_NAME_STYLE));
	}

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor data, IPluginConfig config) {
		Block block = data.getBlock();
		boolean disguised = false;

		if (block instanceof DisguisableBlock) {
			Optional<BlockState> disguisedBlockState = DisguisableBlock.getDisguisedBlockState(data.getWorld(), data.getPosition());

			if (disguisedBlockState.isPresent()) {
				disguised = true;
				block = disguisedBlockState.get().getBlock();
			}
		}

		if (block instanceof IOverlayDisplay && !((IOverlayDisplay) block).shouldShowSCInfo(data.getWorld(), data.getBlockState(), data.getPosition()))
			return;

		BlockEntity be = data.getBlockEntity();

		//last part is a little cheaty to prevent owner info from being displayed on non-sc blocks
		if (config.getBoolean(SHOW_OWNER) && be instanceof IOwnable && Utils.getRegistryName(block).getNamespace().equals(SecurityCraft.MODID))
			tooltip.addLine(Utils.localize("waila.securitycraft:owner", PlayerUtils.getOwnerComponent(((IOwnable) be).getOwner())));

		if (disguised)
			return;

		//if the te is ownable, show modules only when it's owned, otherwise always show
		if (config.getBoolean(SHOW_MODULES) && be instanceof IModuleInventory && (!(be instanceof IOwnable) || ((IOwnable) be).isOwnedBy(data.getPlayer()))) {
			if (!((IModuleInventory) be).getInsertedModules().isEmpty())
				tooltip.addLine(Utils.localize("waila.securitycraft:equipped"));

			for (ModuleType module : ((IModuleInventory) be).getInsertedModules()) {
				tooltip.addLine(Component.literal("- ").append(Component.translatable(module.getTranslationKey())));
			}
		}

		if (config.getBoolean(SHOW_PASSWORDS) && be instanceof IPasswordProtected && !(be instanceof KeycardReaderBlockEntity) && ((IOwnable) be).isOwnedBy(data.getPlayer())) {
			String password = ((IPasswordProtected) be).getPassword();

			tooltip.addLine(Utils.localize("waila.securitycraft:password", (password != null && !password.isEmpty() ? password : Utils.localize("waila.securitycraft:password.notSet"))));
		}

		if (config.getBoolean(SHOW_CUSTOM_NAME) && be instanceof Nameable nameable && nameable.hasCustomName()) {
			Component text = nameable.getCustomName();
			Component name = text == null ? Component.empty() : text;

			tooltip.addLine(Utils.localize("waila.securitycraft:customName", name));
		}
	}

	@Override
	public void appendTail(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		ItemStack disguisedAs = ((IOverlayDisplay) accessor.getBlock()).getDisplayStack(accessor.getWorld(), accessor.getBlockState(), accessor.getPosition());

		if (disguisedAs != null)
			tooltip.setLine(WailaConstants.MOD_NAME_TAG, Component.literal(ModList.get().getModContainerById(Utils.getRegistryName(disguisedAs.getItem()).getNamespace()).get().getModInfo().getDisplayName()).setStyle(MOD_NAME_STYLE));
	}

	@Override
	public void appendBody(ITooltip tooltip, IEntityAccessor data, IPluginConfig config) {
		Entity entity = data.getEntity();

		if (entity instanceof Sentry sentry) {
			SentryMode mode = sentry.getMode();

			if (config.getBoolean(SHOW_OWNER))
				tooltip.addLine(Utils.localize("waila.securitycraft:owner", PlayerUtils.getOwnerComponent(sentry.getOwner())));

			if (config.getBoolean(SHOW_MODULES) && sentry.isOwnedBy(data.getPlayer())) {
				if (!sentry.getAllowlistModule().isEmpty() || !sentry.getDisguiseModule().isEmpty() || sentry.hasSpeedModule()) {
					tooltip.addLine(EQUIPPED);

					if (!sentry.getAllowlistModule().isEmpty())
						tooltip.addLine(ALLOWLIST_MODULE);

					if (!sentry.getDisguiseModule().isEmpty())
						tooltip.addLine(DISGUISE_MODULE);

					if (sentry.hasSpeedModule())
						tooltip.addLine(SPEED_MODULE);
				}
			}

			MutableComponent modeDescription = Utils.localize(mode.getModeKey());

			if (mode != SentryMode.IDLE)
				modeDescription.append("- ").append(Utils.localize(mode.getTargetKey()));

			tooltip.addLine(modeDescription);
		}
	}

	public void onBeforeTooltipRender(PoseStack pose, Rectangle rectangle, ICommonAccessor accessor, IPluginConfig config, Canceller canceller) {
		if (ClientHandler.isPlayerMountedOnCamera())
			canceller.cancel();
	}
}
