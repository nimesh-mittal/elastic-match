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
[![N|Solid](https://cloud.githubusercontent.com/assets/10060860/18985680/b2f22898-8716-11e6-9ac5-944173ba8366.png)](http://elasticmatch.com/)

### Indexing

Let's insert some records for matching. First, let's create a match config.

:soon:

### Matching

Inorder to match records it is important to create correct match configuration. Elasticmatch provides a simple interface to create complex matching rules. It also helps in setting up other elasticmatch configurations like file watcher, mongodb settings using ui.

[![N|Solid](https://cloud.githubusercontent.com/assets/10060860/18986098/f30a76a4-8718-11e6-8a90-96ad3dda0a77.png)](http://elasticmatch.com/)

### Multi Tenant

Create multiple match config like above. Each match config runs independently and maintains its own state.

### System health

Let's face it, things will fail.... It is immportant to have tools that monitor system health and provide the summary of whats happening inside a cluster.

Elasticmatch provides a UI to monitor status of system processes. It also provides summary of matching engine state like number of records matched group by matching rule and count.

[![N|Solid](https://cloud.githubusercontent.com/assets/10060860/18985943/268c4288-8718-11e6-8a61-ca942b28c77a.png)](http://elasticmatch.com/)

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
