(ns popup
  (:require 
    [popup.util :as util]))

(def timer (util/$ ".timer-current-time"))
(def pause (util/$ ".timer-pause"))

(defn get-current-time []
  (.getTime (new js/Date)))

(def time-struct (atom {
                        :last-time (get-current-time)
                        :ranges []
                        :sum 0
                        }))

(defn get-time []
  (let [
        time (get-current-time)]
    (+ (:sum @time-struct)
       (- time (:last-time @time-struct)))))

(defn stop-time []
  (let [{:keys (last-time ranges sum)} @time-struct
        time (get-current-time)]
    (reset! time-struct {
                         :last-time nil
                         :ranges (conj ranges [last-time time])
                         :sum (+ sum (- time last-time))})
    (get-time)))

(defn start-time []
  (reset! time-struct (assoc @time-struct
                             :last-time (get-current-time)))
  (get-time))

(defn time-started? []
  (not= nil (:last-time @time-struct)))

(defn set-time! [time]
  (let [
        time-str (util/format-time time)]
    (set! (.-innerHTML timer) time-str)))

(defn update-time []
  (when (time-started?)
    (set-time! (get-time))
    (js/setTimeout update-time 1000)))

(util/click pause (fn [] (if (time-started?)
                           (stop-time)
                           (do
                             (start-time)
                             (update-time)))))
(update-time)
