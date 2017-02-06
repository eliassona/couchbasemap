(ns couchbasemap.core
  (:require [storagemap.core :refer [IStorage ISerializer IPersistence storage-map store! s-write! s-keys]]
            [clojure.data.json :as json])
  (:import [com.couchbase.client.java Bucket CouchbaseCluster]
           [com.couchbase.client.java.document RawJsonDocument]
           [com.couchbase.client.java.view ViewQuery]))

(deftype JsonSerializer []
  ISerializer
  (serialize [this data] (json/json-str data))
  (deserialize [this data] (json/read-str data)))

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