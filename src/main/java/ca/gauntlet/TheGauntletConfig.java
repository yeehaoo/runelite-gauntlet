package ca.gauntlet;

import java.awt.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup("thegauntlet")
public interface TheGauntletConfig extends Config
{
	// Sections

	@ConfigSection(
		name = "Resources",
		description = "Resources section.",
		position = 0,
		closedByDefault = true
	)
	String resourcesSection = "resources";

	@ConfigSection(
		name = "Utilities",
		description = "Utilities section.",
		position = 1,
		closedByDefault = true
	)
	String utilitiesSection = "utilities";

	@ConfigSection(
		name = "Npcs",
		description = "Npcs section.",
		position = 2,
		closedByDefault = true
	)
	String npcsSection = "npcs";

	@ConfigSection(
		name = "Timer",
		description = "Timer section.",
		position = 3,
		closedByDefault = true
	)
	String timerSection = "timer";

	@ConfigSection(
		name = "Other",
		description = "Other section.",
		position = 4,
		closedByDefault = true
	)
	String otherSection = "other";

	// Resources

	@ConfigItem(
		name = "Overlay resource icon and tile",
		description = "Overlay resources with a respective icon and tile outline.",
		position = 0,
		keyName = "resourceOverlay",
		section = "resources"
	)
	default boolean resourceOverlay()
	{
		return false;
	}

	@Range(
		min = 12,
		max = 64
	)
	@ConfigItem(
		name = "Icon size",
		description = "Change the size of the resource icons.",
		position = 1,
		keyName = "resourceIconSize",
		section = "resources"
	)
	@Units(Units.PIXELS)
	default int resourceIconSize()
	{
		return 14;
	}

	@Range(
		min = 1,
		max = 8
	)
	@ConfigItem(
		name = "Tile outline width",
		description = "Change the width of the resource tile outline.",
		position = 2,
		keyName = "resourceTileOutlineWidth",
		section = "resources"
	)
	@Units(Units.PIXELS)
	default int resourceTileOutlineWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		name = "Tile outline color",
		description = "Change the tile outline color of resources.",
		position = 3,
		keyName = "resourceTileOutlineColor",
		section = "resources"
	)
	default Color resourceTileOutlineColor()
	{
		return Color.YELLOW;
	}

	@Alpha
	@ConfigItem(
		name = "Tile fill color",
		description = "Change the tile fill color of resources.",
		position = 4,
		keyName = "resourceTileFillColor",
		section = "resources"
	)
	default Color resourceTileFillColor()
	{
		return new Color(255, 255, 255, 50);
	}

	@ConfigItem(
		name = "Track resources",
		description = "Track resources in counter infoboxes.",
		position = 5,
		keyName = "resourceTracker",
		section = "resources"
	)
	default boolean resourceTracker()
	{
		return false;
	}

	@ConfigItem(
		name = "Ore",
		description = "The desired number of ores to acquire.",
		position = 6,
		keyName = "resourceOre",
		section = "resources"
	)
	default int resourceOre()
	{
		return 3;
	}

	@ConfigItem(
		name = "Phren bark",
		description = "The desired number of phren barks to acquire.",
		position = 7,
		keyName = "resourceBark",
		section = "resources"
	)
	default int resourceBark()
	{
		return 3;
	}

	@ConfigItem(
		name = "Linum tirinum",
		description = "The desired number of linum tirinums to acquire.",
		position = 8,
		keyName = "resourceTirinum",
		section = "resources"
	)
	default int resourceTirinum()
	{
		return 3;
	}

	@ConfigItem(
		name = "Grym leaf",
		description = "The desired number of grym leaves to acquire.",
		position = 9,
		keyName = "resourceGrym",
		section = "resources"
	)
	default int resourceGrym()
	{
		return 2;
	}

	@ConfigItem(
		name = "Weapon frames",
		description = "The desired number of weapon frames to acquire.",
		position = 10,
		keyName = "resourceFrame",
		section = "resources"
	)
	default int resourceFrame()
	{
		return 2;
	}

	@ConfigItem(
		name = "Paddlefish",
		description = "The desired number of paddlefish to acquire.",
		position = 11,
		keyName = "resourcePaddlefish",
		section = "resources"
	)
	default int resourcePaddlefish()
	{
		return 20;
	}

	@ConfigItem(
		name = "Crystal shards",
		description = "The desired number of crystal shards to acquire.",
		position = 12,
		keyName = "resourceShard",
		section = "resources"
	)
	default int resourceShard()
	{
		return 320;
	}

	@ConfigItem(
		name = "Bowstring",
		description = "Whether or not to acquire the crystalline or corrupted bowstring.",
		position = 13,
		keyName = "resourceBowstring",
		section = "resources"
	)
	default boolean resourceBowstring()
	{
		return false;
	}

	@ConfigItem(
		name = "Spike",
		description = "Whether or not to acquire the crystal or corrupted spike.",
		position = 14,
		keyName = "resourceSpike",
		section = "resources"
	)
	default boolean resourceSpike()
	{
		return false;
	}

	@ConfigItem(
		name = "Orb",
		description = "Whether or not to acquire the crystal or corrupted orb.",
		position = 15,
		keyName = "resourceOrb",
		section = "resources"
	)
	default boolean resourceOrb()
	{
		return false;
	}

	// Utilities Section

	@ConfigItem(
		name = "Outline starting room utilities",
		description = "Outline various utilities in the starting room.",
		position = 0,
		keyName = "utilitiesOutline",
		section = "utilities"
	)
	default boolean utilitiesOutline()
	{
		return false;
	}

	@Range(
		min = 1,
		max = 12
	)
	@ConfigItem(
		name = "Outline width",
		description = "Change the width of the utilities outline.",
		position = 1,
		keyName = "utilitiesOutlineWidth",
		section = "utilities"
	)
	@Units(Units.PIXELS)
	default int utilitiesOutlineWidth()
	{
		return 2;
	}

	@Alpha
	@ConfigItem(
		name = "Outline color",
		description = "Change the color of the utilities outline.",
		position = 2,
		keyName = "utilitiesOutlineColor",
		section = "utilities"
	)
	default Color utilitiesOutlineColor()
	{
		return Color.MAGENTA;
	}

	// Npcs Section

	@ConfigItem(
		name = "Outline demi-bosses",
		description = "Overlay demi-bosses with a colored outline.",
		position = 0,
		keyName = "demibossOutline",
		section = "npcs"
	)
	default boolean demibossOutline()
	{
		return false;
	}

	@Range(
		min = 1,
		max = 12
	)
	@ConfigItem(
		name = "Outline width",
		description = "Change the width of the demi-boss outline.",
		position = 1,
		keyName = "demibossOutlineWidth",
		section = "npcs"
	)
	@Units(Units.PIXELS)
	default int demibossOutlineWidth()
	{
		return 2;
	}

	@ConfigItem(
		name = "Outline strong npcs",
		description = "Overlay strong npcs with a colored outline.",
		position = 2,
		keyName = "strongNpcOutline",
		section = "npcs"
	)
	default boolean strongNpcOutline()
	{
		return false;
	}

	@Range(
		min = 1,
		max = 12
	)
	@ConfigItem(
		name = "Outline width",
		description = "Change the width of the strong npcs outline.",
		position = 3,
		keyName = "strongNpcOutlineWidth",
		section = "npcs"
	)
	@Units(Units.PIXELS)
	default int strongNpcOutlineWidth()
	{
		return 2;
	}

	@Alpha
	@ConfigItem(
		name = "Outline color",
		description = "Change the outline color of strong npcs.",
		position = 4,
		keyName = "strongNpcOutlineColor",
		section = "npcs"
	)
	default Color strongNpcOutlineColor()
	{
		return Color.CYAN;
	}

	@ConfigItem(
		name = "Outline weak npcs",
		description = "Overlay weak npcs with a colored outline.",
		position = 5,
		keyName = "weakNpcOutline",
		section = "npcs"
	)
	default boolean weakNpcOutline()
	{
		return false;
	}

	@Range(
		min = 1,
		max = 12
	)
	@ConfigItem(
		name = "Outline width",
		description = "Change the width of the weak npcs outline.",
		position = 6,
		keyName = "weakNpcOutlineWidth",
		section = "npcs"
	)
	@Units(Units.PIXELS)
	default int weakNpcOutlineWidth()
	{
		return 2;
	}

	@Alpha
	@ConfigItem(
		name = "Outline color",
		description = "Change the outline color of weak npcs.",
		position = 7,
		keyName = "weakNpcOutlineColor",
		section = "npcs"
	)
	default Color weakNpcOutlineColor()
	{
		return Color.CYAN;
	}

	// Timer Section

	@ConfigItem(
		position = 0,
		keyName = "timerOverlay",
		name = "Overlay timer",
		description = "Display an overlay that tracks your gauntlet time.",
		section = "timer"
	)
	default boolean timerOverlay()
	{
		return false;
	}

	@ConfigItem(
		position = 1,
		keyName = "timerChatMessage",
		name = "Chat timer",
		description = "Display a chat message on-death with your gauntlet time.",
		section = "timer"
	)
	default boolean timerChatMessage()
	{
		return false;
	}

	// Other Section

	@ConfigItem(
		name = "Render distance",
		description = "Set render distance of various overlays.",
		position = 0,
		keyName = "resourceRenderDistance",
		section = "other"
	)
	default RenderDistance resourceRenderDistance()
	{
		return RenderDistance.FAR;
	}

	// Constants

	@Getter
	@AllArgsConstructor
	enum RenderDistance
	{
		SHORT("Short", 2350),
		MEDIUM("Medium", 3525),
		FAR("Far", 4700),
		UNCAPPED("Uncapped", 0);

		private final String name;
		private final int distance;

		@Override
		public String toString()
		{
			return name;
		}
	}
}
