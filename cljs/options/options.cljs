(ns options
  (:require
    [lib.util :as util]
    [lib.localstorage :as ls]))

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

(defn generate-result-html [result]
  (let [node (.createElement js/document "li")]
    (set! (.-innerHTML node) (:key result))
    node))

(defn populate-html [container results render-func]
  (doseq [result results]
    (.appendChild container (render-func result))))

(def results (get-results))
(def container (util/$ "#results"))

(populate-html container results generate-result-html)
