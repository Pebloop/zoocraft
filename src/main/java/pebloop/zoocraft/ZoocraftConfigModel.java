package pebloop.zoocraft;

import io.wispforest.owo.config.annotation.Config;

@Config(name = "zoocraft-config", wrapperName = "ZoocraftConfig")
public class ZoocraftConfigModel {
    public boolean isRabbitCatchable = true;
    public boolean isCowCatchable = true;
    public boolean isPigCatchable = true;
    public boolean isSheepCatchable = true;
    public boolean isChickenCatchable = true;
}
