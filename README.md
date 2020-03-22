# Oculus-Quest-Presence

> Discord Rich Presence for the Oculus Quest

---

## How to install

- Dowload the latest zip file from <a href="https://github.com/madmagic007/Oculus-Quest-Presence/releases" target="_blank">here</a> and unzip it somwhere.
- install <a href="https://sidequestvr.com/#/download" target="_blank">sidequest</a> on your pc (skip if you already have sidequest installed).
- Download <a href="https://github.com/tverona1/QuestAppLauncher/releases/tag/v0.10.2" target="_blank">Quest app launcher</a>.
- Sideload the quest app launcher apk to your quest (skip if already installed), plenty of tutorials on how to sideload apks exist.
- When your quest is connected to your pc via usb and connected to internet, look in the top left corner of the sidequest window for the wifi ip of your quest and note it down somewere. It should look something like 192.168.1.100.
- From the zip file, sideload the Oculus Quest Discord RPC.ak to your quest.
- Run oqrpc.exe from the zip file.
- Once its installed, run the Oculus Quest Discord RPC.jar and a window should pop up asking for the ip of your oculus quest.
- Make sure your oculus quest is powered on and connected to the internet. Next type the ip you noted earlier in the text area and hit validate. if everything is done right, it should say "found device, but apk is not running on devide" (don't close this window yet).
- Open the quest app launcher on your quest, go to library > unknown sources and look for something called quest app laucnher.
- In quest app launcher, click on the 2D tab and look for oqrpc.apk and run it. On the first time running it, it will prompt for usage acces (this is so it knows what ap is currently displaying topmost). select oqrpc and toggle it on. Press the B button on your right controller multiple times untill you are in the library again.
- Go back to your pc and hit the validate button in the window again and it should say something like "apk is running on device, everything ready to go (If this doesn't display, then redo the last step)", and hit the save button.
- Right click the icon in system tray again and hit "Request presence to start".
- A notification should appear on your computer and if discord is running, it should show that you are playing on your oculus quest!

---

## Settings

- When you restart your computer, the program on the pc should start automatically, this can be disabled if you right click the icon, hit "open settings" and toggle the "start with windows boot" checkbox.
- If you open the settings and hit the save log button, it will save a file with all the app names that your device has come across that aren't listed in my lang.json file. You can always send me that file so i can add it to the list!
  
---

##  Things to note & issues im aware of

- If your computer/quest lose internet conection for more than one minute, the presence will stop showing. To restore this right click on the oculus quest rpc icon in the system tray on your computer and hit "Request presence to start".
- If your computer is turned on after your quest, it wont show the presence untill you manually start the apk via quest app launcher.
- Both programs "should" automatically start with device boot, but thay may not always the be case. If the presence didn't start but the program on the pc is running, hit the "Request presence to start" button, and if you didnt get a message saying your quest is online, then you have to manually start the app from the quest app launcher.
- When your quest turns on and if it connects to wifi within 3 minutes of it powering on, it will automatically start displaying the presence on discord. If it's after 3 minutes, you have to manyally start the app.
- The way i get the names of the games you are playing is by reading the package name of the current topmost process. For Pavlov this would be "com.vankrupt.pavlov" i use an api that reads the lang.json file found in this repository to get a better looking name, like "Pavlov Vr" in this case. If a package name isn't listed in the lang file, it will show just the last part of that package name, for pavlov that would be just "pavlov". I will try to add as many names as posible, but ofcourse i dont know every game so if you find one, make sure to report it to me.

---

## Contact me for bug reporting or reporting an unlisted name

- Discord: ⎛⎝⧹Maͫgͣiͩc⧸⎠⎞#6891
- Twitter: @madmagic5
- Reddit: u/madmagic008

---

## Contributing

If you have experience with java/android and would like to help, or if you don't know how to code but you want to help adding to the lang.json file. Make sure to contact me and i will see what is possible!
