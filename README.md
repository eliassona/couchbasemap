# couchbasemap

Represents redis as a clojure persistent map.
Note! The bucket used for the map must have a view called allkeys for it to work.
Like this:
```javascript
function (doc, meta) {
  if(meta.type == "json") {
     emit(null);
  }
}
```

## Usage

FIXME

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
