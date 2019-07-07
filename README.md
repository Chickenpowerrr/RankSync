# RankSync
This plugin has been made to give people an easy option to synchronize
ranks between Minecraft and Discord. This simplifies for example purging staffmembers,
giving donators their ranks on Discord.

Please note that the ranks will be given on Discord, based on the Minecraft ranks, 
NOT THE OTHER WAY AROUND. People will only get their rank on Discord when the ranks
they have are included in the config.yml.

## Support
### Data
To store the data, right now we support the following ways of store:
- MySQL
- Yaml

more ways to store data might get added in the future.

### Spigot
Right now RankSync supports the following permission systems:
- Vault
- LuckPerms

they will be detected automatically when your server runs one of these two plugins.

### Bungeecord
Right now RankSync supports the following permission systems:
- LuckPerms

it will be detected automatically when your server runs this plugin.

## Prerequisites
Those are the things you'll need if you want to compile this project for yourself
- Maven
- Git
- JDK 8+


## License
This project is licensed under the MIT license - see the [LICENSE](LICENSE.md) file for details
