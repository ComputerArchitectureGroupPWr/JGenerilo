JGenerilo
=========

JGenerilo is tool for creating floorplans for thermal emulation
on Virtex-5 110t devices.

-------------
BUILD PROJECT
-------------

1. You have to build the project using maven. If you don't have
   already installe maven on your OS please do it.

In case of debian style distribution:

    $ sudo apt-get install maven
    
2. The JGenerilo project use some dependencies, unfortunetly
   jambi qt isn't available in the offcial maven repository
   so you have to install it by yourself. To do this please
   follow instructions below:

    a) First go to Qt Jambi sourge forge 
       [site](http://sourceforge.net/projects/qtjambi/) and 
       download proper version depend on your system type. 
       Please note that you should use the 
       [4.6.3](http://sourceforge.net/projects/qtjambi/files/4.6.3/) 
       release for JGenerilo.
       
    b) After you get proper jar file with Qt Jambi please add
       it to your local maven repository:
       
        $ mvn install:install-file "-Dfile=qtjambi-4.6.3.jar" "-DgroupId=com.trolltech" "-DartifactId=qt" "-Dversion=4.6.3" "-Dpackaging=jar" "-DgeneratePom=true"
        
3.  After all dependencies are installed you can build the final
    JGenerilo package. To do thus please type two times:

        $ mvn compile assembly:single
        
4.  After compilation and building a package in **target** you
    should find several archives and one jar file.

----------------------
PRE INSTALLATION STEPS
----------------------

Before you start to use the JGenerilo tool please download and
install rapidsmith framework in version 0.5.0. Folow the 
instructions on thiers website at: 
[rapidsmith.sourceforge.net](http://rapidsmith.sourceforge.net) 

-----------
INSTALATION                           
-----------

When the compiling and building stage is done you can install
JGenerilo in your system.

1.  On this stage in the target directory you should find several
    archives *JGenerilo-VERSION-SNAPSHOT-bin*. Plaese extract
    one of it eg.

        $ unzip JGenerilo-0.1a-SNAPSHOT-bin.zip
    
    
2.  For installation was prepared a python script install.py. 
    This script install the JGenerilo jar with dependencies in 
    **\usr\local\JGenerilo** and creates execution script which 
    is added to the path. To install the JGenerilo please type 
    in command shell:

        $ sudo python install.py 

3.  After installation reload your shell to update PATH e.g.:
    
        $ bash



---------
EXECUTION
---------

To execute the JGenerilo help tool just write in command shell:

    $ JGenerilo -h
    
-----
USAGE
-----
