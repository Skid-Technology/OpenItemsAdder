<p align="center">
  <img src="assets/ss.png" alt="Skid Solutions Banner" width="350" />
  <br>
  <font size="6"><b>OpenItemsAdder</b></font>
  <br>
  <font size="3">by Skid Solutions</font>
</p>

<p align="center">
  <a href="https://discord.gg/AEwatvnvSf">Discord</a>
</p>

<p align="center">
  <img src="assets/jak.jpg" alt="jak" height="150" />
  <img src="assets/hack.jpg" alt="hack" height="150" />
  <img src="assets/deobf2.png" alt="deobf2" height="150" />
  <img src="assets/deobf.jpg" alt="deobf" height="150" />
</p>

This is NOT a Remake or Recreation.


---

## Prerequisites

- **JDK 21**
- Internet access during the build (Maven Central, `libraries.minecraft.net`, `repo.citizensnpcs.co`, `repo.skriptlang.org`, `repo.jeff-media.com`, `repo.extendedclip.com`, `repo.papermc.io`, `maven.enginehub.org`, `mvn.lumine.io`)

### Local jars (`libs/`)

Some dependencies are not published to Maven. Place these files in `libs/` before building:

| File | Purpose |
|------|---------|
| `spigot-server-1.21.11.jar` | Mojang-named Spigot server with CraftBukkit `v1_21_R7` classes |
| `NativePackets-0.1.15.jar` | NativePackets API |
| `Denizen-1.3.4-b7314-DEV.jar` | Denizen API |
| `Mimic-v0.10.0.jar` | Mimic API |

The server jar can be produced with Spigot BuildTools (`--rev 1.21.11 --compile SPIGOT`), using the `original-spigot-1.21.11-R0.2-SNAPSHOT.jar` output.

---

## How to build

Run from this folder:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

.\gradlew.bat clean build
```

Output in `build/libs/`:
- `Itemsadder-5.0.0-beta-8.jar`

Drop the jar into your server's `plugins/` folder and start the server.

---

## Known limitations

- **Reduced build.** Files that do not compile against 1.21.11 are listed in [`compile-exclusions.txt`](compile-exclusions.txt) and are excluded from compilation. Their sources remain in `src/`. The current jar contains only the compiled subset plus `SimplifiedMain`, so most features are not included yet.
- **Other server versions.** Adapters for `v1_20_R4` and `v1_21_R1`–`R6` are excluded by the build script. Only 1.21.11 (`v1_21_R7`) is targeted.
- **Runtime libraries.** Libraries declared with `implementation` (for example fastjson2, commons, fastutil) are not bundled into the jar yet. Shading is not configured.

---
