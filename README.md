[![N|Solid](https://cloud.githubusercontent.com/assets/10060860/18985121/f7778c04-8713-11e6-9190-11505c8c596c.png)](http://elasticmatch.com/)

## A Generic Restful Match Engine

## http://elasticmatch.com

#### elastic-match is a generic matching engine. Features include:

* Flexible matching rules
  * Support multi-level matching rules each with priority
  * Support 1-1 and M-M matching
  * Support match with threshold
  * Ability to configure existing famous matching algorithms for each matching rule
  * Ability to configure user-define matching algorithms

* Document oriented
  * Schema less records
  * Ability to index any JSON records

* Multi Tenant
  * Support multiple match config per instance
  * Data and matches are isolated per match config

* Restful
  * Rest API to create match config
  * Rest API for records indexing
  * Rest API to check system health

* Reliable and Dynamic
  * Real time matching
  * Indexing and Matching can run in parellel

* Transparant
  * Angular JS UI to see matching status and system health
  * UI to configure matching rules 

* Open Source under the Apache License, version 2 ("ALv2")

## Getting Started

Elastic match is design to keep things simple yet powerful. It will take less than 5 mins to do complete setup on your laptop so that you can do your first match right away.

### Requirements

1. Java 8 or adove 
2. Mongodb 3.2 or above

### Installation

* Download tar from [https://www.elasticmatch.com/downloads](http://elastic.site88.net/Download/) and unzip the Elasticmatch official distribution.
* Run ```bin/start.sh``` on unix, or ```bin\start.bat``` on windows.
* Open browser and type ```http://localhost:12273/admin-ui/src/app/index.html```

### UI
[![N|Solid](https://cloud.githubusercontent.com/assets/10060860/18985121/f7778c04-8713-11e6-9190-11505c8c596c.png)](http://elasticmatch.com/)

### Indexing

Let's insert some records for matching. First, let's create a match config.

:soon:

### Matching

:soon:

### Multi Tenant

<pre>
curl -XPUT 'http://localhost:9200/kimchy/info/1?pretty' -d '{ "name" : "Shay Banon" }'
</pre>
Search (and similar operations) are multi index aware. This means that we can easily search on more than one
index (twitter user), for example:

### System health

Let's face it, things will fail....

:soon:

### Where to go from here?

We have just covered a very small portion of what elastic matching can do. For more information, please refer to the "elasticmatch.com":http://www.elasticmatch.com website. General questions can be asked on the "Elastic Match forum":https://elasticmatch.com/blog. The Elasticmatch GitHub repository is reserved for bug reports and feature requests only.

### Building from Source

Elasticmatch uses "maven":https://maven.apache.org/ for its build system. You'll need to have a modern version of Maven installed.

In order to create a distribution, simply run the @mvn install@ command in the cloned directory.

The distribution for each project will be created under the @build/distributions@ directory in that project.

# License

<pre>
This software is licensed under the Apache License, version 2 ("ALv2"), quoted below.

Copyright 2009-2016 Elasticmatch <https://www.elasticmatch.com>

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
</pre>
