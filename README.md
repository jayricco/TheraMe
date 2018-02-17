# Thera-Me
Software Engineering, Spring 2018, Team 2

## Setup

#### Installing FFmpeg

FFmpeg is used for video conversion, verification, thumbnail generation, etc. It needs to be installed on the local 
system in order to work properly. To do this on macOS with HomeBrew, you can use the following monolith of a command:

    brew install ffmpeg --with-chromaprint --with-fdk-aac --with-libass --with-librsvg --with-libsoxr --with-libssh 
    --with-tesseract --with-libvidstab --with-opencore-amr --with-openh264 --with-openjpeg --with-openssl 
    --with-rtmpdump --with-rubberband --with-sdl2 --with-snappy --with-tools --with-webp --with-x265 --with-xz 
    --with-zeromq --with-zimg

This will install FFmpeg with all of the optional plugins, so that we can support the widest range of video formats.

#### Installing PostgreSQL

Install using HomeBrew:

    brew install postgresql
    
If you want to run it as a service, use the command displayed after installation is completed.

Once Postgres is installed, you must create the `therame` database and user. To do this, first you must enter the psql
shell using:

    psql -d postgres

Then, to create the database/user, run the following queries:

    CREATE DATABASE therame;
    
    CREATE USER therame WITH PASSWORD 'hulkhogan' SUPERUSER;
    
`SUPERUSER` isn't necessary, per se, but it makes thing easier if we just give the user all permissions. If you don't
want to do that, you're on your own. Also, if you change the password you'll also need to change it in the 
`/src/main/resources/application.properties` file.

The `therame` database will be populated with tables, etc. automatically when you run the Spring server.

#### Importing Into IntelliJ

First, clone this repository into your preferred directory with `git clone git@github.com:jayricco/Thera-Me.git`. Next,
open `pom.xml` in IntelliJ as a project. This should download all necessary dependencies for you. If there are any
pop-ups telling you to enable some integration or something, you'll likely want to accept them.

By default, only the compiled binaries are downloaded for libraries, but the sources/docs are useful for development. To
download those, go to `View -> Tool Windows -> Maven Projects` then, in the window that appears, click on the download
button on the toolbar and select `Download Sources and Documentation`.

We also make use of Lombok, which requires a special IntelliJ plugin to function properly. To install this, go to 
`Preferences... -> Plugins -> Browse Repositories` and search for `Lombok Plugin`. Once you install and restart
IntelliJ, it should prompt you to enable annotation processing, do that.