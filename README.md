# mod-spine-o-matic

A middleware module for providing Alma like XML responses for use with [SpineOMatic](https://github.com/ExLibrisGroup/SpineOMatic).

Copyright (C) 2018 The Open Library Foundation

This software is distributed under the terms of the Apache License, Version 2.0.
See the file ["LICENSE"](LICENSE) for more information.

## API

There is a single endpoint `/spine-label?identifier={item barcode|item hrid}`.

## Sample use with SpineOMatic

### Both enumeration and chronology populated
| barcode      | hrid          |
| ------------ | ------------- |
| A14811944997 | it00000000005 |
| A14814371046 | it00000000006 |
| A14812022756 | it00000000007 |

barcode: A14811944997


### enumeration null, chronology populated
| barcode      | hrid          |
| ------------ | ------------- |
| A14809369076 | it00000663749 |
| A14809369084 | it00000663750 |
| A14809369092 | it00000663751 |

### enumeration populated, chronology null
| barcode      | hrid          |
| ------------ | ------------- |
| A14809356455 | it00000663732 |
| A14809356463 | it00000663733 |
| A14809356471 | it00000663734 |

### barcode is null, so input hrid
| barcode      | hrid          |
| ------------ | ------------- |
|              | it00000165938 |
|              | it00000000042 |
|              | it00000165945 |

### location requiring special label printing
| barcode      | hrid          | location_name |
| ------------ | ------------- | ------------- |
| A14823400064 | it00000498821 | mtxt,film     |
| A14823883296 | it00000498822 | mtxt,film     |
|              | it00000831473 | cush,wode     |
|              | it00000748781 | cush,wode     |
| A14809478998 | it00000667357 | wein          |
| A14809479009 | it00000667360 | wein          |
|              | it00002821308 | cush,afri     |
|              | it00002821372 | cush,afri     |
|              | it00002821509 | cush,afri     |
|              | it00002933250 | cush,asia     |
|              | it00002933253 | cush,asia     |
|              | it00002690836 | cush,asia     |

## Additional Information

Other [modules](https://dev.folio.org/source-code/#server-side).

Other FOLIO Developer documentation is at [dev.folio.org](https://dev.folio.org/).

## Docker deployment

When deploying docker, be sure to set the appropriate ports for your environment (make the guest port matches the port specified in `src/main/resources/application.yml` file).
In the example below, the `-p 9000:9000` represents `-p [host port]:[guest port]` with *guest* referring to the docker image and *host* referring to the system running docker.

```
docker build -t folio/mod-spine-o-matic .
docker run -d -p 9000:9000 folio/mod-spine-o-matic
```

### Publish docker image

```
docker login [docker repo]
docker build -t [docker repo]/folio/mod-spine-o-matic:[version] .
docker push [docker repo]/folio/mod-spine-o-matic:[version]
```

### Issue tracker

See project [FOLIO](https://issues.folio.org/browse/FOLIO)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker/).
