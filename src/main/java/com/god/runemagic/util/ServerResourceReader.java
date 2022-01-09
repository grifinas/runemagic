package com.god.runemagic.util;

import com.god.runemagic.RuneMagicMod;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerResourceReader {
    private static ServerResourceReader instance;
    private final IResourceManager resourceManager;

    public ServerResourceReader(FMLServerStartingEvent event) {
        this.resourceManager = event.getServer().getDataPackRegistries().getResourceManager();
        instance = this;
    }

    public static @Nullable ServerResourceReader getInstance() {
        return instance;
    }

    public @Nullable JsonObject readJson(String name) {
        ResourceLocation loc = new ResourceLocation(RuneMagicMod.MOD_ID + ":custom/" + name);
        InputStream in;
        try {
            in = this.resourceManager.getResource(loc).getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            JsonElement je = gson.fromJson(reader, JsonElement.class);
            return je.getAsJsonObject();
        } catch (IOException e) {
            RuneMagicMod.LOGGER.info("Failed to read JSON: {}", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
