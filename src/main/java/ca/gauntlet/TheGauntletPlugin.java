/*
 * BSD 2-Clause License
 *
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

package ca.gauntlet;

import ca.gauntlet.entity.Demiboss;
import ca.gauntlet.entity.Resource;
import ca.gauntlet.overlay.SceneOverlay;
import ca.gauntlet.overlay.TimerOverlay;
import ca.gauntlet.resource.ResourceManager;
import com.google.inject.Provides;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "The Gauntlet",
	description = "All-in-one plugin for The Gauntlet.",
	tags = {"the", "gauntlet"},
	enabledByDefault = false
)
public class TheGauntletPlugin extends Plugin
{
	private static final int VARBIT_GAUNTLET_ENTERED = 9178;
	private static final int VARBIT_GAUNTLET_HUNLLEF_ROOM_ENTERED = 9177;

	private static final Set<Integer> DEMIBOSS_IDS = Set.of(
		NpcID.CRYSTALLINE_BEAR, NpcID.CORRUPTED_BEAR,
		NpcID.CRYSTALLINE_DARK_BEAST, NpcID.CORRUPTED_DARK_BEAST,
		NpcID.CRYSTALLINE_DRAGON, NpcID.CORRUPTED_DRAGON
	);

	private static final Set<Integer> STRONG_NPC_IDS = Set.of(
		NpcID.CRYSTALLINE_SCORPION, NpcID.CORRUPTED_SCORPION,
		NpcID.CRYSTALLINE_UNICORN, NpcID.CORRUPTED_UNICORN,
		NpcID.CRYSTALLINE_WOLF, NpcID.CORRUPTED_WOLF
	);

	private static final Set<Integer> WEAK_NPC_IDS = Set.of(
		NpcID.CRYSTALLINE_BAT, NpcID.CORRUPTED_BAT,
		NpcID.CRYSTALLINE_RAT, NpcID.CORRUPTED_RAT,
		NpcID.CRYSTALLINE_SPIDER, NpcID.CORRUPTED_SPIDER
	);

	private static final Set<Integer> RESOURCE_IDS = Set.of(
		ObjectID.CRYSTAL_DEPOSIT, ObjectID.CORRUPT_DEPOSIT,
		ObjectID.PHREN_ROOTS, ObjectID.PHREN_ROOTS_36066,
		ObjectID.FISHING_SPOT_36068, ObjectID.FISHING_SPOT_35971,
		ObjectID.GRYM_ROOT, ObjectID.GRYM_ROOT_36070,
		ObjectID.LINUM_TIRINUM, ObjectID.LINUM_TIRINUM_36072
	);

	private static final Set<Integer> UTILITY_IDS = Set.of(
		ObjectID.SINGING_BOWL_35966, ObjectID.SINGING_BOWL_36063,
		ObjectID.RANGE_35980, ObjectID.RANGE_36077,
		ObjectID.WATER_PUMP_35981, ObjectID.WATER_PUMP_36078
	);

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private TheGauntletConfig config;

	@Inject
	private SkillIconManager skillIconManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ResourceManager resourceManager;

	@Inject
	private TimerOverlay timerOverlay;

	@Inject
	private SceneOverlay sceneOverlay;

	@Getter
	private final Set<Resource> resources = new HashSet<>();

	@Getter
	private final Set<GameObject> utilities = new HashSet<>();

	@Getter
	private final Set<Demiboss> demibosses = new HashSet<>();

	@Getter
	private final Set<NPC> strongNpcs = new HashSet<>();

	@Getter
	private final Set<NPC> weakNpcs = new HashSet<>();

	private final List<Set<?>> entitySets = Arrays.asList(resources, utilities, demibosses, strongNpcs, weakNpcs);

	private boolean inGauntlet;
	private boolean inHunllef;

	@Override
	protected void startUp()
	{
		if (client.getGameState() == GameState.LOGGED_IN)
		{
			clientThread.invoke(this::pluginEnabled);
		}
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(sceneOverlay);
		overlayManager.remove(timerOverlay);

		inGauntlet = false;
		inHunllef = false;

		timerOverlay.reset();
		resourceManager.reset();

		entitySets.forEach(Set::clear);
	}

	@Provides
	TheGauntletConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(TheGauntletConfig.class);
	}

	@Subscribe
	private void onGameStateChanged(final GameStateChanged event)
	{
		switch (event.getGameState())
		{
			case LOADING:
				if (inGauntlet)
				{
					resources.clear();
					utilities.clear();
				}
				break;
			case LOGIN_SCREEN:
			case HOPPING:
				shutDown();
				break;
		}
	}

	@Subscribe
	private void onConfigChanged(final ConfigChanged event)
	{
		if (!event.getGroup().equals("thegauntlet"))
		{
			return;
		}

		switch (event.getKey())
		{
			case "resourceIconSize":
				if (!resources.isEmpty())
				{
					resources.forEach(r -> r.setIconSize(config.resourceIconSize()));
				}
				break;
			case "resourceTracker":
				if (inGauntlet && !inHunllef)
				{
					resourceManager.reset();
					resourceManager.init();
				}
				break;
			default:
				break;
		}
	}

	@Subscribe
	private void onVarbitChanged(final VarbitChanged event)
	{
		if (isHunllefVarbitSet())
		{
			if (!inHunllef)
			{
				initHunllef();
			}
		}
		else if (isGauntletVarbitSet())
		{
			if (!inGauntlet)
			{
				initGauntlet();
			}
		}
		else
		{
			if (inGauntlet || inHunllef)
			{
				shutDown();
			}
		}
	}

	@Subscribe
	private void onWidgetLoaded(final WidgetLoaded event)
	{
		if (!inGauntlet)
		{
			return;
		}

		if (event.getGroupId() == WidgetID.GAUNTLET_TIMER_GROUP_ID)
		{
			timerOverlay.setGauntletStart();
			resourceManager.init();
		}
	}

	@Subscribe
	private void onChatMessage(final ChatMessage event)
	{
		if (!inGauntlet || inHunllef)
		{
			return;
		}

		final ChatMessageType type = event.getType();

		if (type == ChatMessageType.SPAM || type == ChatMessageType.GAMEMESSAGE)
		{
			resourceManager.parseChatMessage(event.getMessage());
		}
	}

	@Subscribe
	private void onActorDeath(final ActorDeath event)
	{
		if (!inGauntlet || event.getActor() != client.getLocalPlayer())
		{
			return;
		}

		timerOverlay.onPlayerDeath();
	}

	@Subscribe
	private void onGameObjectSpawned(final GameObjectSpawned event)
	{
		if (!inGauntlet || inHunllef)
		{
			return;
		}

		final GameObject gameObject = event.getGameObject();

		final int id = gameObject.getId();

		if (RESOURCE_IDS.contains(id))
		{
			resources.add(new Resource(gameObject, skillIconManager, config.resourceIconSize()));
		}
		else if (UTILITY_IDS.contains(id))
		{
			utilities.add(gameObject);
		}
	}

	@Subscribe
	private void onGameObjectDespawned(final GameObjectDespawned event)
	{
		if (!inGauntlet || inHunllef)
		{
			return;
		}

		final GameObject gameObject = event.getGameObject();

		final int id = gameObject.getId();

		if (RESOURCE_IDS.contains(gameObject.getId()))
		{
			resources.removeIf(o -> o.getGameObject() == gameObject);
		}
		else if (UTILITY_IDS.contains(id))
		{
			utilities.remove(gameObject);
		}
	}

	@Subscribe
	private void onNpcSpawned(final NpcSpawned event)
	{
		if (!inGauntlet || inHunllef)
		{
			return;
		}

		final NPC npc = event.getNpc();

		final int id = npc.getId();

		if (DEMIBOSS_IDS.contains(id))
		{
			demibosses.add(new Demiboss(npc));
		}
		else if (STRONG_NPC_IDS.contains(id))
		{
			strongNpcs.add(npc);
		}
		else if (WEAK_NPC_IDS.contains(id))
		{
			weakNpcs.add(npc);
		}
	}

	@Subscribe
	private void onNpcDespawned(final NpcDespawned event)
	{
		if (!inGauntlet || inHunllef)
		{
			return;
		}

		final NPC npc = event.getNpc();

		final int id = npc.getId();

		if (DEMIBOSS_IDS.contains(id))
		{
			demibosses.removeIf(d -> d.getNpc() == npc);
		}
		else if (STRONG_NPC_IDS.contains(id))
		{
			strongNpcs.remove(npc);
		}
		else if (WEAK_NPC_IDS.contains(id))
		{
			weakNpcs.remove(npc);
		}
	}

	private void pluginEnabled()
	{
		if (isGauntletVarbitSet())
		{
			timerOverlay.setGauntletStart();
			resourceManager.init();
			initGauntlet();
		}

		if (isHunllefVarbitSet())
		{
			initHunllef();
		}
	}

	private void initGauntlet()
	{
		inGauntlet = true;

		overlayManager.add(sceneOverlay);
		overlayManager.add(timerOverlay);
	}

	private void initHunllef()
	{
		inHunllef = true;

		timerOverlay.setHunllefStart();
		resourceManager.reset();

		overlayManager.remove(sceneOverlay);
	}

	private boolean isGauntletVarbitSet()
	{
		return client.getVarbitValue(VARBIT_GAUNTLET_ENTERED) == 1;
	}

	private boolean isHunllefVarbitSet()
	{
		return client.getVarbitValue(VARBIT_GAUNTLET_HUNLLEF_ROOM_ENTERED) == 1;
	}
}
