
# Oculus-Quest-Presence

## Quest 2 is now supported! Detailed instructions on how to make it work can be found inside the zip, or <a href="https://github.com/madmagic007/Oculus-Quest-Presence/wiki/Quest-2-support">here</a>
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
- Make sure you have the latest version of the <a href="https://www.java.com/en/download/win10.jsp" target="_blank">Java JRE</a> installed on your computer.
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

## Modules

Modules allow for more detailed information about a game or program on the quest to be shown in the presence.
Read <a href="https://github.com/madmagic007/Oculus-Quest-Presence/wiki/Modules" target="_blank">more here</a> about how to implement a module to a game/program on the quest.

---

## Things to note

- Loss of internet connection for more than one minute will stop showing the presence. Right click on the oculus quest rpc icon in the system tray on your computer and hit "Request presence to start".
- Both programs *should* automatically start with device boot, but that may not always the be case. If the presence didn't start but the program on the pc is running, hit the "Request presence to start" button. If you didn't get a message saying your quest is online, then you have to manually start the app from the quest app launcher.
- If you quest connects to wifi within 3 minutes of powering on, it will automatically start displaying the presence on Discord. If it doesn't, you may have to manually start the app.

---

## Troubleshooting / questions

#### - I open the .jar file but nothing happens
- Make sure you have the latest version of the <a href="https://www.java.com/en/download/win10.jsp" target="_blank">Java JRE</a> installed on your computer.
- The window asking for the ipv4 address only shows up on the first time opening the program. To see if the program is running look in the system tray. If an icon looking like a quest shows, that means that it is running.
- If you have the latest Java JRE installed but you arent able to open .jar files, run <a href="https://johann.loefflmann.net/en/software/jarfix/index.html" target="_blank">jarfix</a> and it should resovle the issue.

#### - It keeps saying "error finding device"
- Make sure both your quest and pc are connected to the same wifi network, that the quest is powered on and that the entered ip is correct.

#### - It only says "device found, but apk is not running on device"
- Check if the program is actually running on your quest. If it is open, hit the terminate button in the quest and then hit the start button and try to verify again.
- Check if the firewall isn't blocking Java and add an exception if necessary.

#### - I did everything and it said "device found and apk is running on device", but the presence isn't showing
- Add java.exe and javaw.exe as an exception to your firewall.
- Check if you got the notification on your pc saying that your quest is online. If you received that, it means that the quest has connected to the pc and the error is with discord. If you didn't get that notification, make sure that the apk is running on your quest.
- Discord for web browser won't detect games running on your pc, so make sure you are using the desktop application.
- Check the settings in discord and make sure that game presence is enabled.
- Try running the jar via commandline by using the command `java -jar "path_to_jar`.

#### - It was working before and it randomly stopped working
- Dynamic ip addresses can change after a while, open the settings window and make sure that the ip there is the same as the one found in SideQuest. You can also assign a static ip address to your quest.
- If the ip is correct, follow the steps above this.

##### If none of these steps helped, contact me on discord (link at the top).  

#### - Do i need to set the ip every single time?
- No, the window asking for the ip only comes up if no ip has been set before, or that it doesn't find one in the config file. 
#### - Will this work for mac/linux?
- For mac, download jar from <a href="https://github.com/madmagic007/Oculus-Quest-Presence/raw/master/pc/out/artifacts/pc_jar/Oculus%20Quest%20Discord%20RPC.jar">here</a> and place this is a folder where no admin privlegses are required to edit files. I dont have an installer yet because I dont know how to work with mac. 
- Linux has no support yet, you can try downloading the jar but that most likely won't work properly on linux.
#### - Does my pc needs to be on all the time?
- If you want to show the presence then yes, if you dont want to have it show then your pc can be turned off.
#### - Will this affect performance/battery life on my quest?
- This is a very light application so it shouldn't be noticeable at all. However keep in mind that it is yet another application that is running in the background.

---

## Contributing

If you have experience with java/android or you'd like to help adding to the `lang.json` file, contact me using the social badges at the top of the repo. 

---

Special thanks to [u/FinalFortune_](https://www.reddit.com/user/FinalFortune_/) for helping me out with bug testing and creating a demo/tutorial video, [DexTheArcticFox](https://github.com/dexthearcticfox) for helping me with Quest 2 development and [LaurieTheFish](https://github.com/Lauriethefish) for helping with module development.
