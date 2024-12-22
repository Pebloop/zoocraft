package pebloop.zoocraft.ducks

import pebloop.zoocraft.screenHandlers.enclosureController.EnclosureControllerData

interface LivingEntityExtended {

    // Is this entity catchable and can be put in the zoo ?
    /*fun `zoocraft$isCatchable`(): Boolean
    fun `zoocraft$setCatchable`(tamable: Boolean)*/

    // is this entity caught and in the zoo ?
    fun `zoocraft$isCaught`(): Boolean
    fun `zoocraft$setCaught`(caught: Boolean)

    // entities need to num num
    fun `zoocraft$setHunger`(hunger: Float)
    fun `zoocraft$getHunger`(): Float
    fun `zoocraft$setHungerMax`(hunger: Float)
    fun `zoocraft$getHungerMax`(): Float

    // entities enclosure needs
    fun `zoocraft$getNeeds`(enclosureData: EnclosureControllerData): Float

}