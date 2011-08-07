ItemLogger plugin v1.031	
by Pandemoneus	
https://github.com/Pandemoneus

Requirements:
----------------
- mySQL or SQLite (optional)

How to install:
----------------
1. Copy 'ItemLogger.jar' into your 'plugins/' folder.	
2. Start your server to create a config file.	
3. Edit the config file in 'plugins/ItemLogger/config.yml'.

How to uninstall:
-----------------
1. Delete 'ItemLogger.jar'.	
2. Delete the folder 'plugins/ItemLogger'.

Editable options:
-----------------
UseFlatFile: [true, false] - determines whether a regular flat file(*.txt) should be used, if true, the plugin will not use MySQL or SQLite	
MySQL.enabled: [true, false] - determines whether mySQL should be used (if UseFlatFile = false), if false, SQLite will be used	
MySQL.host: [string] - insert the name of your host here	
MySQL.username: [string] - insert your username here	
MySQL.password: [string] - insert your password here	
MySQL.database: [string] - determine the used database here	

To sum it up:	
If you want	
- Flat files: UseFlatFile = true	
- MySQL: UseFlatFile = false, MySQL.enabled = true	
- SQLite: UseFlatFile = false, MYSQL.enabled = false

Permission nodes:
-----------------
itemlogger.help //makes help command available	
itemlogger.log.clearlogs //makes clearlogs command available	
itemlogger.log.deletelog //makes deletelog command available

Commands:
-----------------
itemlogger (alias: il) - shows the help	
itemlogger clearlogs (alias: il cl) - deletes all logs	
itemlogger deletelog [player] (alias: il dl [player]) - deletes the log of the [player]