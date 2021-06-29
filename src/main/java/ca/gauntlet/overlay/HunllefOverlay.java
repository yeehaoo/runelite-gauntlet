/*
 * BSD 2-Clause License
 * 
 * Copyright (c) 2020, yeehaoo <https://github.com/yeehaoo>
 * Copyright (c) 2020, dutta64 <https://github.com/dutta64>
 * Copyright (c) 2019, kThisIsCvpv <https://github.com/kThisIsCvpv>
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * Copyright (c) 2019, kyle <https://github.com/Kyleeld>
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

package net.runelite.client.plugins.gauntlet.overlay;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Perspective;
import net.runelite.client.plugins.gauntlet.TheGauntletConfig;
import net.runelite.client.plugins.gauntlet.TheGauntletPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import java.awt.*;

import static net.runelite.client.plugins.gauntlet.overlay.Utility.drawOutlineAndFill;

public class HunllefOverlay extends Overlay
{
	private final Client client;
	private final TheGauntletPlugin plugin;
	private final TheGauntletConfig config;

	@Inject
	public HunllefOverlay(final Client client, final TheGauntletPlugin plugin, final TheGauntletConfig config)
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
		renderHunllef(graphics2D);
		renderTornadoes(graphics2D);

		boolean isRanged = plugin.isRanged;

		if(isRanged) {
			styleChange(graphics2D, true);
		}
		else{
			styleChange(graphics2D, false);
		}

		return null;
	}

	private void renderHunllef(final Graphics2D graphics2D)
	{
		final NPC hunllef = plugin.getHunllef();

		if (!config.hunllefOutlineTile() || hunllef == null || hunllef.isDead())
		{
			return;
		}

		final NPCComposition npcComposition = hunllef.getComposition();

		if (npcComposition == null)
		{
			return;
		}

		final Polygon polygon = Perspective.getCanvasTileAreaPoly(client, hunllef.getLocalLocation(),
			npcComposition.getSize());

		if (polygon == null)
		{
			return;
		}

		drawOutlineAndFill(graphics2D, config.hunllefOutlineColor(), config.hunllefFillColor(),
			config.hunllefTileOutlineWidth(), polygon);
	}

	private void styleChange(final Graphics2D graphics2D, boolean isRanged) {

		final NPC hunllef = plugin.getHunllef();
		final NPCComposition npcComposition = hunllef.getComposition();
		final Polygon polygon = Perspective.getCanvasTileAreaPoly(client, hunllef.getLocalLocation(),
				npcComposition.getSize());

		if(isRanged) {
			drawOutlineAndFill(graphics2D, Color.GREEN, config.hunllefFillColor(),
					config.hunllefTileOutlineWidth(), polygon);
		}
		else {
			drawOutlineAndFill(graphics2D, Color.BLUE, config.hunllefFillColor(),
					config.hunllefTileOutlineWidth(), polygon);
		}
	}

	private void renderTornadoes(final Graphics2D graphics2D)
	{
		if (!config.tornadoTileOutline() || plugin.getTornadoes().isEmpty())
		{
			return;
		}

		for (final NPC tornado : plugin.getTornadoes())
		{
			final Polygon polygon = Perspective.getCanvasTilePoly(client, tornado.getLocalLocation());

			if (polygon == null)
			{
				continue;
			}

			drawOutlineAndFill(graphics2D, config.tornadoOutlineColor(), config.tornadoFillColor(),
				config.tornadoTileOutlineWidth(), polygon);
		}
	}
}
