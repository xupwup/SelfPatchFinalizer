SelfPatchFinalizer
==================

Helper to allow overwriting the LoLPatcher jar. (Avoid file locking)


When self-patching, one needs to overwrite the current jar file. 
However, that jar is currently locked because the program is still running. This program avoids this problem.
When a patch has been downloaded, updates should be written to .new files. Then, the patcher should call this
program and immediately exit. The patcher jar will then be overwriten and called again.
