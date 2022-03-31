# CollegeAppBackend

Backend and other services for [CollegeAppIIITN](https://github.com/4shutosh/CollegeAppIIITN)
using [Ktor](https://github.com/ktorio/ktor) 💙

#### Plugins Used:
- Server: Netty
- Authentication: JWT Token
- Serialization: Ktor Serialization & GSON
- EngineMain as `main` runner
- [Koin](https://insert-koin.io/docs/reference/koin-ktor/ktor/) for dependency Injection
- MongoDB as primary Database: [KMongo](https://litote.org/kmongo/) (a kotlin toolkit for mongo)
- Kotlin Coroutines
- CORS
- Call Logging: Ktor Server Call Logging
- Content Negotiation
- Gradle KTS

#### Hosted on Heroku (Official [documentation](https://ktor.io/docs/eap/heroku.html))

Steps other than the documentation to host on heroku:

- make sure heroku CLI is installed
- setup env variables
- adding the Procfile for the script along with the correct `build/install/<package-name>` location

````
MONGO_URL = <mongo db hosted connect url, atlas, aws anything>
JWT_SECRET = <some jwt secret here>
PORT = <4200>

and some other env variables if you want
````

- deployment works only on `main` and `master` branches on heroku
