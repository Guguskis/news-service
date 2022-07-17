package lt.liutikas.reddit.opm;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DiscordBot {

    private static final Logger LOG = LoggerFactory.getLogger(DiscordBot.class);
    private final DiscordApi api;
    @Value("${discord.channelId}")
    private String channelId;

    public DiscordBot(DiscordApi api) {
        this.api = api;
    }

    public void sendMessage(String message) {
        Optional<ServerTextChannel> channelOpt = api.getServerTextChannelById(channelId);

        if (channelOpt.isEmpty()) {
            LOG.error("Channel not found {}", channelId);
            return;
        }

        new MessageBuilder()
                .append(message)
                .appendNewLine()
                .send(channelOpt.get());
    }

    private void purgeAllMessages(ServerTextChannel serverTextChannel) {

        int limit = 100;
        MessageSet messages;
        do {
            messages = serverTextChannel.getMessages(limit).join();
            messages.forEach(Message::delete);
        } while (messages.size() == limit);
    }

}
