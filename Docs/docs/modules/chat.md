# Chat Module

The Chat module is a module designed to manage and format chat via the use of channels.

Name: `Chat`

Requirements: `Core`

### Commands

| Name        | Permission Node            | Description                                                                 | Default Aliases                         | Recommended Security  |
| ----------- | ---------------------------|-----------------------------------------------------------------------------|-----------------------------------------|-----------------------|

### Non-Command Permission

| Description                                                                                                                                              | Permission Node    |
| -------------------------------------------------------------------------------------------------------------------------------------------------------- |--------------------|
| Can a user use color codes in chat using the `&` sign, See [Formatting Codes](https://minecraft.fandom.com/wiki/Formatting_codes) for a full list        | `chat.color`       |
| Can a user use replacement to print info related to themselves, see [#formatting](/modules/chat#formatting) for a full list or possible replacements     | `chat.replacment`  |

### Config

File: `Modules/Chat.json`

| Name              | Description                                                                                                                                                             |
|-------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `defaultChannel`  | Channel to set new users to upon login.                                                                                                                                 |
| `chatFormat`      | Formatting for how to display chat, See [#formatting](/modules/chat#formatting) for a full list of possible formatting codes                                            |


### Formatting

#### Config Replacements
| Find            | Replace                                                                                                                                                             | Example           |
|-----------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------|
| `%USERNAME%`    | The user's username, ignores nicknames                                                                                                                              | Wurmatron         |
| `%NAME%`        | The user's name as represented by the server, nickname, username.                                                                                                   | *Wurm             |
| `%DIMENSION%`   | The Dimension the user is currently in                                                                                                                              | 0                 |
| `%RANK_PREFIX%` | The users rank prefix with the highest display priority                                                                                                             |[Default]          |
| `%RANK_SUFFIX%` | The users rank suffix with the highest display priority                                                                                                             | &3                |
| `%CHANNEL_PREFIX%` | The users rank suffix with the highest display priority                                                                                                          | Local             |

#### Chat Replacements
| Find            | Replace                                                                                                                                                             | Example           |
|-----------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------|
| `{BALANCE}`     | The users current balance, in the servers default currency                                                                                                          | 500.25 Coinz      |
| `{EXP}`         | The amount of exp. the user currently has                                                                                                                           | 520 exp           |
| `{LEVEL}`       | The amount of exp. level's the user currently has                                                                                                                   | lvl 52.           |
| `{PLAY_TIME}`   | The users playtime (total)                                                                                                                                          | 6d 5h 20m         |
| `{TIME}`        | The users playtime (local server only)                                                                                                                              | 3d 9h 5m          |
| `{REWARDS}`     | The amount of reward points the user has.                                                                                                                           | 52 points         |
| `{POINTS}`      | The amount of reward points the user has.                                                                                                                           | 42 points         |
| `{LANGUAGE}`    | The users current set language key                                                                                                                                  | en_us             |
| `{LANG}`        | The users current set language key                                                                                                                                  | en_us             |
