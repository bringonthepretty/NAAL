# NAAL
NAAL is a library that exports Nier:Automata .mot files from blender

# Usage
1. Install blender
2. Download init.py from latest release
3. Install init.py as blender addon
4. Download t_pose.blend from latest release
5. Open t_pose.blend in blender
6. Animate
7. Export animation from blender as .ran file ("Nier:Automata animation (.ran)" option should be available in export menu). Name of .ran file must be same as original .mot file you are replacing
8. Download NAAL.exe or NAAL.jar from latest release. .jar is prefered, because it writes logs in file instead of console, but requires jre17+ installed
9. Download map.txt from latest release
10. Place NAAL.exe or NAAL.jar, .ran and map.txt in the same folder
11. Run NAAL.exe or NAAL.jar. (Double click for NAAL.exe or "java -jar NAAL.jar" in console for NAAL.jar)
12. If you did everything right, .mot file will be created in the same folder
13. Find .dat file that contains .mot you want to replace and extract data with for example RaiderB's https://github.com/ArthurHeitmann/F-SERVO
14. Replace original .mot with created .mot
15. Rapack .dat with RaiderB's tool
16. Paste repacked .dat to Nier:Automata data folder

# Tips
1. Do not change rest position for model
2. Do not use rotations by more that 180 degrees between 2 keyframes for any axis
3. Avoid -90 and 90 degrees rotations for Y axis. (89.9 is ok)
4. Do not change rotation type for bones. All rotations must be quaternions
5. t_pose.blend file from release is A2 in t pose. animating this model will work with 2B as well (A2 and 2B use same file for animations)
6. map.txt file from release as mapping between blender bones names and in-game bones names for A2
7. If you want to animate someone other than A2/2B, you should import wmb to blender using WoefulWolf's https://github.com/WoefulWolf/NieR2Blender2NieR, set rest pose to t pose and create map.txt using Kerilk's https://github.com/Kerilk/bayonetta_tools/blob/master/lib/bayonetta/tools/wmb_get_bone_map.rb (requires installed ruby on pc)
8. Tool ignores bones scale
9. Tool supposed to work with player models only and behaviour with non-player model (enemies, weapons, etc) is undefined. Throught, tool has better chances to work as expected with humanoid enemies and worse chances with weapons, large enemies and animated map objects

# Troubleshooting
Tool is in early beta state, so expect bugs. Also, feel free to contact me on discord [Wait and Hope]#3662 or create issue on github

# Thanks
WoefulWolf and all contributors for https://github.com/WoefulWolf/NieR2Blender2NieR<br/>
Kerilk and all contributors for https://github.com/Kerilk/bayonetta_tools and .mot file documentation<br/>
RaiderB for https://github.com/ArthurHeitmann/F-SERVO<br/>
Nier:Modding discord server community<br/>
