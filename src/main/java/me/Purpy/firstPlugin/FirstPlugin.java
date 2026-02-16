package me.Purpy.firstPlugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class FirstPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Purp Plugin has started! :)");
    }

    @Override
    public void onDisable() {
        System.out.println("Purp Plugin has stopped :(");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("broadcast")) {

            if (!sender.hasPermission("purpy.broadcast")) {
                sender.sendMessage(ChatColor.RED + "U don't have perms!");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /broadcast <message>");
                return true;
            }

            String message = String.join(" ", args);
            String title = ChatColor.translateAlternateColorCodes('&', "&c&lBROADCAST&7:&r " + message);
            String subtitle = "";

            int fadeIn = 10;
            int stay = 70;
            int fadeOut = 20;

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            }

            return true;
        }


        if (command.getName().equalsIgnoreCase("getrandomquote")) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage("Players only.");
                return true;
            }

            if (!sender.hasPermission("purpy.getquote")) {
                sender.sendMessage(ChatColor.RED + "U don't have perms!");
                return true;
            }

            player.sendMessage(ChatColor.GRAY + "Fetching a random quote...");

            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                try {
                    URL url = new URL("https://quote-generator-api-six.vercel.app/api/quotes/random");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    InputStreamReader reader =
                            new InputStreamReader(connection.getInputStream());

                    JsonObject json =
                            JsonParser.parseReader(reader).getAsJsonObject();

                    String quote = json.get("quote").getAsString();

                    Bukkit.getScheduler().runTask(this, () ->
                            player.sendMessage(ChatColor.DARK_PURPLE + "\nQuote:§d§l " + quote)
                    );

                } catch (Exception e) {
                    Bukkit.getScheduler().runTask(this, () ->
                            player.sendMessage(ChatColor.DARK_RED + "Failed to fetch quote.")
                    );
                    e.printStackTrace();
                }
            });
            return true;
        }



        return false;
    }
}
