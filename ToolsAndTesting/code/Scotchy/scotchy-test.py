# Imports the monkeyrunner modules used by this program
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice

# Connects to the current device, returning a MonkeyDevice object
device = MonkeyRunner.waitForConnection()

# Installs the Android package. 
device.installPackage('ScotchyApp/out/production/Scotchy/Scotchy.apk')

# sets a variable with the package's internal name
package = 'net.opgenorth.prdc11.intro'

# sets a variable with the name of an Activity in the package
activity = 'net.opgenorth.prdc11.scotchy.Scotchy'

# sets the name of the component to start
runComponent = package + '/' + activity

# Runs the component
device.startActivity(component=runComponent)
MonkeyRunner.sleep(5)

# Takes a screenshot, writes it to a file.
result = device.takeSnapshot()
result.writeToFile('ScotchyApp/shot1.png','png')


# Presses the Menu button
#device.press('KEYCODE_MENU','DOWN_AND_UP')
device.touch(78,93, MonkeyDevice.DOWN_AND_UP)

MonkeyRunner.sleep(15)
result=device.takeSnapshot()
result.writeToFile('ScotchyApp/shot2.png', 'png')


