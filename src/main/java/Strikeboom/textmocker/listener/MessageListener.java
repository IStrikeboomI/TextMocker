package Strikeboom.textmocker.listener;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MessageListener extends ListenerAdapter {
    private static final List<MessageChannel> CHANNELS = new LinkedList<>();
    private static final List<Member> MEMBER_BLACKLIST = new LinkedList<>();
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getMember().getUser().isBot()) {
            if (CHANNELS.contains(event.getChannel())) {
                if (!MEMBER_BLACKLIST.contains(event.getMember())) {
                    String[] words = event.getMessage().getContentRaw().split("\\s+");
                    event.getMessage().reply(words[new Random().nextInt(words.length)]).queue();
                }
            }
        }
    }
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        System.out.println(event.getName());
        switch (event.getName()) {
            case "addchannel": {
                event.deferReply().queue();
                MessageChannel channel = event.getOption("channel").getAsMessageChannel();
                if (channel != null) {
                    if (!CHANNELS.contains(channel)) {
                        CHANNELS.add(channel);
                        event.getHook().sendMessage("Added Channel").queue();
                    } else {
                        event.getHook().sendMessage("Channel Already Added").queue();
                    }
                } else {
                    event.getHook().sendMessage("Add Only A Single Text Channel!").queue();
                }
                break;
            }
            case "blacklist" : {
                event.deferReply().queue();
                Member blacklistMember = Objects.requireNonNull(event.getOption("user")).getAsMember();
                if (blacklistMember != null) {
                    if (!MEMBER_BLACKLIST.contains(blacklistMember)) {
                        MEMBER_BLACKLIST.add(blacklistMember);
                        event.getHook().sendMessage("Added " + blacklistMember.getAsMention() + " To Blacklist").queue();
                    } else {
                        event.getHook().sendMessage("User Already Added").queue();
                    }
                } else {
                    event.getHook().sendMessage("Add A User!").queue();
                }
                break;
            }
        }
    }
}
