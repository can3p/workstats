(ns popup
  (:require 
    [popup.timer :as timer]
    [popup.util :as util]))

(def timer-node (util/$ ".timer-current-time"))
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

(util/click pause (fn [] (if (timer/started? @time-struct)
                           (timer/stop! time-struct)
                           (do
                             (timer/start! time-struct)
                             (update-time)))))

(util/click restart (fn []
                      (timer/stop! time-struct)
                      (timer/create! time-struct)
                      (update-time)))

(update-time)
