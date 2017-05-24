package net.blay09.mods.bmc;

import com.google.common.collect.Maps;
import net.blay09.mods.bmc.chat.ChatChannel;
import net.blay09.mods.bmc.chat.ChatMessage;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatManager {
	private static final int START_ID = 500;
	private static final AtomicInteger chatLineCounter = new AtomicInteger(START_ID);
	private static final ChatChannel mainChannel = new ChatChannel("main");
	private static final ChatChannel interactionChannel = new ChatChannel("interaction");
	private static final ChatChannel systemChannel = new ChatChannel("system");
	private static final ChatChannel deathChannel = new ChatChannel("death");
	private static final Map<String, ChatChannel> channels = Maps.newHashMap();

	public static String[] systemLang = new String[] {
			"gameMode.changed",
			"chat.type.admin",
	};

	public static String[] interactionLang = new String[] {
			"tile.bed.noSleep",
			"tile.bed.notSafe",
			"tile.bed.occupied",
	};

	public static void init() {
		addChatChannel(mainChannel);
		addChatChannel(interactionChannel);
		addChatChannel(systemChannel);
	}

	public static ChatChannel findChatChannel(ChatMessage message) {
		if(message.getTextComponent() instanceof TextComponentTranslation) {
			String key = ((TextComponentTranslation) message.getTextComponent()).getKey();
			if(!key.equals("chat.type.text") && !key.equals("chat.type.emote")) {
				for(String s : systemLang) {
					if(key.equals(s)) {
						return systemChannel;
					}
				}
				for(String s : interactionLang) {
					if(key.equals(s)) {
						return interactionChannel;
					}
				}
				if(key.startsWith("death.")) {
					return deathChannel;
				}
			}
		}
		return mainChannel;
	}

	public static ChatChannel getChatChannel(String name) {
		return channels.get(name);
	}

	public static void addChatChannel(ChatChannel channel) {
		if(channels.containsKey(channel.getName())) {
			throw new RuntimeException("duplicate channel " + channel.getName());
		}
		channels.put(channel.getName(), channel);
	}

	public static int getNextMessageId() {
		return chatLineCounter.incrementAndGet();
	}

	public static String[] collectChatChannelNames() {
		return channels.keySet().toArray(new String[channels.size()]);
	}
}
