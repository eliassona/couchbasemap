(defproject couchbasemap "0.1.0-SNAPSHOT"
  :description "Couchbase persistent map"
  :url "https://github.com/eliassona/couchbasemap"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha10"]
                 [com.couchbase.client/java-client "2.4.7"]
                 [org.clojure/data.json "0.2.4"]
                 [criterium "0.4.3"]
                 [storagemap "0.2.0-SNAPSHOT"]]
    :java-source-paths ["java/src"]
)
