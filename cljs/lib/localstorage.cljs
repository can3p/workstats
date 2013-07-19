(ns lib.localstorage
  (:require [cljs.reader :as reader]))

(defn get-item [item]
  (reader/read-string
    (.getItem js/localStorage item)))

(defn set-item [item value]
  (.setItem js/localStorage item (pr-str value)))

(defn remove-item [item]
  (.removeItem js/localStorage item))
