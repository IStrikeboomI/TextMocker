package Strikeboom.textmocker;

import Strikeboom.textmocker.listener.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class TextMocker {
    public static void main(String[] args) {
        String token = null;
        File tokenFile = new File("token.txt");
        if (!tokenFile.exists()) {
            try {
                System.out.println("Fill out token.txt with bot token");
                tokenFile.createNewFile();
                System.exit(0);
            } catch (IOException ignored) {}
        }
        try {
            Scanner reader = new Scanner(tokenFile);
            token = reader.nextLine();
            reader.close();
        } catch (FileNotFoundException ignored) {}
        try {
            JDA jda = JDABuilder.createDefault(token).build();

            jda.getPresence().setActivity(Activity.listening("/addchannel to start"));

            jda.updateCommands().addCommands(
            new CommandData("addchannel","Adds A Channel For The Bot To Mock Others On").addOption(OptionType.CHANNEL,"channel","The channel to add",true),
            new CommandData("blacklist","Blacklists User So They Don't Get Mocked").addOption(OptionType.USER,"user","The user you want blacklisted",true)).queue();

            jda.addEventListener(new MessageListener());
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}
