/*
 * Copyright (c) 2018, Jos <Malevolentdev@gmail.com>
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
package net.runelite.client.plugins.statusbars;

import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;
import com.google.inject.Provides;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.itemstats.ItemStatPlugin;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Status Bars",
	description = "Draws status bars next to players inventory showing current HP & Prayer and healing amounts",
	enabledByDefault = false
)

@Slf4j
@PluginDependency(ItemStatPlugin.class)
public class StatusBarsPlugin extends Plugin
{
	private static final String EXTENDED_SUPER_ANTIFIRE_DRINK_MESSAGE = "You drink some of your extended super antifire potion.";
	private static final String SUPER_ANTIFIRE_EXPIRED_MESSAGE = "<col=7f007f>Your super antifire potion has expired.</col>";

	private boolean isAntiFireActivated;

	@Inject
	private StatusBarsOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Provides
	StatusBarsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(StatusBarsConfig.class);
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.SPAM && event.getType() != ChatMessageType.GAMEMESSAGE)
		{
			return;
		}

		if (event.getMessage().equals(EXTENDED_SUPER_ANTIFIRE_DRINK_MESSAGE))
		{
			isAntiFireActivated = true;
		}
		if (event.getMessage().equals(SUPER_ANTIFIRE_EXPIRED_MESSAGE))
		{
			isAntiFireActivated = false;
		}

		log.debug("Event Fired" + "");
	}
}
