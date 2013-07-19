(ns popup
  (:require 
    [lib.localstorage :as ls]
    [popup.timer :as timer]
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

(defn store-result
  ([] (store-result (str "result_" (timer/get-current))))
  ([key]
   (ls/set-item
     key
     @time-struct)))

(defn restore-result []
  (let [result (ls/get-item "latest")]
    (ls/remove-item "latest")
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
                      (store-result)
                      (timer/create! time-struct)
                      (set-timer-state! "runs")
                      (update-time)))

(util/bind-event js/window "unload" (fn []
                                      (store-result "latest")))

(restore-result)

(if-not (timer/started? @time-struct)
  (set-timer-state! "paused"))

(when time-struct
  (set-time! (timer/get-length @time-struct)))

(update-time)
