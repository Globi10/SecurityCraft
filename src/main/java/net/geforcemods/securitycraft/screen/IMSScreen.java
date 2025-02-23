package net.geforcemods.securitycraft.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.geforcemods.securitycraft.SCContent;
import net.geforcemods.securitycraft.SecurityCraft;
import net.geforcemods.securitycraft.blockentities.IMSBlockEntity;
import net.geforcemods.securitycraft.blockentities.IMSBlockEntity.IMSTargetingMode;
import net.geforcemods.securitycraft.network.server.SyncIMSTargetingOption;
import net.geforcemods.securitycraft.screen.components.ToggleComponentButton;
import net.geforcemods.securitycraft.util.Utils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class IMSScreen extends Screen {
	private static final ResourceLocation TEXTURE = new ResourceLocation("securitycraft:textures/gui/container/blank.png");
	private final Component imsName = Utils.localize(SCContent.IMS.get().getDescriptionId());
	private final Component target = Utils.localize("gui.securitycraft:ims.target");
	private int imageWidth = 176;
	private int imageHeight = 166;
	private int leftPos;
	private int topPos;
	private IMSBlockEntity be;
	private IMSTargetingMode targetMode;

	public IMSScreen(IMSBlockEntity be) {
		super(be.getDisplayName());
		this.be = be;
		targetMode = be.getTargetingMode();
	}

	@Override
	public void init() {
		super.init();

		leftPos = (width - imageWidth) / 2;
		topPos = (height - imageHeight) / 2;
		addRenderableWidget(new ToggleComponentButton(width / 2 - 75, height / 2 - 38, 150, 20, this::updateButtonText, targetMode.ordinal(), 3, this::modeButtonClicked));
	}

	@Override
	public void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
		renderBackground(pose);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem._setShaderTexture(0, TEXTURE);
		blit(pose, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		super.render(pose, mouseX, mouseY, partialTick);
		font.draw(pose, imsName, width / 2 - font.width(imsName) / 2, topPos + 6, 4210752);
		font.draw(pose, target, width / 2 - font.width(target) / 2, topPos + 30, 4210752);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (minecraft.options.keyInventory.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode))) {
			onClose();
			return true;
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void modeButtonClicked(Button button) {
		targetMode = IMSTargetingMode.values()[((ToggleComponentButton) button).getCurrentIndex()];
		be.setTargetingMode(targetMode);
		SecurityCraft.channel.sendToServer(new SyncIMSTargetingOption(be.getBlockPos(), be.getTargetingMode()));
	}

	private Component updateButtonText(int index) {
		return Utils.localize("gui.securitycraft:srat.targets" + ((index + 2) % 3 + 1));
	}
}
