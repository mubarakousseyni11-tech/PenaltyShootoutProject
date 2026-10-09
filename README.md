# Penalty Shootout

A Java Swing football penalty-shootout game. Aim and shoot through a 10,000-level campaign or play a five-round cup shootout with sudden death.

## Features

- Scrollable campaign with timed, streak, accuracy, and sequence challenges.
- Penalty Cup mode with alternating kicks, sudden death, and a match summary.
- Selectable stadiums, penalty takers, and keepers.
- Career penalty conversion records for featured takers.
- Persistent in-game keeper save records.
- Settings for game audio, stadium crowd sound, and graphics quality.
- Synthesized home music, crowd ambience, and match sound effects.
- Custom player creation.

## Requirements

- Java Development Kit (JDK) 17 or newer.
- Windows for the included batch files.
- Internet access is optional. When available, the game checks selected players' current club and shirt number against ESPN roster data. If a check is unavailable, it displays the data as unverified.

## Run on Windows

Double-click `run.bat`, or run this from a terminal in the project folder:

```bat
run.bat
```

To compile without starting the game:

```bat
build.bat
```

The scripts compile into `build\` and do not require third-party libraries.

## Controls

1. Choose Campaign or Penalty Cup on the home screen.
2. In Campaign, select an unlocked level and start its challenge.
3. Click inside the goal to shoot. Use the arrow keys to adjust aim and Space to shoot.
4. Use the gear icon to open settings.

## Data and artwork notes

- Featured player penalty totals are career snapshots sourced from the Transfermarkt penalty-goals pages; each player's source URL is available in the player-selection info tooltip.
- Club and shirt-number lookups use ESPN's public soccer roster endpoints and require internet access.
- The keeper progression uses a dated FIFA men's national-team ranking as a country ordering; it is not an individual goalkeeper ranking.
- The stadiums, kits, player portraits, and crowd/music sounds are stylized or synthesized game assets, not official club artwork, photographs, or recordings.

## Project layout

```text
PenaltyShootoutProject/
├── PenaltyShootout.java
├── build.bat
├── run.bat
└── README.md
```
