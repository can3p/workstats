(ns lib.results
  (:require
    [lib.timer :as timer]
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

(defn store-result!
  ([struct] (store-result! struct (str "result_" (timer/get-current))))
  ([struct key]
   (ls/set-item
     key
     @struct)))

(defn get-latest! []
  (let [result (ls/get-item "latest")]
    (ls/remove-item "latest")
    result))
