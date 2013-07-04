(ns popup
  (:require 
    [popup.util :as util]))

(def timer (util/$ ".timer-current-time"))
(def start-time (.getTime (new js/Date)))

(defn set-time! [d]
  (let [
        time (.getTime d)
        date-str (util/format-time (- time start-time))]
    (set! (.-innerHTML timer) date-str)))

(defn update-time []
  (set-time! (new js/Date)))

(js/setInterval update-time 1000)
(update-time)
