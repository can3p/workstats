(ns popup
  (:require [clojure.string :as string]))

(defn $ [selector]
  (.querySelector js/document selector))

(def timer ($ ".timer-current-time"))
(def start-time (.getTime (new js/Date)))

(defn div [number divider]
  (let [modulo (mod number divider)]
    (/ (- number modulo) divider)))

(defn log [msg]
  (.log js/console msg))

(defn populate-string [chr len]
  (if (= len 0)
    ""
    (+ chr (populate-string chr (dec len)))))

(defn pad [msg len chr]
  (let [base-str (.toString msg)
        add-len (- len (count base-str))]
    (if (< add-len 0)
      base-str
      (+ (populate-string chr add-len)
         base-str))))

(defn format-time [time]
  (let [
        stamp (div time 1000)
        format-recur (fn [stamp, times]
                       (if (= stamp 0)
                         (if (> (count times) 0)
                           (string/join ":" (reverse times))
                           "0")
                         (let [
                               remainder (pad (mod stamp 60) 2 "0")
                               divided (div stamp 60) ]
                           (recur divided
                                  (conj times remainder)))))]
    (log stamp)
    (format-recur stamp [])))

(defn set-time! [d]
  (let [
        time (.getTime d)
        date-str (format-time (- time start-time))]
    (set! (.-innerHTML timer) date-str)))

(defn update-time []
  (set-time! (new js/Date)))

(js/setInterval update-time 1000)
(update-time)
