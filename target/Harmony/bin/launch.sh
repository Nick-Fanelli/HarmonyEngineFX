#!/bin/sh
JLINK_VM_OPTIONS="-Djavax.net.debug=all"
DIR=`dirname $0`
$DIR/java $JLINK_VM_OPTIONS -m com.harmony/com.harmony.engine.Launcher $@
