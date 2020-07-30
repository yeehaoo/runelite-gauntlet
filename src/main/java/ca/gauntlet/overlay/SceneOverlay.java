/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2020, dutta64 <https://github.com/dutta64>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ca.gauntlet.overlay;

import ca.gauntlet.TheGauntletConfig;
import ca.gauntlet.TheGauntletPlugin;
import ca.gauntlet.entity.Demiboss;
import ca.gauntlet.entity.Resource;
import static ca.gauntlet.overlay.Utility.drawOutlineAndFill;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

@Singleton
public class SceneOverlay extends Overlay
{
	private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

	private final Client client;
	private final TheGauntletPlugin plugin;
	private final TheGauntletConfig config;

	private Player player;

	@Inject
	public SceneOverlay(final Client client, final TheGauntletPlugin plugin, final TheGauntletConfig config)
	{
		super(plugin);

		this.client = client;
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGH);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	@Override
	public Dimension render(final Graphics2D graphics2D)
	{
		player = client.getLocalPlayer();

		if (player == null)
		{
			return null;
		}

		renderResources(graphics2D);
		renderUtilities(graphics2D);
		renderDemibosses(graphics2D);
		renderStrongNpcs(graphics2D);
		renderWeakNpcs(graphics2D);

		return null;
	}

	private void renderResources(final Graphics2D graphics2D)
	{
		if (!config.resourceOverlay() || plugin.getResources().isEmpty())
		{
			return;
		}

		final LocalPoint localPointPlayer = player.getLocalLocation();

		for (final Resource resource : plugin.getResources())
		{
			final GameObject gameObject = resource.getGameObject();

			final LocalPoint localPointGameObject = gameObject.getLocalLocation();

			if (isOutsideRenderDistance(localPointGameObject, localPointPlayer))
			{
				continue;
			}

			if (config.resourceOverlay())
			{
				final Polygon polygon = Perspective.getCanvasTilePoly(client, localPointGameObject);

				if (polygon == null)
				{
					continue;
				}

				drawOutlineAndFill(graphics2D, config.resourceTileOutlineColor(), config.resourceTileFillColor(),
					config.resourceTileOutlineWidth(), polygon);

				OverlayUtil.renderImageLocation(client, graphics2D, localPointGameObject, resource.getIcon(), 0);
			}
		}
	}

	private void renderUtilities(final Graphics2D graphics2D)
	{
		if (!config.utilitiesOutline() || plugin.getUtilities().isEmpty())
		{
			return;
		}

		final LocalPoint localPointPlayer = player.getLocalLocation();

		for (final GameObject gameObject : plugin.getUtilities())
		{
			if (isOutsideRenderDistance(gameObject.getLocalLocation(), localPointPlayer))
			{
				continue;
			}

			final Shape shape = gameObject.getConvexHull();

			if (shape == null)
			{
				continue;
			}

			drawOutlineAndFill(graphics2D, config.utilitiesOutlineColor(), TRANSPARENT, config.utilitiesOutlineWidth(), shape);
		}
	}

	private void renderDemibosses(final Graphics2D graphics2D)
	{
		if (!config.demibossOutline() || plugin.getDemibosses().isEmpty())
		{
			return;
		}

		final LocalPoint localPointPlayer = player.getLocalLocation();

		for (final Demiboss demiboss : plugin.getDemibosses())
		{
			final NPC npc = demiboss.getNpc();

			final LocalPoint localPointNpc = npc.getLocalLocation();

			if (localPointNpc == null || npc.isDead() || isOutsideRenderDistance(localPointNpc, localPointPlayer))
			{
				continue;
			}

			final Shape shape = npc.getConvexHull();

			if (shape == null)
			{
				continue;
			}

			drawOutlineAndFill(graphics2D, demiboss.getType().getOutlineColor(), TRANSPARENT, config.demibossOutlineWidth(), shape);
		}
	}

	private void renderStrongNpcs(final Graphics2D graphics2D)
	{
		if (!config.strongNpcOutline() || plugin.getStrongNpcs().isEmpty())
		{
			return;
		}

		final LocalPoint localPointPLayer = player.getLocalLocation();

		for (final NPC npc : plugin.getStrongNpcs())
		{
			final LocalPoint localPointNpc = npc.getLocalLocation();

			if (localPointNpc == null || npc.isDead() || isOutsideRenderDistance(localPointNpc, localPointPLayer))
			{
				continue;
			}

			final Shape shape = npc.getConvexHull();

			if (shape == null)
			{
				continue;
			}

			drawOutlineAndFill(graphics2D, config.strongNpcOutlineColor(), TRANSPARENT, config.strongNpcOutlineWidth(), shape);
		}
	}

	private void renderWeakNpcs(final Graphics2D graphics2D)
	{
		if (!config.weakNpcOutline() || plugin.getWeakNpcs().isEmpty())
		{
			return;
		}

		final LocalPoint localPointPlayer = player.getLocalLocation();

		for (final NPC npc : plugin.getWeakNpcs())
		{
			final LocalPoint localPointNpc = npc.getLocalLocation();

			if (localPointNpc == null || npc.isDead() || isOutsideRenderDistance(localPointNpc, localPointPlayer))
			{
				continue;
			}

			final Shape shape = npc.getConvexHull();

			if (shape == null)
			{
				continue;
			}

			drawOutlineAndFill(graphics2D, config.weakNpcOutlineColor(), TRANSPARENT, config.weakNpcOutlineWidth(), shape);
		}
	}

	private boolean isOutsideRenderDistance(final LocalPoint localPoint, final LocalPoint playerLocation)
	{
		final int maxDistance = config.resourceRenderDistance().getDistance();

		if (maxDistance == 0)
		{
			return false;
		}

		return localPoint.distanceTo(playerLocation) >= maxDistance;
	}
}
