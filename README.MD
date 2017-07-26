# Slidead app

Already use jobscheduler to download API list 

this is sample json of API list

```
{
  "images": [
    {
      "id": 1,
      "title": "House of Cards",
      "author": "typicode",
      "imageurl": "http://cdn3.nflximg.net/images/3093/2043093.jpg"
    },
    {
      "id": 2,
      "title": "Hannibal",
      "author": "typicode",
      "imageurl": "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg"
    },
    {
      "id": 3,
      "title": "Game of Thrones",
      "author": "typicode",
      "imageurl": "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg"
    },
    {
      "id": 4,
      "title": "Big Bang Theory",
      "author": "typicode",
      "imageurl": "http://tvfiles.alphacoders.com/100/hdclearart-10.png"
    }
  ]
}
```

The scheduler used Firebase JobDispatcher

```https://github.com/firebase/firebase-jobdispatcher-android```

The scheduler will run every several hour, if condition inside startjob  meet `current_time` == `03.00 AM` then scheduler will retrieve new List of url image.   


The json result will be saved to file in android storage. When the file is exist json reader will read the file and parse based on json properties given.


