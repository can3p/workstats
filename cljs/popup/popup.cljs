(ns popup
  (:require 
    [lib.timer :as timer]
    [lib.results
     :refer [store-result! get-latest!]]
    [lib.util :as util]))

(def timer-node (util/$ ".timer-current-time"))
(def body (util/$ ".timer-body"))
(def pause (util/$ ".timer-pause"))
(def restart (util/$ ".timer-reset"))
(def time-struct (atom (timer/create)))

(defn set-time! [time]
  (let [
        time-str (util/format-time time)]
    (set! (.-innerHTML timer-node) time-str)))

(defn update-time []
  (when (timer/started? @time-struct)
    (set-time! (timer/get-length @time-struct))
    (js/setTimeout update-time 1000)))

(defn restore-result []
  (let [result (get-latest!)]
    (when result
      (reset! time-struct result))))

(defn set-timer-state! [state]
  (if (= state "paused")
    (do
      (.remove (.-classList body) "timer-runs")
      (.add (.-classList body) "timer-paused"))
    (do
      (.add (.-classList body) "timer-runs")
      (.remove (.-classList body) "timer-paused"))))

(util/click pause (fn [] (if (timer/started? @time-struct)
                           (do
                             (set-timer-state! "paused")
                             (timer/stop! time-struct))
                           (do
                             (set-timer-state! "runs")
                             (timer/start! time-struct)
                             (update-time)))))

(util/click restart (fn []
                      (timer/stop! time-struct)
                      (store-result! time-struct)
                      (timer/create! time-struct)
                      (set-timer-state! "runs")
                      (update-time)))

(util/bind-event js/window "unload" (fn []
                                      (store-result! time-struct "latest")))

(restore-result)

(if-not (timer/started? @time-struct)
  (set-timer-state! "paused"))

(when time-struct
  (set-time! (timer/get-length @time-struct)))

(update-time)
