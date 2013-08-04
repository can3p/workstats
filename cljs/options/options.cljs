(ns options
  (:use-macros [dommy.macros :only [sel1 deftemplate]])
  (:require
    [clojure.string
     :refer [join]]
    [dommy.core]
    [lib.util :as util]
    [lib.results
     :refer [get-results]]))

(defn round [val]
  (js/Math.round val))

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

(defn time-string [time]
  (let [chunks (util/parse-time time)
        len (count chunks)
        labels (take-last len [" days " " hours " " mins " " secs"])]
    (join (util/join-seqs chunks labels))))

(deftemplate result-html [time]
             [:li [:div] [:p (str "Total work time: " time)]])

(defn generate-result-html [result container]
  (let [
        time (time-string (:sum result))
        node (result-html time)
        graph (.querySelector node "div")]

    (.appendChild container node)
    (setup-chronoline! graph (:ranges result))
    node))

(defn populate-html [container results render-func]
  (doseq [result results]
    (render-func result container)))

(def results (get-results))
(def container (sel1 :#results))

(populate-html container results generate-result-html)
