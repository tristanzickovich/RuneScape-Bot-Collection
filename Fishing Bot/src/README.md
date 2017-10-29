# Fishing Bot

Screenshots in the [images](https://github.com/tristanzickovich/RuneScape-Bot-Collection/tree/master/Fishing%20Bot/src/images) folder must be updated for different screen sizes.

The global arrays in `main.java` must be updated, arranged preferred order of execution
* `dropItemArray`: Array of the `inventoryItem` class requiring:
    * image path
    * keybind (in [SikuliX formatting](https://raiman.github.io/SikuliX-2014/javadocs/index.html))
    * match similarity
* `fishingMethodArray`: Array of the fishing method image paths
## Known Bugs
* Determining if character is fishing or still only works if character is facing the screen
