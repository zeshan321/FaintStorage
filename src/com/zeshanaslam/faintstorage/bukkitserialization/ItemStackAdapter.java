package com.zeshanaslam.faintstorage.bukkitserialization;

import com.google.gson.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.lang.reflect.Type;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        try {
            return ItemstackSerialization.itemStackArrayFromBase64(jsonElement.getAsString())[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ItemStack(Material.AIR);
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext context) {
        ItemStack[] itemStacks = new ItemStack[]{itemStack};

        return new JsonPrimitive(ItemstackSerialization.itemStackArrayToBase64(itemStacks));
    }
}
