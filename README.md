
# Oculus-Quest-Presence

#### Discord Rich Presence for the Oculus Quest

<p align="center">
   <a href="https://discordapp.com/users/401795293797941290/">
   <img src="https://img.shields.io/badge/Discord-%232C2F33.svg?logo=discord" alt="Discord">
   </a>
   <a href="https://twitter.com/madmagic5">
   <img src="https://img.shields.io/badge/Twitter-%23657786.svg?logo=twitter" alt="Twitter">
   </a>
   <a href="https://www.reddit.com/user/madmagic008/">
   <img src="https://img.shields.io/badge/Reddit-%23cee3f8.svg?logo=reddit" alt="Reddit">
   </a>
</p>

## How to install

Video tutorial:
<div align="center">
  <a href="https://www.youtube.com/watch?v=omhujeBPCIU"><img src="https://img.youtube.com/vi/omhujeBPCIU/maxresdefault.jpg" alt="Video tutorial"></a>
</div>
<br/><br/>

### Files
- Download the <a href="https://github.com/madmagic007/Oculus-Quest-Presence/releases" target="_blank">latest release</a> and unzip it.
- Download and install <a href="https://sidequestvr.com/#/download" target="_blank">SideQuest</a> on your PC.
- Download <a href="https://github.com/tverona1/QuestAppLauncher/releases" target="_blank">Quest app launcher</a>.

### On Your PC
- Use SideQuest to sideload the `Quest App Launcher apk` and the `Quest Discord RPC apk` (from the zip)
- Run `oqrpc.exe` to install the client on your PC (from the zip)
- After the installer is finished, run the `Oculus Quest Discord RPC.jar`
- In SideQuest, check the top left corner to see the IP address of your Quest. It should look something like this: `192.168.x.x`
- Enter this IP address into the popup window.

### On Your Quest
- Open the `Quest App Launcher` found under `Library > Unknown Sources`
- Select the `2D` tab and look for the `oqrpc.apk`. It will now prompt you for permissions.
- Select `oqrpc` and toggle it on. Press the `B` button on your controller to navigate back.

You can now go back to your PC and press the `validate` button in the popup window. It should tell you that the apk is running. Restart your quest and a notification should appear on your computer telling you that it is online. Your Quest games will now appear in your Discord rich presence.

---

## Settings

- When you restart your computer, the program on the pc should start automatically, this can be disabled if you right click the icon, hit "open settings" and toggle the "start with windows boot" checkbox.
- If you open the settings and hit the save log button, it will save a file with all the app names that your device has come across that aren't listed in my lang.json file. You can always send me that file so i can add it to the list!
---

## Known Issues

- Loss of internet connection for more than one minute will stop showing the presence. Right click on the oculus quest rpc icon in the system tray on your computer and hit "Request presence to start".
- Both programs *should* automatically start with device boot, but that may not always the be case. If the presence didn't start but the program on the pc is running, hit the "Request presence to start" button. If you didn't get a message saying your quest is online, then you have to manually start the app from the quest app launcher.
- If you quest connects to wifi within 3 minutes of powering on, it will automatically start displaying the presence on Discord. If it doesn't, you may have to manually start the app.
- Game presence names are based on the package. For Pavlov, this would be "com.vankrupt.pavlov". These are then mapped to a proper name using the `lang.json` file found in this repository. If no name mapping is available, it will take the last piece of the package name instead. For example, `com.vankrupt.pavlov -> pavlov`

---

## Contributing

If you have experience with java/android or you'd like to help adding to the `lang.json` file, contact me using the social badges at the top of the repo. 

---

Special thanks to [u/FinalFortune_](https://www.reddit.com/user/FinalFortune_/) for helping me out with bug testing and creating a demo/tutorial video.
