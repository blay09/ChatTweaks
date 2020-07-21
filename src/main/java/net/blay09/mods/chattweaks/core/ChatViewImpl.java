package net.blay09.mods.chattweaks.core;

import net.blay09.mods.chattweaks.api.ChatMessage;
import net.blay09.mods.chattweaks.api.ChatView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ChatViewImpl implements ChatView {

    public static final Pattern defaultFilterPattern = Pattern.compile("(?:<(?<s>[^>]+)>)? ?(?<m>.*)", Pattern.DOTALL);
    public static final Pattern groupPattern = Pattern.compile("\\$(?:([0-9])|\\{([\\w])\\})");
    public static final Pattern outputFormattingPattern = Pattern.compile("(\\\\~|~[0-9abcdefkolmnr])");

    private final Set<String> channels = new HashSet<>();
    private final List<ChatMessage> chatLines = new ArrayList<>();

    private final String name;
    private String display;
    private boolean muted;
    private boolean exclusive;
    private boolean unread;

    private String filterPattern = "";
    private String outputFormat = "$0";
    private String outgoingPrefix;

    private Pattern compiledFilterPattern = defaultFilterPattern;
    private String builtOutputFormat = outputFormat;

    public ChatViewImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isMuted() {
        return muted;
    }

    @Override
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    @Override
    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    @Override
    public boolean isExclusive() {
        return exclusive;
    }

    @Override
    public String getDisplay() {
        return display;
    }

    @Override
    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public void addChannel(String channelName) {
        channels.add(channelName);
    }

    @Override
    public boolean containsChannel(String channelName) {
        return channels.contains(channelName);
    }

    @Override
    public void removeChannel(String channelName) {
        channels.remove(channelName);
    }

    @Override
    public boolean hasUnreadMessages() {
        return unread;
    }

    @Override
    public void markAsUnread() {
        unread = true;
    }

    @Override
    public void markAsRead() {
        unread = false;
    }

    @Override
    public boolean messageMatches(String message) {
        Matcher matcher = compiledFilterPattern.matcher(message);
        return matcher.matches();
    }

    @Override
    public ChatMessage addChatLine(ChatMessage chatMessage) {
        return chatMessage;
    }

    @Override
    public String getFilterPattern() {
        return filterPattern;
    }

    @Override
    public void setFilterPattern(String filterPattern) {
        this.filterPattern = filterPattern;
        if (!filterPattern.isEmpty()) {
            try {
                compiledFilterPattern = Pattern.compile(filterPattern, Pattern.DOTALL);
            } catch (PatternSyntaxException e) {
                compiledFilterPattern = defaultFilterPattern;
            }
        } else {
            compiledFilterPattern = defaultFilterPattern;
        }
    }

    @Override
    public String getOutputFormat() {
        return outputFormat;
    }

    @Override
    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
        Matcher matcher = outputFormattingPattern.matcher(outputFormat);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "\u00a7" + matcher.group(1));
        }
        matcher.appendTail(sb);
        builtOutputFormat = sb.toString();
    }

    @Override
    public String getOutgoingPrefix() {
        return outgoingPrefix;
    }

    @Override
    public void setOutgoingPrefix(String outgoingPrefix) {
        this.outgoingPrefix = outgoingPrefix;
    }
}