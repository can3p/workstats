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

(defn reset-date [d]
            (let [new_d (new js/Date d)]
              (do
                (.setHours new_d 8)
                (.setMinutes new_d 0)
                (.setSeconds new_d 0))
              new_d))

(defn setup-chronoline! [node ranges]
  (let [
        dates (map (fn [range]
                     {
                      :dates (map #(new js/Date %1) range)
                      :title "Coding"
                      })
                    ranges)]
    (new js/Chronoline node
         (clj->js dates)
         (clj->js {
                   :animated true
                   :visibleSpan 86400000
                   :subLabel "day"
                   :subSubLabel "yearmonth"
                   :floatingSubLabels false
                   :defaultStartDate (reset-date
                                       (first
                                         (:dates
                                           (first dates))))
                   }))))

(defn generate-result-html [result container]
  (let [node (.createElement js/document "li")]
    (.appendChild container node)
    (setup-chronoline! node (:ranges result))
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
