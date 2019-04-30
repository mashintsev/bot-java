package ru.mail.im.botapi.sample;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.mail.im.botapi.BotApiClient;
import ru.mail.im.botapi.request.GeneralRequest;
import ru.mail.im.botapi.request.SelfGetRequest;
import ru.mail.im.botapi.request.SendTextRequest;
import ru.mail.im.botapi.response.GeneralResponse;
import ru.mail.im.botapi.sample.command.CommandListener;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public class AppCommandListener implements CommandListener {

    private final Gson gson = new GsonBuilder().create();

    @Nullable
    private BotApiClient client;

    @Override
    public void onStart(final String token) {
        client = new BotApiClient("https://u.icq.net/rapi/botapi", token);
        client.start();
    }

    @Override
    public void onExit() {
        if (client != null) {
            client.stop();
            client = null;
        }
    }

    @Override
    public void onSelf() {
        execute(new SelfGetRequest());
    }

    @Override
    public void onSendText(final String chatId, final String text) {
        execute(SendTextRequest.simple(chatId, text));
    }

    @Override
    public void onSendFile(final String chatId, final File file) {
        System.out.format("Send file '%s' to chat %s%n", file, chatId);
    }

    @Override
    public void onSendVoice(final String chatId, final File file) {
        System.out.format("Send voice '%s' to chat %s%n", file, chatId);
    }

    private <T extends GeneralResponse> void execute(final GeneralRequest<T> request) {
        if (client != null) {
            try {
                printResponse(client.execute(request));
            } catch (IOException ex) {
                System.err.println("Fail to execute API method: " + ex.getMessage());
            }
        } else {
            System.err.println("Client not started");
        }
    }

    private void printResponse(final GeneralResponse response) {
        System.out.println(gson.toJson(response));
    }
}