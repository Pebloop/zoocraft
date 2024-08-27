package pebloop.zoocraft;

import blue.endless.jankson.Jankson;
import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ZoocraftConfig extends ConfigWrapper<pebloop.zoocraft.ZoocraftConfigModel> {

    public final Keys keys = new Keys();

    private final Option<java.lang.Boolean> isRabbitCatchable = this.optionForKey(this.keys.isRabbitCatchable);
    private final Option<java.lang.Boolean> isCowCatchable = this.optionForKey(this.keys.isCowCatchable);
    private final Option<java.lang.Boolean> isPigCatchable = this.optionForKey(this.keys.isPigCatchable);
    private final Option<java.lang.Boolean> isSheepCatchable = this.optionForKey(this.keys.isSheepCatchable);
    private final Option<java.lang.Boolean> isChickenCatchable = this.optionForKey(this.keys.isChickenCatchable);

    private ZoocraftConfig() {
        super(pebloop.zoocraft.ZoocraftConfigModel.class);
    }

    private ZoocraftConfig(Consumer<Jankson.Builder> janksonBuilder) {
        super(pebloop.zoocraft.ZoocraftConfigModel.class, janksonBuilder);
    }

    public static ZoocraftConfig createAndLoad() {
        var wrapper = new ZoocraftConfig();
        wrapper.load();
        return wrapper;
    }

    public static ZoocraftConfig createAndLoad(Consumer<Jankson.Builder> janksonBuilder) {
        var wrapper = new ZoocraftConfig(janksonBuilder);
        wrapper.load();
        return wrapper;
    }

    public boolean isRabbitCatchable() {
        return isRabbitCatchable.value();
    }

    public void isRabbitCatchable(boolean value) {
        isRabbitCatchable.set(value);
    }

    public boolean isCowCatchable() {
        return isCowCatchable.value();
    }

    public void isCowCatchable(boolean value) {
        isCowCatchable.set(value);
    }

    public boolean isPigCatchable() {
        return isPigCatchable.value();
    }

    public void isPigCatchable(boolean value) {
        isPigCatchable.set(value);
    }

    public boolean isSheepCatchable() {
        return isSheepCatchable.value();
    }

    public void isSheepCatchable(boolean value) {
        isSheepCatchable.set(value);
    }

    public boolean isChickenCatchable() {
        return isChickenCatchable.value();
    }

    public void isChickenCatchable(boolean value) {
        isChickenCatchable.set(value);
    }


    public static class Keys {
        public final Option.Key isRabbitCatchable = new Option.Key("isRabbitCatchable");
        public final Option.Key isCowCatchable = new Option.Key("isCowCatchable");
        public final Option.Key isPigCatchable = new Option.Key("isPigCatchable");
        public final Option.Key isSheepCatchable = new Option.Key("isSheepCatchable");
        public final Option.Key isChickenCatchable = new Option.Key("isChickenCatchable");
    }
}

