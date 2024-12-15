@echo off
setlocal enabledelayedexpansion

REM Specify the folder where the files are located
set "source_folder=C:\Users\fujitsu\AndroidStudioProjects\AudioRecorder\app\src\main"

REM Specify the output file name
set "output_file=combined.txt"

REM Navigate to the source folder
cd /d "%source_folder%"

REM Delete the output file if it already exists
if exist "%output_file%" del "%output_file%"

REM Define file_list line by line using a `for /f` loop
for %%f in (
    "AndroidManifest.xml"
    "java\com\bakrxc\audiorecorder\MainActivity.java"
    "java\com\bakrxc\audiorecorder\AudioNote.java"
    "java\com\bakrxc\audiorecorder\AudioNoteRecorder.java"
    "java\com\bakrxc\audiorecorder\AudioNotesManager.java"
    "java\com\bakrxc\audiorecorder\AudioNotePlayer.java"
    "java\com\bakrxc\audiorecorder\ArrayAdapterUtil.java"
    "res\layout\activity_main.xml"
    "res\layout\audio_item.xml"
) do (
    REM Check if the file exists before attempting to type it
    if exist "%%f" (
        REM Add the file name at the start of the section
        echo %%f: >> "%output_file%"
        
        REM Append the content of the file (accounting for subfolders)
        type "%%f" >> "%output_file%"
        
        REM Add new lines to separate file contents
        echo. >> "%output_file%"
        echo. >> "%output_file%"
    ) else (
        echo File not found: %%f >> "%output_file%"
    )
)

echo Files have been combined into %output_file%.
pause
