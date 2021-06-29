27 June 2021

Edited files: HunlleffOverlay.java and TheGauntletPlugin.java

Disclaimer: Use of this plugin is obviously against game rules, and up to the user's discretion. The purpose of this project is for experimentation with runelite plugin development. USE AT YOUR OWN RISK.

What I've done:

1. Added outline changes showing attack style of the boss. Green = ranged, blue = magic

TODO:

~~1. Instead of using the hitsplat event, use animation event so that when player gets hit by lightning, it doesn't count as the boss's attacks.~~

2. Done halfway: implement changing of player attack styles to suit boss's protection prayers. Counting is done and is somewhat accurate but display is not implemented, other than a simple "system.out.println".

29 June 2021: Changed counting from hitsplat-based to animation based. Counting of hunllef attacks is now almost completely accurate, however there is a bug where if the ticks do not sync correctly, counting of attacks will be off by 1-2. Also added chatbox message for attack counts, so the player can verify if it is synchronised correctly.
