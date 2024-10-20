# EasyPersistence
Object persistence system designed for easy quick managed persistence automation
## Setup
coming soon
## File Formats
EasyPersistence was designed so we can add support for file formats with ease!
Currently Supported:
- JSON
- YAML
## Persistence actually desinged for an OOPL
EasyPersistence uses an Identifier for each PersistenceObject this Identifier contains 3 main metrics, a projectID, classID, and objectID this allows you to have an endless amount of Objects attached to each class registered. 
This is a great way to real time load users data an example Identifier would be EasyPersistance, Example, DroppingAnvil.
Now remember all you have to do is make sure to use an objectID that will be unique to that object only and the rest is handled by EasyPersistence if your project and classes are registered properly.
### Object Creation
You might be asking yourself "how will I implement my own objects?" and the answer is quite simple all you need to do is register a ComplexBuilder or just a Builder that will handle the transition to and from the String(s), if one is not found for the Object the system will attempt to cast other Builders and ComplexBuilders to the Object that needs to be built.
Default Objects: (Objects that will be recognized without modification)
- String
- Integer
- Boolean
- Double
- Collection
