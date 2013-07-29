(ns options
  (:require
    [clojure.string
     :refer [join]]
    [lib.util :as util]
    [lib.localstorage :as ls]))

(defn round [val]
  (js/Math.round val))

(defn get-keys []
  (.keys js/Object js/localStorage))

(defn get-result-keys []
  (sort >
        (filter #(re-find #"^result_" %1)
                (get-keys))))

(defn get-result [key]
  (ls/get-item key))

(defn get-results []
  (let [
        keys (get-result-keys)
        results (map #(assoc (get-result %1)
                             :key
                             %1) keys)]
    results))

(defn get-seconds [range]
  (round (/ (apply - range) -1000)))

(declare populate-html)

(defn generate-result-html [result container]
  (let [node (.createElement js/document "li")]
    (.appendChild container node)
    (new js/Chronoline node
         (clj->js [
                  {
                   :dates [(new js/Date 2012 3 9 14 15) (new js/Date 2012 3 9 19)]
                   :title "Coding"
                   }
                   {
                    :dates [(new js/Date 2012 3 9 14 15) (new js/Date 2012 3 9 19)]
                    :title "Meetings"
                    :attrs {
                            :fill "#ff9e44"
                            :stroke "#ff9e44" }
                    }])
         (clj->js {
                   :animated true
                   :visibleSpan 86400000
                   :subLabel "day"
                   :subSubLabel "yearmonth"
                   :floatingSubLabels false
                   :defaultStartDate (new js/Date 2012 3 9)
                   }))
    node))

(defn time-string [time]
  (let [chunks (util/parse-time time)
        len (count chunks)
        labels (take-last len [" days " " hours " " mins " " secs"])]
    (join (util/join-seqs chunks labels))))

(defn populate-html [container results render-func]
  (doseq [result results]
    (render-func result container)))

(def results (get-results))
(def container (util/$ "#results"))

(populate-html container results generate-result-html)
