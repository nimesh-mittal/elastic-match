# elastic-match

## A Generic Restful Match Engine

## http://elasticmatch.com

#### elastic-match is a generic matching engine. Features include:

* Flexible Matching rules
  * Support multi-level matching rules each with priority
  * Support 1-1 and M-M matching
  * Support match with threshold
  * Ability to configure existing famous matching algorithms
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
  * Real time match
  * Indexing and Matching can run in parellel

* Transparant
  * Angular JS UI to see matching status and system health
  * UI to configure matching rules 

* Open Source under the Apache License, version 2 ("ALv2")

## Getting Started

Elastic match is design to keep things simple yet powerful. It will take less than 5 mins to do complete setup on your laptop so that you can do your first match right away.

### Requirements

1. You need to have Java 8 or adove installed. 
2. Any version of Mongodb

### Installation

* Download tar from https://www.elasticmatch.com/downloads and unzip the Elasticmatch official distribution.
* Run @bin/start.sh@ on unix, or @bin\start.bat@ on windows.
* Open browser and type http://localhost:12273/admin-ui/src/app/index.html

### Indexing

Let's try and index some twitter like information. First, let's create a twitter user, and add some tweets (the @twitter@ index will be created automatically):

<pre>
curl -XPUT 'http://localhost:9200/twitter/user/kimchy?pretty' -d '{ "name" : "Shay Banon" }'

curl -XPUT 'http://localhost:9200/twitter/tweet/1?pretty' -d '
{
    "user": "kimchy",
    "post_date": "2009-11-15T13:12:00",
    "message": "Trying out Elasticsearch, so far so good?"
}'

curl -XPUT 'http://localhost:9200/twitter/tweet/2?pretty' -d '
{
    "user": "kimchy",
    "post_date": "2009-11-15T14:12:12",
    "message": "Another tweet, will it be indexed?"
}'
</pre>

Now, let's see if the information was added by GETting it:

<pre>
curl -XGET 'http://localhost:9200/twitter/user/kimchy?pretty=true'
curl -XGET 'http://localhost:9200/twitter/tweet/1?pretty=true'
curl -XGET 'http://localhost:9200/twitter/tweet/2?pretty=true'
</pre>

### Matching

Mmm search..., shouldn't it be elastic?
Let's find all the tweets that @kimchy@ posted:

<pre>
curl -XGET 'http://localhost:9200/twitter/tweet/_search?q=user:kimchy&pretty=true'
</pre>

We can also use the JSON query language Elasticsearch provides instead of a query string:

<pre>
curl -XGET 'http://localhost:9200/twitter/tweet/_search?pretty=true' -d '
{
    "query" : {
        "match" : { "user": "kimchy" }
    }
}'
</pre>

Just for kicks, let's get all the documents stored (we should see the user as well):

<pre>
curl -XGET 'http://localhost:9200/twitter/_search?pretty=true' -d '
{
    "query" : {
        "match_all" : {}
    }
}'
</pre>

We can also do range search (the @postDate@ was automatically identified as date)

<pre>
curl -XGET 'http://localhost:9200/twitter/_search?pretty=true' -d '
{
    "query" : {
        "range" : {
            "post_date" : { "from" : "2009-11-15T13:00:00", "to" : "2009-11-15T14:00:00" }
        }
    }
}'
</pre>

There are many more options to perform search, after all, it's a search product no? All the familiar Lucene queries are available through the JSON query language, or through the query parser.

### Multi Tenant - Indices and Types

Maan, that twitter index might get big (in this case, index size == valuation). Let's see if we can structure our twitter system a bit differently in order to support such large amounts of data.

Elasticsearch supports multiple indices, as well as multiple types per index. In the previous example we used an index called @twitter@, with two types, @user@ and @tweet@.

Another way to define our simple twitter system is to have a different index per user (note, though that each index has an overhead). Here is the indexing curl's in this case:

<pre>
curl -XPUT 'http://localhost:9200/kimchy/info/1?pretty' -d '{ "name" : "Shay Banon" }'

curl -XPUT 'http://localhost:9200/kimchy/tweet/1?pretty' -d '
{
    "user": "kimchy",
    "post_date": "2009-11-15T13:12:00",
    "message": "Trying out Elasticsearch, so far so good?"
}'

curl -XPUT 'http://localhost:9200/kimchy/tweet/2?pretty' -d '
{
    "user": "kimchy",
    "post_date": "2009-11-15T14:12:12",
    "message": "Another tweet, will it be indexed?"
}'
</pre>

The above will index information into the @kimchy@ index, with two types, @info@ and @tweet@. Each user will get their own special index.

Complete control on the index level is allowed. As an example, in the above case, we would want to change from the default 5 shards with 1 replica per index, to only 1 shard with 1 replica per index (== per twitter user). Here is how this can be done (the configuration can be in yaml as well):

<pre>
curl -XPUT http://localhost:9200/another_user?pretty -d '
{
    "index" : {
        "number_of_shards" : 1,
        "number_of_replicas" : 1
    }
}'
</pre>

Search (and similar operations) are multi index aware. This means that we can easily search on more than one
index (twitter user), for example:

<pre>
curl -XGET 'http://localhost:9200/kimchy,another_user/_search?pretty=true' -d '
{
    "query" : {
        "match_all" : {}
    }
}'
</pre>

Or on all the indices:

<pre>
curl -XGET 'http://localhost:9200/_search?pretty=true' -d '
{
    "query" : {
        "match_all" : {}
    }
}'
</pre>

{One liner teaser}: And the cool part about that? You can easily search on multiple twitter users (indices), with different boost levels per user (index), making social search so much simpler (results from my friends rank higher than results from friends of my friends).

### Distributed, Highly Available

Let's face it, things will fail....

Elasticsearch is a highly available and distributed search engine. Each index is broken down into shards, and each shard can have one or more replica. By default, an index is created with 5 shards and 1 replica per shard (5/1). There are many topologies that can be used, including 1/10 (improve search performance), or 20/1 (improve indexing performance, with search executed in a map reduce fashion across shards).

In order to play with the distributed nature of Elasticsearch, simply bring more nodes up and shut down nodes. The system will continue to serve requests (make sure you use the correct http port) with the latest data indexed.

### Where to go from here?

We have just covered a very small portion of what Elasticsearch is all about. For more information, please refer to the "elastic.co":http://www.elastic.co/products/elasticsearch website. General questions can be asked on the "Elastic Discourse forum":https://discuss.elastic.co or on IRC on Freenode at "#elasticsearch":https://webchat.freenode.net/#elasticsearch. The Elasticsearch GitHub repository is reserved for bug reports and feature requests only.

### Building from Source

Elasticsearch uses "Gradle":https://gradle.org for its build system. You'll need to have a modern version of Gradle installed - 2.13 should do.

In order to create a distribution, simply run the @gradle assemble@ command in the cloned directory.

The distribution for each project will be created under the @build/distributions@ directory in that project.

See the "TESTING":TESTING.asciidoc file for more information about
running the Elasticsearch test suite.

### Upgrading from elastic-match 1.x?

In order to ensure a smooth upgrade process from earlier versions of
Elasticsearch (1.x), it is required to perform a full cluster restart. Please
see the "setup reference":
https://www.elastic.co/guide/en/elasticsearch/reference/current/setup-upgrade.html
for more details on the upgrade process.

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
