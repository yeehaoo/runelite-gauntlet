package ca.gauntlet.entity;

import java.awt.image.BufferedImage;
import lombok.Getter;
import net.runelite.api.GameObject;
import net.runelite.api.ObjectID;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.util.ImageUtil;

public class Resource
{
	@Getter
	private final GameObject gameObject;

	private final BufferedImage originalIcon;

	private BufferedImage icon;

	private int iconSize;

	public Resource(final GameObject gameObject, final SkillIconManager skillIconManager, final int iconSize)
	{
		this.gameObject = gameObject;
		this.iconSize = iconSize;

		this.originalIcon = getOriginalIcon(skillIconManager, gameObject.getId());
	}

	public void setIconSize(final int iconSize)
	{
		this.iconSize = iconSize;
		icon = ImageUtil.resizeImage(originalIcon, iconSize, iconSize);
	}

	public BufferedImage getIcon()
	{
		if (icon == null)
		{
			icon = ImageUtil.resizeImage(originalIcon, iconSize, iconSize);
		}

		return icon;
	}

	private static BufferedImage getOriginalIcon(final SkillIconManager skillIconManager, final int objectId)
	{
		switch (objectId)
		{
			case ObjectID.CRYSTAL_DEPOSIT:
			case ObjectID.CORRUPT_DEPOSIT:
				return skillIconManager.getSkillImage(Skill.MINING);
			case ObjectID.PHREN_ROOTS:
			case ObjectID.PHREN_ROOTS_36066:
				return skillIconManager.getSkillImage(Skill.WOODCUTTING);
			case ObjectID.FISHING_SPOT_36068:
			case ObjectID.FISHING_SPOT_35971:
				return skillIconManager.getSkillImage(Skill.FISHING);
			case ObjectID.GRYM_ROOT:
			case ObjectID.GRYM_ROOT_36070:
				return skillIconManager.getSkillImage(Skill.HERBLORE);
			case ObjectID.LINUM_TIRINUM:
			case ObjectID.LINUM_TIRINUM_36072:
				return skillIconManager.getSkillImage(Skill.FARMING);
			default:
				throw new IllegalArgumentException("Unsupported gauntlet resource gameobject id: " + objectId);
		}
	}
}
