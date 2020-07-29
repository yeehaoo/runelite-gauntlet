package ca.gauntlet.resource;

import ca.gauntlet.TheGauntletPlugin;
import java.awt.image.BufferedImage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.infobox.Counter;
import net.runelite.client.ui.overlay.infobox.InfoBoxPriority;

class ResourceCounter extends Counter
{
	private final Resource resource;

	private int count;
	private String text;

	ResourceCounter(final TheGauntletPlugin plugin, final Resource resource, final BufferedImage bufferedImage, final int count)
	{
		super(bufferedImage, plugin, count);

		this.resource = resource;
		this.count = count;
		this.text = String.valueOf(count);

		setPriority(getPriority(resource));
	}

	@Override
	public String getText()
	{
		return text;
	}

	@Subscribe
	void onResourceEvent(final ResourceEvent event)
	{
		if (resource != event.getResource())
		{
			return;
		}

		count = Math.max(0, count + event.getCount());
		text = String.valueOf(count);
	}

	private static InfoBoxPriority getPriority(final Resource resource)
	{
		switch (resource)
		{
			case CRYSTAL_ORE:
			case CORRUPTED_ORE:
			case PHREN_BARK:
			case CORRUPTED_PHREN_BARK:
			case LINUM_TIRINUM:
			case CORRUPTED_LINUM_TIRINUM:
				return InfoBoxPriority.HIGH;
			case GRYM_LEAF:
			case CORRUPTED_GRYM_LEAF:
				return InfoBoxPriority.MED;
			case CRYSTAL_SHARDS:
			case CORRUPTED_SHARDS:
			case RAW_PADDLEFISH:
				return InfoBoxPriority.NONE;
			default:
				return InfoBoxPriority.LOW;
		}
	}
}
