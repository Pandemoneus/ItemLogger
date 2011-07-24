ItemLogger plugin v1.031<br>
by Pandemoneus<br>
https://github.com/Pandemoneus

Requirements:
----------------
- mySQL or SQLite (optional)

How to install:
----------------
1. Copy 'ItemLogger.jar' into your 'plugins/' folder.<br>
2. Start your server to create a config file.<br>
3. Edit the config file in 'plugins/ItemLogger/config.yml'.

How to uninstall:
-----------------
1. Delete 'ItemLogger.jar'.<br>
2. Delete the folder 'plugins/ItemLogger'.

Editable options:
-----------------
UseFlatFile: [true, false] - determines whether a regular flat file(*.txt) should be used, if true, the plugin will not use MySQL or SQLite<br>
MySQL.enabled: [true, false] - determines whether mySQL should be used (if UseFlatFile = false), if false, SQLite will be used<br>
MySQL.host: [string] - insert the name of your host here<br>
MySQL.username: [string] - insert your username here<br>
MySQL.password: [string] - insert your password here<br>
MySQL.database: [string] - determine the used database here<br>

To sum it up:<br>
If you want<br>
- Flat files: UseFlatFile = true<br>
- MySQL: UseFlatFile = false, MySQL.enabled = true<br>
- SQLite: UseFlatFile = false, MYSQL.enabled = false

Permission nodes:
-----------------
itemlogger.help //makes help command available<br>
itemlogger.log.clearlogs //makes clearlogs command available<br>
itemlogger.log.deletelog //makes deletelog command available

Commands:
-----------------
itemlogger (alias: il) - shows the help<br>
itemlogger clearlogs (alias: il cl) - deletes all logs<br>
itemlogger deletelog [player] (alias: il dl [player]) - deletes the log of the [player]