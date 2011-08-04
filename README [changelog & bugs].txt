- means feature or issue removed.
+ means feature added.
* means bug of feature fixed.

BUGS/TODO:
+Undo works only by deleting/replacing diamonds (with air), and no cages.

CHANGELOG:

1.0
*Created first plugin, detects portal placement.
+When portal is placed, finally, it is changed into a wood block, and a neat structure takes its place.
+Made complex structure branch off, with partitions.
+Structure finally traps player, almost makes it impossible NOT to die.
*Fixed all bugs associated, especially the infinite loop.

1.1
-Scrapped killing player/branched idea, and worked on diamond.
*Placing portal will AT LAST create a weird looking diamond out of wood.
+The diamond is jump-insidable (by accident)!
+FIRST PLUGIN OFFICIAL RELEASE!

1.2
+Added commands! No more stupid portals!
*The "plugin.yml" has a use, and can be easily read and understood.
+Arguments and Quickcommands added! Make diamonds out of glass, use only four total keystrokes!
+Added help menus and a Quick Reference, and a more organized if/else branch system.
*Made the diamonds spawn one block up from current block, waaaay better.

1.2_01
*Diamond creation more streamlined, only "valid" blocks spawn, no more apparent bugs.

1.2_02
+Cages added (on request from poolshark)! At your current target, a sloppy about 5x5ish box is made!

1.2_03
+Added, after hours of tedious work, an undo function, and streamlined code more. Determined the only way to implement undo as of now is to make the undo replace the last Diamond placed with air. But, the "last diamond placed" thingy is still neat, in my books.
+Released changelog/etc online, and made a mental note to create way more structures ASAP, focus more on video-making next Thursday, and have undo be a necessity. 

1.3
*Cleaned up code, again, and added OP support. Permissions coming next update, but sending off to Bukkit Plugin List now!

1.4
*Finally got approved on the Bukkit list! Cool!
+With much help from my brother, added incredibly perfect spheres (just 9 lines of code)! You get to choose your radius, too.
+Added a  "disable" feature, so I can disable the plugin easily, even though I still can't update seamlessly! >:(
*Fixed a wall of the cage spawning one block lower than the rest.
*Doesn't allow undos without a structure creation first. (Almost overlooked this one!)
+Added an awesome feature where the user can create his/her own unique structures from an external file! [Ω]
+Added support of the all-loving Permissions plugin! [∆]
-Got rid of cages being made of liquids, as well as diamonds.
*Modified the general layout of the plugin.yml, adding the nodes for Permissions (for fun!).
+Generates a folder on first run, and then stores a temporary "readme" file.
+Added fancy brackets around the log info messages!
*Fixed a bunch of misc. errors, making sure that the plugin handles everything, and no "Internal errors" pop up. Hopefully that's all, but I'm relying on a few forum-testers to prove me wrong!
+When making a sphere, if the user specifies for a liquid (id#8-11), a glass sphere will be made, with a sphere of the specified liquid inside!

Ω - By creating a readable, .txt, file in the Replicator-generated directory, you can create your own structures! Every line will be a new block placement, with the following format: 
		[item id], [x additive], [y additive], [z additive]
Where the additives are the additions to the current block you're looking at. So, to make a three-block high column of wood…
		5, 0, 1, 0
		5, 0, 2, 0
		5, 0, 3, 0

∆ - If you have the Permissions plugin enabled, then the nodes are:
		replicator.diamond
		replicator.cage
		replicator.sphere
		replicator.read
		replicator.disable
    Without the Permissions plugin enabled, Replicator will default to OP.