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
(declare generate-range-html)

(defn generate-result-html [result]
  (let [node (.createElement js/document "li")
        container (.createElement js/document "ul")]
    (set! (.-innerHTML node) (:key result))
    (populate-html container (:ranges result) generate-range-html)
    (.appendChild node container)
    node))

(defn generate-range-html [range]
  (let [node (.createElement js/document "li")]
    (set! (.-innerHTML node) (str (get-seconds range)
                                  " secs: "
                                  (join " - "
                                   (map util/format-date range))))
    node))

(defn populate-html [container results render-func]
  (doseq [result results]
    (.appendChild container (render-func result))))

(def results (get-results))
(def container (util/$ "#results"))

(populate-html container results generate-result-html)
