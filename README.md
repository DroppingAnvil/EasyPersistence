# EasyPersistence  
[![CodeFactor](https://www.codefactor.io/repository/github/droppinganvil/easypersistence/badge)](https://www.codefactor.io/repository/github/droppinganvil/easypersistence)  

Object persistence library designed for easy quick managed persistence automation
## Setup
coming soon
## Formats
EasyPersistence was designed so we can add support for file formats with ease!
Currently Supported:
- JSON
- YAML
- Plans to add MySQL DB automation
## How it works
EasyPersistence uses an Identifier for each PersistenceObject this Identifier contains 3 main metrics, a projectID, classID, and objectID this allows you to have an endless amount of Objects attached to each class registered. 
Now remember all you have to do is make sure to use an objectID that will be unique to that object only and the rest is handled by EasyPersistence if your project and classes are registered properly.
### Object Creation
You might be asking yourself "how will I implement my own objects?" and the answer is quite simple all you need to do is register a ComplexBuilder or just a Builder that will handle the transition to and from the String(s), if one is not found for the Object the system will attempt to cast other Builders and ComplexBuilders to the Object that needs to be built.
Default Objects: (Objects that will be recognized without modification)
- String
- Integer
- Boolean
- Double
- Collection
## TODO
In retrospective Strings should not have been used like they were in the builders, it is functional but inneficient this is a planned change.  

Add cryptography functions, most likely based on OpenPGP  

Implementation guide  

Better way of adding types
