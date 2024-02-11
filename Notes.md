# Notes about automatic project generation

## Symbolic Links

The following symbolic links were built in the root folder for this project.

* The 'sourcedir' symbolic link points to the folder where the project's Java source files are placed.

* The 'testdir' symbolic link points to the folder where the project's Java test files are placed.

## Next Steps

 1. **Choose a repo/project name**.
    Edit pom.xml and add public-facing repo/project name to the build
    product 'artifactId'. Lower case letters and hyphens only. Be concise but 
    descriptive. This name is consumed for dependencies and should be unique.
 2. **Describe your project with a name**.
    Edit pom.xml and add public-facing description to 'the build 
    product 'name'. Use normal Caps-case English with spacing between
    words. Make it human-readable. This name is not used programmatically.
    For example, "Pizza Business Emulation App".
 3. **Create a package**.
    Decide on your first package name. A Java package organizes related
    classes and interfaces under a namespace. Use reverse domain naming,
    e.g. io.temporal.myfirstproject or io.temporal.my_first_project.
    Use lowercase and underscores for the name.
    In Java, package names correspond to directory structure. Create a directory
    in the convenience 'sourceDir'. It will house all your source files for
    this package. If the package is 'io.temporal.myfirstproject', the directory
    is named 'myfirstproject'.
 4. **Create a hop**.
    Create a way to hop back to the root of your project like: 
    'alias hop pushd ../../../../../..'
    Do not use symbolic links. They will create infinite loops with
    Maven compilation.
 5. **Create test infrastructure**.
    Optionally repeat steps 4 and 5 for tests:
    project-folder/src/test/java/io/temporal
 6. **Write code**.
    Add java content to your new project folder. A sample has already
    been added on your behalf.
 7. Edit the Makefile run to build your targets from 
    io.temporal.YOURPACKAGENAME.YOURPACKAGEMAINCLASS to the actual 
    package name and main class name. Change 'run' in the 'PHONY' and 'run:' 
    lines to more meaningful names so you can use the Makefile 
    for multiple targets.
