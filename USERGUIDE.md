# CARELender

###Welcome to CARELender<sup>TM</sup>
*a task manager for a people manager*

##Introduction
This program is meant to help you efficiently manage your work life and personal life. It's built around a keyboard only input so all functions are fully accessible only through the keyboard. 

The first thing you will see when you open the program is the user interface. There, you will see these main components.

 No. | Item | Description
---  | --- | ---
 1   | Command box    | This is where you input everything to use the app                 
 2   | Program output | This gives you any information regarding the commands you entered 
 3   | Calendar View  | This gives you an overview of the upcoming weeks                  
 4   | Task View      | This shows you all your upcoming tasks and events 
 
This interface you see is just the start screen. There will be different interfaces as you further explore the program.
This program is split into three main interfaces. 

 No. | Screen | Description
---  | --- | ---
 1   | Startup   | This screen displays both your personal and work schedule.         
 2   | Personal | This screen displays primarily your personal schedule.
 3   | Work  | This screen displays primarily your work schedule.         
 
 Each of these screens is specially designed to cater to your needs.

##Basic Commands

To use the application, you will have to type the commands. Don't worry about memorising all the commands, the application will automatically help you to complete the commands if you forget.
Note: Anything in the [] means it's optional. () refers to a parameter.

Command | Usage | Description
---  | --- | ---
 help   | help [command] | This will give you a list of commands you can use.<br/>If you give it a command, it will tell you how to use that command
 add  | add (eventname) of [type] \<from/to\>/on/at [eventdatetime] [priority]  |  This adds an item to your schedule.    
 edit | change (fieldname) of (eventname) to (newvalue) | This lets you edit the details of an event.
 list  | list [type]  | This lists all future events <br/> If type is defined, it will list all of that type only.  
 switch   | switch [screen] | This toggles between the personal and work screen.</br>If screen is given, then it switches to that screen.
 delete | delete (eventname) | This deletes the given event.
 complete | complete (eventname) | This marks an event complete.
 
##Advanced Commands

These commands are there for those who want to get the most out of the program.

Command | Usage | Description
---  | --- | ---
settings   | settings | This brings you to the settings menu
silent   | silent [time] | This silences all reminders for the stipulated timing
freeup   | freeup (datefrom) [dateto] | This starts the free up process and removes everything in the given time span.<br/>You will be asked to select new dates for each affected item.
select   | select (datefrom) [dateto] | This selects all events in the given time span.
move     | move | This starts the move process. You will be asked where to put each of the new events.
postpone | postpone (by) | All selected items will be postponed by the stipulated time. Yuo will be asked to resolve any conflicts.
delete   | delete | This deletes all events in the selected timespan

##Settings

Command | Usage | Description
---  | --- | ---
startscreen   | startscreen (screen) | This sets the screen on start
silenttime   | silenttime (time) | The default amount of time for the silent feature.

