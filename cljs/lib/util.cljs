(ns lib.util
  (:require 
    [clojure.string :as string]))

(defn $ [selector]
  (.querySelector js/document selector))

(defn bind-event [element event handle]
  (.addEventListener element event handle false))

(defn click [element handle]
  (bind-event element "click" handle))

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

(defn join-seqs [seq1 seq2]
  (if (empty? seq1)
    []
    (concat [(first seq1) (first seq2)]
          (join-seqs (rest seq1)
                     (rest seq2)))))

(defn format-date [stamp]
  (.toString (new js/Date stamp)))

(defn parse-time [time]
  (let [
        stamp (div time 1000)
        format-recur (fn [stamp, times]
                       (if (= stamp 0)
                         (if (> (count times) 0)
                           (reverse times)
                           [0])
                         (if (= (count times) 2)
                           (recur 0
                                  (conj times stamp))
                           (let [
                                 remainder (mod stamp 60)
                                 divided (div stamp 60) ]
                             (recur divided
                                    (conj times remainder))))
                         )
                       )
        ]
    (format-recur stamp [])))

(defn format-time [time delims]
  (string/join ":" (map
                     (fn [chunk] (pad (str chunk) 2 "0"))
                     (parse-time time))))
