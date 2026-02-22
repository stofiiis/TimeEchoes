# Time Echoes

Time Echoes is a NeoForge mod for Minecraft `1.21.11`.

The mod adds "echoes of the past":
- temporal artifacts with aura effects,
- lore collectible relics,
- worldgen ruins and forgotten sites,
- an Echo Archivist trader variant.

## Current Features

- Artifacts:
  - `chrono_shard`: can accelerate nearby crop growth.
  - `entropy_core`: can slow or block nearby crop growth.
- Collectible relic set:
  - `bronze_hourglass`
  - `obsidian_sundial`
  - `fractured_tablet`
  - `echo_compass`
- Wandering trader integration:
  - extra Time Echoes trades,
  - chance to rename to `Echo Archivist`.
- Custom world structures (no vanilla village templates):
  - `fractured_camp`
  - `collapsed_watchpost`
  - `buried_archive`
  - `sundial_court`
  - `shattered_workshop`
  - `echo_outpost`
  - `moss_vault`
  - `silent_forum`
  - `temporal_sanctum`
  - `grand_obelisk`
- Teleport commands:
  - `/timeechoes tp_site <site_name> [radius]`
  - `/timeechoes tp_echo_ruin [radius]` (alias for `fractured_camp`)
  - `/timeechoes tp_structure <structure_id> [radius]`

## Development

- Run client: `./gradlew runClient`
- Build jar: `./gradlew build`

## License

This project is licensed under the custom **DONT TOUCH MY SHIT LICENSE v1.0**.
See `LICENSE`.
