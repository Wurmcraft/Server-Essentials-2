package com.wurmcraft.serveressentials.forge.server.data;

import static com.wurmcraft.serveressentials.forge.server.ServerEssentialsServer.GSON;
import static com.wurmcraft.serveressentials.forge.server.ServerEssentialsServer.SAVE_DIR;

import com.wurmcraft.serveressentials.forge.api.SECore;
import com.wurmcraft.serveressentials.forge.api.data.DataKey;
import com.wurmcraft.serveressentials.forge.api.json.JsonParser;
import com.wurmcraft.serveressentials.forge.server.ServerEssentialsServer;
import java.io.File;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.util.Strings;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class FileDataHandler extends BasicDataHandler {

  @Override
  public <T extends JsonParser> NonBlockingHashMap<String, T> getDataFromKey(DataKey key,
      T type) {
    try {
      NonBlockingHashMap<String, T> currentlyLoaded = super.getDataFromKey(key, type);
      if (!currentlyLoaded.isEmpty()) {
        return currentlyLoaded;
      }
    } catch (NoSuchElementException ignored) {
    }
    NonBlockingHashMap<String, T> fileData = new NonBlockingHashMap<>();
    File dataDir;
    if (key.isInStorage()) {
      dataDir = new File(SAVE_DIR + File.separator + "Storage" + key.getName());
    } else {
      dataDir = new File(SAVE_DIR + File.separator + key.getName());
    }
    if (dataDir.exists() && dataDir.isDirectory()) {
      for (File file : dataDir.listFiles()) {
        try {
          JsonParser data = getData(key, file.getName().replaceAll(".json", ""));
          fileData.put(data.getID(), (T) data);
        } catch (Exception e) {
          ServerEssentialsServer.LOGGER
              .warn("Failed to read '" + file.getPath() + "' for '" + key + "'");
        }
      }
      return fileData;
    }
    ServerEssentialsServer.LOGGER.info("No data for '" + key.getName() + " was found!");
    return new NonBlockingHashMap<>();
  }

  @Override
  public JsonParser getData(DataKey key, String dataID) throws NoSuchElementException {
    try {
      return super.getData(key, dataID);
    } catch (NoSuchElementException e) {
      File fileLoc;
      if (key.isInStorage()) {
        fileLoc = new File(
            SAVE_DIR + File.separator + "Storage" + File.separator + key.getName()
                + File.separator + dataID
                + ".json");
      } else {
        fileLoc = new File(
            SAVE_DIR + File.separator + key.getName() + File.separator + dataID
                + ".json");
      }
      if (key.getDataType() != null) {
        if (fileLoc.exists()) {
          try {
            registerData(key,
                GSON.fromJson(Strings.join(Files.readAllLines(fileLoc.toPath()), '\n'),
                    key.getDataType()));
            JsonParser data = getData(key, dataID);
            if (data != null) {
              return data;
            }
          } catch (Exception f) {
            ServerEssentialsServer.LOGGER
                .warn("Failed to read '" + fileLoc.getPath() + "' for '" + key + "'");
            if (fileLoc.isDirectory()) {
              fileLoc.delete();
            }
          }
        }
      } else if (key == DataKey.MODULE_CONFIG) {
      }
      throw new NoSuchElementException();
    }
  }

  @Override
  public void registerData(DataKey key, JsonParser dataToStore) {
    super.registerData(key, dataToStore);
    File file;
    if (key.isInStorage()) {
      file = new File(
          SAVE_DIR + File.separator + "Storage" + File.separator + key.getName()
              + File.separator + dataToStore.getID()
              + ".json");
    } else {
      file = new File(
          SAVE_DIR + File.separator + key.getName() + File.separator + dataToStore.getID()
              + ".json");
    }
    try {
      file.getParentFile().mkdirs();
      file.createNewFile();
      Files.write(file.toPath(), GSON.toJson(dataToStore).getBytes());
    } catch (Exception e) {
      ServerEssentialsServer.LOGGER
          .error("Failed to save '" + key.getName() + ":" + dataToStore.getID() + "'");
    }
  }

  @Override
  public void delData(DataKey key, String dataToRemove, boolean deleteFromDisk)
      throws NoSuchElementException {
    super.delData(key, dataToRemove, deleteFromDisk);
    if (deleteFromDisk) {
      File file;
      if (key.isInStorage()) {
        file = new File(
            SAVE_DIR + File.separator + "Storage" + File.separator + key.getName()
                + File.separator + dataToRemove
                + ".json");
      } else {
        file = new File(
            SAVE_DIR + File.separator + key.getName() + File.separator + dataToRemove
                + ".json");
      }
      if (file.delete()) {
        if (SECore.config.debug) {
          ServerEssentialsServer.LOGGER
              .info("Deleting '" + key + ":" + dataToRemove + "");
        }
      } else {
        throw new NoSuchElementException();
      }
    }
  }
}
