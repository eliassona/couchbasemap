(ns couchbasemap.core
  (:require [storagemap.core :refer [IStorage ISerializer IPersistence storage-map store! s-write! s-keys]]
            [clojure.data.json :as json])
  (:use [clojure.pprint]
        [criterium.core])
  (:import [com.couchbase.client.java Bucket CouchbaseCluster]
           [com.couchbase.client.java.env DefaultCouchbaseEnvironment]
           [com.couchbase.client.java.document RawJsonDocument SerializableDocument]
           [com.couchbase.client.java.view ViewQuery Stale]
           [com.couchbase.client.java.env DefaultCouchbaseEnvironment]
           [rx.schedulers Schedulers]
           [java.util.concurrent Executors]
           [storagemap.core NopSerializer]))

(deftype JsonSerializer []
  ISerializer
  (serialize [this data] (when data (json/json-str data)))
  (deserialize [this data] (when data (json/read-str data))))

(extend-type Bucket
  IStorage
 (s-write! [this k v] 
   (.upsert this (RawJsonDocument/create k v))) 
 (s-read [this k] (.content (.get this (RawJsonDocument/create k))))
 (s-delete! [this k] (.remove this (RawJsonDocument/create k)))
 (s-keys [this query] 
   (into #{} (map #(.id %) (.allRows (.query this (ViewQuery/from "allkeys" "allkeys")))))))



(defn couchbase-map 
  ([bucket prefix serializer]
    (storage-map bucket prefix serializer))
  ([bucket prefix]
    (couchbase-map bucket prefix (JsonSerializer.))))

(comment 
  (def executor (Executors/newFixedThreadPool 2))
  (def builder (.scheduler (DefaultCouchbaseEnvironment/builder) (Schedulers/from executor)))
  (def env (.build builder))
  (def c (CouchbaseCluster/create env))
  (def b (.openBucket c))
  (.disconnect c)
  (.shutdown env)
  (pprint (sort (map #(.getName %) (.keySet (Thread/getAllStackTraces)))))
  (.shutdown executor)
  )

