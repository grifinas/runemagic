# Keywords
**Activating a circle** -
While wearing Alchemists' gloves shift rightclick on any kind of Chalk Writing. This requires mana. On successful activation, circle will be destroyed

**Sacrifice** - Thorw items on the circle before activating it. Successfull activation will result in items being at least partially consumed

**Connected circles** - Circles that are on the same Y coordinate and are adjacent to each other are considered connected. If there is a circle adjacent to one of the already connected circles, it is also connected.

**Chalk writing** - the sum of all connected circles

**Mana bar** -
A bar of mana above food bar, does not regenerate. Can be used for alchemy.
Replenishes by disassembling items in disassembly circle or standing in Meditation circle.
By default holds 100 mana, 20 half-orbs, each 5 in value

# Minable Blocks
## Limestone
* Spawns identically to diorite/andesite
* Can be mined with pickaxe
### Crafting
* Can be made into chalk

# Chalk Circles (Carpet blocks)
* Does not generate in the world. Can be created by using chalk on the ground.
* Can be broken by any material. Never drops anything

## Usage
* Using chalk on Chalk Circle causes it to change the type. The types alternate in order (initially first, after using chalk once - second and so on), when using chalk on the circle in the last mode, it will change back into first one
* Can be used to perform magic or alchemy

## **Transmutation Upgrade Circle**
Made by using Chalk on the ground.
Every adjacent Transmutation upgrade Circle increases effectiveness by 5% up to 9

### Usage
**Sacrifice** items to transmute them into different items. Not all items can be transmuted. When caster does not have enough mana or material, only part of stack will be consumed.

**Available transmutations**

|   From    |   To      |   Conversion rate |   Cost    |   Cost/stack @+40%    | 
|-----------|-----------|-------------------|-----------|-----------------------|
| Diorite   | Limestone |   1:1             |   1       |   38,4                |
| Wood      | Coal      |   1:1             |   1.5     |   57,6                |
| Coal      | Iron      |   1:4             |   2.5     |   96                  |
| Iron      | Gold      |   1:4             |   3.5     |   134,4               |
| Gold      | Diamond   |   1:4             |   5       |   192                 |
| Diamond   | Philosopher shard   |   1:4   |   10      |   384                 |
| Redstone dust | Glowstone dust   |   1:4  |   2       |   76,8                |
| Wool      |   Dye     |   1:1             |   2       |   76,8                |
| Stone     |   Mana stone|   1:1           |   100+     |   100+                 |

## **Transmutation Downgrade Circle**
Made by using Chalk on the ground.
Every adjacent Transmutation Downgrade Circle increases effectiveness by 5% up to 9

### Usage
**Sacrifice** items to transmute them into different items. Not all items can be transmuted. When caster does not have enough mana or material, only part of stack will be consumed.

**Available transmutations**

|   From    |   To      |   Conversion rate |   Cost    |   Cost/stack @+40%    | 
|-----------|-----------|-------------------|-----------|-----------------------|
| Coal      | Wood      |   1:1             |   1.5     |   57,6                |
| Iron      | Coal      |   4:1             |   2.5     |   96                  |
| Gold      | Iron      |   4:1             |   3.5     |   134,4               |
| Diamond   | Gold      |   4:1             |   5       |   192                 |
| Philosopher shard | Diamond   |   4:1     |   10      |   384                 |
| Glowstone dust | Glowstone dust   |   4:1 |   2       |   76,8                |


## **Disassembly circle**
Made by using Chalk on the ground.

### Usage
**Sacrifice** any amount of any item

**Effect:** Destroys items, awards mana to caster based on the value of items

|   Item    |   Mana    |
|-----------|-----------|
|   Coal    |   2       |
|   Iron    |   8       |
|   Gold    |   32      |
|   Diamond |   64      |
|   Philosopher shard |256|
|   Anything else |   1      |

## **Meditation circle**
Created by using blue Chalk on the ground.

### Usage
* Stand inside to lose hunger, but gain mana at the rate of 10 mana per 2 hunger bars
* Cannot be activated


## **Tool Conjuration Circle**
Created by using green Chalk on the ground.

### Usage
**Sacrifice** paper, any tool

**Optionally sacrifice** enchanted book, only if circle is connected to Alteration circle.

**Activating** the circle costs 10 mana per connected circle. On success, atempts to create a tool conjuration scroll.
* For every Conjuration circle (up to 9) connected - increases the durability of conjured tool.
* For every connected Alteration circle (up to 9), tool will have a random enchantment with level of 3x the number of connected alteration circles
* if enchanted book was sacrificed - the tool will atempt to have the books enchantment, when that's not possible, will have random enchantment.
* For every connected Alteration Meditation circle (up to 9), decreases conjure tool casting price by 8%

## **Destruction circle**
Created by using red Chalk on the ground.

### Usage
**Sacrifice** Fire charge

**Activating** the circle consts 10 mana per connected circle. On success, creates a scroll of fire ball.
* For every connected Destruction circle, increases fireball damage by 1 (up to 9).
* For every 3 connected Conjuration circles (up to 9) creates an additional fire ball.
* For every connected Alteration Meditation circle (up to 9), decreases fire ball casting price by 8%

## **Flesh sacrifice circle**
Created by using black Chalk on the ground.

To **activate** must have a connected meditation circle.
* Deal 4 hearts woth of damage to creatures on the writing, if a creature dies it does not drop any loot or experience
* Restores 20 mana to caster for every 4 hearts of damage dealt

## **Mind sacrifice circle**
Created by using black Chalk on the ground.

**Sacrifice** Deal 4 hearts worth of damage to creatures on the writing, if a creature dies it does not drop any loot or experience

To **activate** must have a connected meditation circle and conjuration circle.
* Caster loses 2 levels
* Caster gains 20 mana

## **Raise dead circle**
Created by using black Chalk on the ground.

**Sacrifice** Bones or rotten flesh or spider eyes

**Activate** costs 10 mana per connected circle, attempts to spawn friendly undead.
* For every sacrificed item, increases the cost by 30. Atempts to spawn one corresponding entity.
* For every connected raise dead circle, increases the duration of the undead by 30 seconds
* for every connected conjuration circle increases the health of undead by 1 heart (up to 9)


# Items
## Chalk
* Does not generate in the world
* Crafted: limestone = chalk * 4
### Crafting
* Chalk + green dye = green Chalk
* Chalk + red dye = red Chalk
* Chalk + blue dye = blue Chalk
* Chalk + black dye = black Chalk
## Alchemists' book
* Does not generate in the world
* Crafted: book + chalk = Alchemists' book

### Usage
* Can be used to open tutorial
* Can be used to craft alchemists' gloves

## Alchemists' gloves
* Does not generate in the world
* Crafted:
||||
|---|---|---|
||L||
|L|B|L|
||||

### Usage
* User can **activate** **Chalk circles**

## Philosophers' stone
* Does not generate in the world
* Crafted from Philosopher shards:

Philosopher shard = P
||||
|---|---|---|
|P|P|P|
|P|P|P|
|P|P|P|

### Usage
* Doubles the amount of mana you have
* Allows to use hunger instead of mana at the rate of 1 hunger = 10 mana
* Allows to use health instead of mana at the rate of 1 heart = 10 mana
* Always uses mana -> hunger -> health
* Successful **circle activation** lowers durability

## Mana stone
* Does not generate in the world
* Transmuted from stone
* Holds up to 200 mana
### Usage
* Can be consumed to instantly restore all mana held inside

## Mana star
* Does not generate in the world
* Holds up to 200 mana
* Regenerates 1 mana per second
* Crafted:

Mana stone = M

Philosopher stone = P
||||
|---|---|---|
|M|M|M|
|M|P|M|
|M|M|M|

### Usage
* Before casters' mana is used, use mana stars' mana
* If casters' mana is full and mana would be regained, refill mana star