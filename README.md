# CollegeAppBackend

Backend and other services for [CollegeAppIIITN](https://github.com/4shutosh/CollegeAppIIITN)
using [Ktor](https://github.com/ktorio/ktor) 💙

#### Hosted on Heroku (Official [documentation](https://ktor.io/docs/eap/heroku.html))

Steps to host on heroku:

- make sure heroku CLI is installed
- setup env variables

````
MONGO_URL = <mongo db hosted connect url, atlas, aws anything>
JWT_SECRET = <some jwt secret here>
PORT = <4200>

and some other env variables if you want
````

- deployment works only on `main` and `master` branches on heroku
