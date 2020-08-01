package com.wurmcraft.serveressentials.forge.modules.general.command;

import com.wurmcraft.serveressentials.forge.api.command.Command;
import com.wurmcraft.serveressentials.forge.api.command.CommandArguments;
import com.wurmcraft.serveressentials.forge.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.forge.api.json.player.Home;
import com.wurmcraft.serveressentials.forge.api.json.player.StoredPlayer;
import com.wurmcraft.serveressentials.forge.modules.general.GeneralModule;
import com.wurmcraft.serveressentials.forge.server.utils.ChatHelper;
import com.wurmcraft.serveressentials.forge.server.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.server.utils.TeleportUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

@ModuleCommand(moduleName = "General", name = "Home", aliases = {"H"})
public class HomeCommand {

  @Command(inputArguments = {CommandArguments.HOME}, inputNames = {"homeName"})
  public void sendHome(ICommandSender sender, Home home) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      TeleportUtils.teleportTo(player, home);
      ChatHelper.sendHoverMessage(sender,
          PlayerUtils.getLanguage(sender).GENERAL_HOME.replaceAll("%HOME%", home.name),
          TextFormatting.GOLD + "X: " + Math.round(home.x) + " Y: " + Math.round(home.y)
              + " Z: " + Math
              .round(home.z) + " Dim: " + home.dim);
    }
  }

  @Command(inputArguments = {})
  public void defaultHome(ICommandSender sender) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      StoredPlayer playerData = PlayerUtils.get(player);
      boolean found = false;
      for (Home h : playerData.server.homes) {
        if (h.name.equalsIgnoreCase(GeneralModule.config.defaultHomeName)) {
          found = true;
          sendHome(sender, h);
        }
      }
      if (!found) {
        ChatHelper.sendMessage(sender,
            PlayerUtils.getLanguage(sender).COMMAND_USAGE.replaceAll("%COMMAND%", "/Home")
                .replaceAll("%ARGS%", ""));
        ChatHelper.sendMessage(sender,
            PlayerUtils.getLanguage(sender).COMMAND_USAGE.replaceAll("%COMMAND%", "/Home")
                .replaceAll("%ARGS%", "<homeName>"));
        ChatHelper.sendMessage(sender,
            PlayerUtils.getLanguage(sender).COMMAND_USAGE.replaceAll("%COMMAND%", "/Home")
                .replaceAll("%ARGS%", "<list>"));
      }
    }
  }

  @Command(inputArguments = {CommandArguments.INTEGER}, inputNames = {"homeName"})
  public void homeNo(ICommandSender sender, int home) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      StoredPlayer playerData = PlayerUtils.get(player);
      boolean found = false;
      for (Home h : playerData.server.homes) {
        if (h.name.equalsIgnoreCase("" + home)) {
          found = true;
          sendHome(sender, h);
        }
      }
      if (!found) {
        ChatHelper.sendMessage(sender,
            PlayerUtils.getLanguage(sender).COMMAND_USAGE.replaceAll("%COMMAND%", "/Home")
                .replaceAll("%ARGS%", ""));
        ChatHelper.sendMessage(sender,
            PlayerUtils.getLanguage(sender).COMMAND_USAGE.replaceAll("%COMMAND%", "/Home")
                .replaceAll("%ARGS%", "<homeName>"));
        ChatHelper.sendMessage(sender,
            PlayerUtils.getLanguage(sender).COMMAND_USAGE.replaceAll("%COMMAND%", "/Home")
                .replaceAll("%ARGS%", "<list>"));
      }
    }
  }

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"list"})
  public void listHome(ICommandSender sender, String arg) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (arg.equalsIgnoreCase("list")) {
        ChatHelper.sendSpacerWithMessage(sender,
            PlayerUtils.getLanguage(sender).COMMAND_SPACER, "Home");
        StoredPlayer playerData = PlayerUtils.get(player);
        for (Home h : playerData.server.homes) {
          ChatHelper.sendHoverMessage(sender, TextFormatting.GOLD + h.name,
              TextFormatting.GOLD + "X: " + Math.round(h.x) + " Y: " + Math.round(h.y)
                  + " Z: " + Math.round(h.z) + " Dim: " + h.dim);
        }
        ChatHelper.sendMessage(sender, PlayerUtils.getLanguage(sender).COMMAND_SPACER);
      } else {
        ChatHelper.sendMessage(sender,
            PlayerUtils.getLanguage(sender).COMMAND_USAGE.replaceAll("%COMMAND%", "/Home")
                .replaceAll("%ARGS%", ""));
        ChatHelper.sendMessage(sender,
            PlayerUtils.getLanguage(sender).COMMAND_USAGE.replaceAll("%COMMAND%", "/Home")
                .replaceAll("%ARGS%", "<homeName>"));
        ChatHelper.sendMessage(sender,
            PlayerUtils.getLanguage(sender).COMMAND_USAGE.replaceAll("%COMMAND%", "/Home")
                .replaceAll("%ARGS%", "<list>"));
      }
    }
  }
}
