# SeedLight Riftways

With this mod you will be able to seamlessly travel from world to world, from your own front lawn in that single player world you started ages ago to you're friend's server, without going through the menus and stuff!

## About
This mod doesn't really add something you *can't* already do, but makes it a lot prettier. By cultivating a SeedLight plant, you will be able to open a Riftway portal, that can take you directly to a server that also has at least one Riftway active on it. And from there you could go back to your own world if you want, or start and adventure between so many other worlds, like you've seen in Minecraft Storymode, Alan Becker's animations, Hermticraft x Empires etc! (They didn't actually use this mod. Or maybe they did if they had a time machine)

It's kind of is the fundation of a REAL Minecraft "multiverse", or at least a cool way to connect worlds toghether without breaking the flow by going the main menu and stuff.

## How to play
### Creating the Riftway portal
More like *cultivating* it eheh!
The first thing to is try to obtain a SeedLight Seed!
/image goes here/
This is an ancient and probably forgotten seed that can only be found in a few places, legends tell that it was key to some ancient culture's successes. It can be found in these loot chests: 
  - End City, 1/2 with 20% chance
  - Ancient City, 1 with 25% chance
  - Bastion, 1/2 with 15%

Once obtained make sure to plant it in an open space, possibly not underground, and not in the dark. After, all it's called SeedLight for a reason. 
It's a very care-requiring plant, it grows quite slowly compared to most other plants and you will need to splash it with some throwable water bottles ever so often.
Each time it grows one stage it has a chance of consuming 1 or 2 units of water. It holds 10 at most. Every bottle splashed adds between 0 and 3 units.
/gif of plant growing/

 Oh and make sure not to destry it, since it's quite delicate and without the Fortune III enchantment you aren't guarantee the seed back! (The base chance is 33%, it grows every level)

When it finally blooms a lot of energy is contained in it, so it exponentially grows into a large tree, and inside of it the long-awateid Riftway!
/riftway tree here/
But it doesn't really lead anywhere does it? An inactive riftway will bounce you back away from it if you try to enter it!

### Setting a destination

You know need to go back and search for some Inter-Riftways Leves. And yes, they can be found where the seeds were stashed:
  - End City, 1/2 with 30% chance
  - Ancient City, 1/2 with 30% chance
  - Bastion, 1 with 25%

Or you could destroy some leaves from the new born tree, but they look so pretty, why would you do that? Oh fine, at least do it with fortune tho:
  - SeedLight Rift Flowering Leaves: 13-70% chance of getting one. Bonus: 0-40% chance of getting a SeedLight seed. (firt no fortune, last fortune III)
  - SeedLight Rift Leaves: 15-72% chance of getting one. (firt no fortune, last fortune III)

Now we will prepare some tea... well no we will smack the name of the place you want to vist through the riftway using an anvil!
Put the newly acquired leaf into an anvil and rename it to the IP of the server you want to connect to, such as "mc.example.com" or "hypixel.net" (hypixel won't actually work, as far as I know they don't have a riftway opened yet), or if you want to connect to a local wolrd (aka a singleplayer wolrd) write "local" if you want to go to your most-recently-played world or "local:nameoftheworld" to go to a sepcific one, such as "local:testworld".

Once you have done that, use the leaf on the Riftway, and it will turn blue singaling it is now active! (If you are on a server, now player will be able to connect to it if your sever is public and they know the "password". More on that later)

You can now enter the riftway and travel to never-before-seen places! Or your friend's backgarden. 

### Interactions with the Riftway
As seen before, you can activate a Riftway by simply setting a destination. 
To **deactivate** it you can right-click on it using a Fire Charge, and it will go back to being a red-inactive riftway.

You can also set a **combination** of items that is required to travel through your Riftway portal. To do this, fill a Bundle with the items that will make up your combination, and right-click on the riftway! Now everyone trying to get thorugh your riftway will need to add this before entering. (Warning: No encryption see the Disclaimer section down below). 

To remove a combination, simply empty the bundle and click again.

#### Connection to a riftway which is locked behind an item combination
If you want to connect to riftway that requires an item combination/password you will need to 1) Know the exact item combination, so go ask for it 2) You will need to fill up a Bundle in the exact same way as the combination given to you. 3) Simply hold it in your hand when travelling through the Riftway.

##### How do I get a Bundle item?
In survival you can't yet (blame Mojang), but there are some datapacks/mods that allow you to do so. Or if you want you can use the /give command to get it.


### Extra/"Advanced" stuff
If you are like a server admin or you would simply like to have a bigger portal, there is a block in the creative inverntory call "Riftway Block". Place it down as you like, you can create a portal as big as you like. As long as you place a riftway block that is no further away than 10 blocks from another one it still going to count as one. This is important because:

If more than one riftway portal is present when a player connects though it, it will be spawned at the closest one to where the player orignally came from.
For example, if a player stepped into his own riftway at 0 64 0, and there are two portals, one at 369 98 3 and one at -20 60 44 they will spawn at -20 60 44.

You can collect the riftway wood and use into your builds, but the bark is thick that it would be impossible to cut it down into planks.
You can also collect the leaves with shears or silk touch. 

## Plans
This mod is currently in ALPHA, meaning: It probably has bugs that will be fixed and also, quite a bit of conent is missing, such as 

- a "direct" riftway, meaning that a world can have multiple portals that lead directly to other ones, without being limited by one server(or wolrd)-wide portal to another location. This will allow you to build like a room with dozens of portals each leading to another place. (The other place has to be active to make the connection working)

- A way to set your own destination (not really hard to do, probabbly one of the features coming soon) instead of selecting one server-wide

- A vanilla server-only mod/plugin that is compatible with this mod to make players travel around even in vanilla servers using riftways (that will probably using End Gateways instead of riftways)

- A forge version of the mod. Or at least the thing abobe ^^^

- Add whitelist-bypass if the player is connecting using a riftway. Or something like that. (It would need to be turned on by a server owner of course)

- A patchouli book or something to explain in-game how the mod works
- An in-game config interface
- A feature to send you to a random server/world, kind of like an "instability" during your journey through the Riftways
- Maybe allow somekind of inventory/data sharing between worlds? This is kind of complex and maybe risky

## DISCLAIMER (Important, please read!)
What this mod **CAN NOT** and **WILL NOT** DO:
- It can't bypass server authenitcation plugins or whitelist or anything, it doesn't rewrite any of the code that actually makes you connect to a server. To the server's eyes, you are simply connection to it. The only thing it will know is that you came from a riftway, and it will teleport you to the closest one. I repeat, it CANNOT bypass server security and it won't do that.

- The list of items that make up the "password" for a riftway, are NOT ENCRYPTED, simply beacuse they are not meant to. They are not a proper security feature, so do not treat it as it was, use a whitelist instead! (If there is enough request, I can try to look into it and try to encrypt it). 
That said, it is only stored inside a data file on the server, so provided your server is secure, unless someones is using a plugin/mod/prograrm to catch the packets sent between a server and a client, no one else will know the password. (I doubt anyone will go to this lenghts only to steal a "toy password" for joining a server through a riftway)

- To make the client know if the server it is trying to join has riftway or not a socket-sever will be started, default port is 27999. Everytime a client connects a new thread is created to exchange information between the two, it should close right after the server respond, but bugs exist so watch out and, in the event something goes wrong please report it! (Also, restart the server in that case)

## Setup on a server
To make your sever visible to client connecting to it using riftways you will need to open a port for communication (a socket-server to exchange info between client and server, is started whenever you load the mod on a server). By default itis 27999 (TCP) but you can change it in the data file (located in the /config/ folder)

## License

This template is available under ARR for the time being!
