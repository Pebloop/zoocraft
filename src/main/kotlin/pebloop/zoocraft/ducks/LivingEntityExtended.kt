package pebloop.zoocraft.ducks

interface LivingEntityExtended {

    // Is this entity catchable and can be put in the zoo ?
    fun `zoocraft$isCatchable`(): Boolean
    fun `zoocraft$setCatchable`(tamable: Boolean)

    // is this entity caught and in the zoo ?
    fun `zoocraft$isCaught`(): Boolean
    fun `zoocraft$setCaught`(caught: Boolean)

}