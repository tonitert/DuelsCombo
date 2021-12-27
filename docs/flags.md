#Flags
All flags that can be applied to items are listed here. To learn more about commands used to set these flags to items, see [this page](https://github.com/t0nero/DuelsCombo/blob/master/docs/commands.md).

##Data types
Flags can have different data types, which control what values can be set to them. Data types currently used by the plugin are listed below.

* **bool**: A boolean value, can be set to true or false.
* **double**: A decimal value.
* **int**: An integer value.

##Flags

* **projectile-shooter-knockback**: Sets how much the bow shooter's velocity is changed when the projectile hits and right or left click is pressed. Mainly used for creating grappling hook bows, which propel the player in the direction of the projectile, when the player clicks a mouse button. Data type: double
* **switch-positions-on-projectile-hit**: Whether to switch positions of the shooter and the shot entity on projectile hit. Data type: boolean
* **bow-instant-shoot**: Whether to make a bow shoot arrows instantly when the interact key is pressed. Data type: bool
* **time-between-bow-shots**: Cooldown in ticks after shooting the bow. Data type: int
* **projectile-velocity-multiplier**: Multiplier for the velocity of the shot projectile. Data type: double
* **projectile-explosion-size**: Size of the explosion created when the projectile hits an obstacle. Data type: double
* **projectile-explosion-destroy-blocks**: Whether to make the projectile destroy blocks. Data type: bool