package pebloop.zoocraft.ducks

import pebloop.zoocraft.ducks.data.PlayerZooEntityData

interface PlayerEntityExtended {

    fun getZoodexData(): ArrayList<PlayerZooEntityData>
    fun setZoodexData(data: ArrayList<PlayerZooEntityData>)

}