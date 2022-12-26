# SeedLight Riftways

With this mod you will be able to seamlessly travel from world to world, from your own front lawn in that single player world you started ages ago to you're friend's server, without going through the menus and stuff!

## About
This mod doesn't really add something you *can't* already do, but makes it a lot prettier. By cultivating a SeedLight plant, you will be able to open a Riftway portal, that can take you directly to a server that also has at least one Riftway active on it. And from there you could go back to your own world if you want.

It's kind of is the fundation of a REAL Minecraft "multiverse", or at least a cool way to connect worlds toghether without breaking the flow by going the main menu and stuff.

## Plans
This mod is currently in ALPHA, meaning: It probably has bugs that will be fixed and also, quite a bit of conent is missing, such as 

- a "direct" riftway, meaning that a world can have multiple portals that lead directly to other ones, without being limited by one server(or wolrd)-wide portal to another location

- A way to set your own destination (not really hard to do, probabbly one of the features coming soon) instead of selecting one server-wide

- A vanilla server-only mod/plugin that is compatible with this mod to make players travel around even in vanilla servers using riftways (that will probably using End Gateways instead of riftways)

- A forge version of the mod. Or at least the thing abobe ^^^

- Add whitelist-bypass if the player is connecting using a riftway. Or something like that. (It would need to be turned on by a server owner of course)

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
