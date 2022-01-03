# Commands

The plugin has many commands related to adding modifiers to duel kits, and items, listed below. All commands need the duels.admin permission in order to be used.

## setcombo

Manages the settings of a given Duels kit. 

### Usage:

    /duels combo setcombo [kit] [comboDuel:true|false] [noDamageTicks] [knockbackMultiplier] [maxKnockbackSpeedMultiplier] [knockbackYMultiplier]

* kit: Name of the kit that will be modified.
* comboDuel: Whether to apply the noDamageTicks option on duel start.
* noDamageTicks: Adjusts the amount of ticks the hit player is invulnerable after taking damage.
* knockbackMultiplier: When a player is hit by another player in the duel, this parameter adjusts the amount of knockback applied to the player. When this parameter is left to the default value, the vanilla knockback algorithm will be used. This parameter is stored as decimal. Default value: 1
* maxKnockbackSpeedMultiplier: The maximum knockback a player is allowed to receive when the vanilla knockback algorithm isn't being used.
* knockbackYMultiplier: Amount of additional knockback to give in the positive Y direction, to emulate vanilla knockback.

## setflag

Manages the properties of an item currently in the hand of the player using the command.

### Usage:
    /duels combo setitemflag [flag] [value]

* flag: The flag to set to the item.
* value: The value to set for the flag.

All available flags are listed [here](https://github.com/t0nero/DuelsCombo/blob/master/docs/flags.md).

## list

Lists all saved settings related to kits.

### Usage

    /duels combo list

## listflags

Lists all flags that can be set to a item.

### Usage

    /duels combo listflags

All available flags are listed [here](https://github.com/t0nero/DuelsCombo/blob/master/docs/flags.md).

## listitemflags

Shows all flags that have been assigned to the item currently held in the main hand.

### Usage

    /duels combo listitemflags



