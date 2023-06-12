package upe.resource.model.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import upe.process.messages.UProcessMessage;
import upe.process.messages.UProcessMessageImpl;

import java.lang.reflect.Type;

public class UpeMessageClassTypeAdapter implements JsonDeserializer<UProcessMessage> {

    @Override
    public UProcessMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, UProcessMessageImpl.class);
    }
}
