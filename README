JGenerilo - tool for creating floorplans for thermal emulation

===============================================================
|                       INSTALATION                           |
===============================================================

For installation was prepared a python script install.py. This
script install the JGenerilo jar with dependencies in 
\usr\local\JGenerilo and creates execution script which is 
added to the path. To install the JGenerilo please write in
command shell:

    $ sudo python install.py 

After installation reload your shell to update PATH e.g.:
    
    $ bash

Before you start to use the JGenerilo tool please download and
install rapidsmith framework in versioni 0.5.0. Folow the 
instructions on thiers website at: 
http://rapidsmith.sourceforge.net 

===============================================================
|                        EXECUTION                            |
===============================================================

To execute the JGenerilo help tool just write in command shell:

    $ JGenerilo -h

===============================================================
|                      PROJECT HEAT NOTES                     |
===============================================================

Paczka ze snapshotem JGenerilo do pobrania przez ssh z 
dream:ProjectHeatToolsSnapshots/JGenerilo-0.1a-SNAPSHOT-bin.zip

W celu zbudowania projektu w Maven należy zainstalować mavena
np.

    $ sudo apt-get install maven

oraz dołożyć nie będącą w repozytorium mavana paczkę qtjambi

    $ mvn install:install-file "-Dfile=qtjambi-4.6.3.jar" "-DgroupId=com.trolltech" "-DartifactId=qt" "-Dversion=4.6.3" "-Dpackaging=jar" "-DgeneratePom=true"

Po ukończeniu należy stworzyć paczkę z JGenerilo

    $ mvn compile assembly:single

A następnie wejść do katalogu target, rozpakować paczkę
np. zip i zainstalować używając skryptu install.py

    $ cd target
    $ unzip JGenerilo-0.1a-SNAPSHOT-bin.zip
    $ cd JGenerilo-0.1a-SNAPSHOT
    $ sudo python install.py

JGenerilo mozna już odpalić używając:

    $ JGenerilo

Opcjonalnie przeładować shell'a

-- 

16W - default program
11.80 W - idling with SimCore + some heaters turned off
with 110 RO1 heaters: 12.20
with 4900-1450 RO1 heaters: 
10% : 12.38
20% : 13.18
30% : 13.98
40% : 14.83
60% : 16.37
-- 

Virtex - tylko parzyste kolumny (nieparzyste mają interconnect tile)
Co któraś kolumna parzysta zawiera RAM (Generilo wywali error)
Zakres kolumn/wierszy: 

add task for PW: FM for V5, RO1-RO7 heaters, SHR16/32 heaters, combined heaters,
routing heaters

 impact -batch _impact.cmd
