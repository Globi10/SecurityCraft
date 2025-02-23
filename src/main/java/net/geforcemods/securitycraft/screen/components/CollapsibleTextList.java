package net.geforcemods.securitycraft.screen.components;

import java.util.List;
import java.util.function.BiPredicate;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.ScreenUtils;

public class CollapsibleTextList extends Button {
	private static final Component PLUS = Component.literal("+ ");
	private static final Component MINUS = Component.literal("- ");
	private final int threeDotsWidth = Minecraft.getInstance().font.width("...");
	private final int heightOpen;
	private final int textCutoff;
	private final Component originalDisplayString;
	private final List<? extends Component> textLines;
	private final List<Long> splitTextLineCount;
	private final BiPredicate<Integer, Integer> extraHoverCheck;
	private boolean open = true;
	private boolean isMessageTooLong = false;
	private int initialY = -1;

	public CollapsibleTextList(int xPos, int yPos, int width, Component displayString, List<? extends Component> textLines, OnPress onPress, BiPredicate<Integer, Integer> extraHoverCheck) {
		super(xPos, yPos, width, 12, displayString, onPress, s -> Component.empty());
		originalDisplayString = displayString;
		switchOpenStatus(); //properly sets the message as well
		textCutoff = width - 5;

		ImmutableList.Builder<Long> splitTextLineCountBuilder = new ImmutableList.Builder<>();
		Font font = Minecraft.getInstance().font;
		int amountOfLines = 0;

		for (Component line : textLines) {
			long count = font.getSplitter().splitLines(line, textCutoff, line.getStyle()).size();

			amountOfLines += count;
			splitTextLineCountBuilder.add(count);
		}

		this.textLines = textLines;
		splitTextLineCount = splitTextLineCountBuilder.build();
		heightOpen = height + amountOfLines * font.lineHeight + textLines.size() * 3;
		this.extraHoverCheck = extraHoverCheck;
	}

	@Override
	public void renderButton(PoseStack pose, int mouseX, int mouseY, float partialTick) {
		isHovered &= extraHoverCheck.test(mouseX, mouseY);

		Font font = Minecraft.getInstance().font;
		int v = getYImage(isHoveredOrFocused());
		int heightOffset = (height - 8) / 2;

		ScreenUtils.blitWithBorder(pose, WIDGETS_LOCATION, getX(), getY(), 0, 46 + v * 20, width, height, 200, 20, 2, 3, 2, 2, getBlitOffset());
		drawCenteredString(pose, font, getMessage(), getX() + font.width(getMessage()) / 2 + 3, getY() + heightOffset, getFGColor());

		if (open) {
			int renderedLines = 0;

			GuiComponent.fillGradient(pose, getX(), getY() + height, getX() + width, getY() + heightOpen, 0xC0101010, 0xD0101010, 0);

			for (int i = 0; i < textLines.size(); i++) {
				int textY = getY() + 2 + height + renderedLines * font.lineHeight + (i * 12);

				if (i > 0)
					GuiComponent.fillGradient(pose, getX() + 1, textY - 3, getX() + width - 2, textY - 2, 0xAAA0A0A0, 0xAAA0A0A0, getBlitOffset());

				font.drawWordWrap(textLines.get(i), getX() + 2, textY, textCutoff, getFGColor());
				renderedLines += splitTextLineCount.get(i) - 1;
			}
		}
	}

	public void renderLongMessageTooltip(PoseStack pose) {
		if (isMessageTooLong && isHoveredOrFocused()) {
			Screen currentScreen = Minecraft.getInstance().screen;

			if (currentScreen != null)
				currentScreen.renderTooltip(pose, originalDisplayString, getX() + 1, getY() + height + 2);
		}
	}

	@Override
	public void setMessage(Component message) {
		Font font = Minecraft.getInstance().font;
		int stringWidth = font.width(message);
		int cutoff = width - 6;

		if (stringWidth > cutoff && stringWidth > threeDotsWidth) {
			isMessageTooLong = true;
			message = Component.literal(font.substrByWidth(message, cutoff - threeDotsWidth).getString() + "...");
		}

		super.setMessage(message);
	}

	@Override
	public int getHeight() {
		return open ? heightOpen : height;
	}

	@Override
	public void onPress() {
		switchOpenStatus();
		super.onPress();
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY) {
		return isMouseOver(mouseX, mouseY);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return extraHoverCheck.test((int) mouseX, (int) mouseY) && super.isMouseOver(mouseX, mouseY);
	}

	@Override
	public void setY(int y) {
		if (initialY == -1)
			initialY = y;

		super.setY(y);
	}

	public void switchOpenStatus() {
		open = !open;
		setMessage((open ? MINUS : PLUS).copy().append(originalDisplayString));
	}

	public boolean isOpen() {
		return open;
	}

	public int getInitialY() {
		return initialY;
	}
}
