package ca.gauntlet.overlay;

import ca.gauntlet.TheGauntletConfig;
import ca.gauntlet.TheGauntletPlugin;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.time.Instant;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.ChatMessageType;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

@Singleton
public class TimerOverlay extends OverlayPanel
{
	private final TheGauntletConfig config;
	private final ChatMessageManager chatMessageManager;

	private final PanelComponent timerComponent;
	private final LineComponent prepTimeComponent;
	private final LineComponent totalTimeComponent;

	private long timeGauntletStart;
	private long timeHunllefStart;

	@Inject
	TimerOverlay(final TheGauntletPlugin plugin, final TheGauntletConfig config, final ChatMessageManager chatMessageManager)
	{
		super(plugin);

		this.config = config;
		this.chatMessageManager = chatMessageManager;

		this.timerComponent = new PanelComponent();
		timerComponent.setBorder(new Rectangle(2, 1, 4, 0));
		timerComponent.setBackgroundColor(null);

		panelComponent.setBorder(new Rectangle(2, 2, 2, 2));
		panelComponent.getChildren().add(TitleComponent.builder().text("Gauntlet Timer").build());
		panelComponent.getChildren().add(timerComponent);

		prepTimeComponent = LineComponent.builder().left("Preparation:").right("").build();
		totalTimeComponent = LineComponent.builder().left("Total:").right("").build();

		this.timeGauntletStart = -1;
		this.timeHunllefStart = -1;

		setClearChildren(false);
		getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "The Gauntlet timer"));
		setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
		setPriority(OverlayPriority.HIGH);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	@Override
	public Dimension render(final Graphics2D graphics2D)
	{
		if (!config.timerOverlay() || timeGauntletStart == -1)
		{
			return null;
		}

		graphics2D.setFont(FontManager.getRunescapeSmallFont());

		final LineComponent lineComponent = timeHunllefStart == -1 ? prepTimeComponent : totalTimeComponent;

		lineComponent.setRight(calculateElapsedTime(Instant.now().getEpochSecond(), timeGauntletStart));

		return super.render(graphics2D);
	}

	public void reset()
	{
		timeGauntletStart = -1;
		timeHunllefStart = -1;
		prepTimeComponent.setRight("");
		totalTimeComponent.setRight("");
		timerComponent.getChildren().clear();
	}

	public void setGauntletStart()
	{
		timeGauntletStart = Instant.now().getEpochSecond();
		prepTimeComponent.setLeftColor(Color.WHITE);
		prepTimeComponent.setRightColor(Color.WHITE);
		timerComponent.getChildren().clear();
		timerComponent.getChildren().add(prepTimeComponent);
	}

	public void setHunllefStart()
	{
		timeHunllefStart = Instant.now().getEpochSecond();
		prepTimeComponent.setLeftColor(Color.LIGHT_GRAY);
		prepTimeComponent.setRightColor(Color.LIGHT_GRAY);
		timerComponent.getChildren().clear();
		timerComponent.getChildren().add(prepTimeComponent);
		timerComponent.getChildren().add(totalTimeComponent);
	}

	public void onPlayerDeath()
	{
		if (!config.timerChatMessage())
		{
			return;
		}

		printTime();
		reset();
	}

	private void printTime()
	{
		if (timeGauntletStart == -1 || timeHunllefStart == -1)
		{
			return;
		}

		final long current = Instant.now().getEpochSecond();

		final String elapsedPrepTime = calculateElapsedTime(timeHunllefStart, timeGauntletStart);
		final String elapsedBossTime = calculateElapsedTime(current, timeHunllefStart);
		final String elapsedTotalTime = calculateElapsedTime(current, timeGauntletStart);

		final ChatMessageBuilder chatMessageBuilder = new ChatMessageBuilder()
			.append(ChatColorType.NORMAL)
			.append("Preparation time: ")
			.append(ChatColorType.HIGHLIGHT)
			.append(elapsedPrepTime)
			.append(ChatColorType.NORMAL)
			.append(". Hunllef time: ")
			.append(ChatColorType.HIGHLIGHT)
			.append(elapsedBossTime)
			.append(ChatColorType.NORMAL)
			.append(". Total time: ")
			.append(ChatColorType.HIGHLIGHT)
			.append(elapsedTotalTime + ".");

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.CONSOLE)
			.runeLiteFormattedMessage(chatMessageBuilder.build())
			.build());
	}

	private static String calculateElapsedTime(final long end, final long start)
	{
		final long elapsed = end - start;

		final long minutes = elapsed % 3600 / 60;
		final long seconds = elapsed % 60;

		return String.format("%01d:%02d", minutes, seconds);
	}
}

