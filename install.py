#!/usr/bin/python2.7
import sys
import os
import shutil
from subprocess import call
import getpass


prefix = '/usr/local/JGenerilo'
bin_prefix = '/usr/local/bin/JGenerilo'
home_prefix = str.format("/home/{}", getpass.getuser())
jar_file = [f for f in os.listdir('.') if os.path.isfile(f) and 'jar' in f][0]
rc_file = "data/.jgenerilorc"

try:
    os.mkdir(prefix)
except OSError:
    pass

shutil.copy(jar_file, prefix)

run_script = """ #!/usr/bin/bash
java -jar {}/{} $@
""".format(prefix, jar_file)

run_file = file(bin_prefix, 'w+')
run_file.write(run_script)
run_file.close()

os.chmod(bin_prefix,711)

print "JGenerilo installed in {prefix_dir}. To execute program please reload your shell" \
        " and write JGenerilo.".format(prefix_dir=prefix)

print "Import heaters to mongo"
heaters = ["data/virtex5elements/heaters/FF.json", "data/virtex5elements/heaters/RO1.json",
           "data/virtex5elements/heaters/RO3.json", "data/virtex5elements/heaters/RO7.json"]

for heater in heaters:
    heaterImportCmd = ["mongoimport", "-d", "JGeneriloDB", "-c", "virtex5heaters", "--type", "json", "--file",
                       heater, "--jsonArray"]
    print "Adding heater %s to mongo" % heater
    call(heaterImportCmd)

print "Adding therometer RO7 to mongo"
thermometerImportCmd = ["mongoimport", "-d", "JGeneriloDB", "-c", "virtex5thermometers", "--type", "json", "--file",
                        "data/virtex5elements/thermometers/RO7.json", "--jsonArray"]

call(thermometerImportCmd)

print "Create .jgenerilorc in home directory"
shutil.copy(rc_file, home_prefix)
