# Flags
All flags that can be applied to items are listed here. To learn more about commands used to set these flags to items, see [this page](https://github.com/t0nero/DuelsCombo/blob/master/docs/commands.md).

## Data types
Flags can have different data types, which control what values can be set to them. Data types currently used by the plugin are listed below.

* **bool**: A boolean value, can be set to true or false.
* **double**: A decimal value.
* **int**: An integer value.

## Flags

* **projectile-shooter-knockback**: Sets how much the bow shooter's velocity is changed when the projectile hits and right or left click is pressed. Mainly used for creating grappling hook bows, which propel the player in the direction of the projectile, when the player clicks a mouse button. Data type: double
* **switch-positions-on-projectile-hit**: Whether to switch positions of the shooter and the shot entity on projectile hit. Data type: boolean
* **bow-instant-shoot**: Whether to make a bow shoot arrows instantly when the interact key is pressed. Data type: bool
* **time-between-bow-shots**: Time to cancel bow shooting for after shooting the bow. Data type: int
* **projectile-velocity-multiplier**: Multiplier for the velocity of the shot projectile. Data type: double
* **projectile-explosion-size**: Size of the explosion created when the projectile hits an obstacle. If set to 0, no explosion will be created. Data type: double
* **projectile-explosion-destroy-blocks**: Whether to make the projectile explosion destroy blocks. Data type: bool
* **projectile-push-amount**: Amount of pushing force to apply to the hit entity, when this item shoots a projectile. Data type: double
* **projectile-direction-randomness-multiplier**: Multiplier for the direction randomness Minecraft adds to bows by default. Data type: double
* **projectile-trail-particle**: Sets a particle to use as the projectile's trail. All available types can be found [here](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html). Data type: string
* 